/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.networking;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.netty.util.AsciiString;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayPacketSender;

public abstract class AbstractChanneledNetworkAddon<H> extends AbstractNetworkAddon implements PlayPacketSender {
	protected final SimpleChannelHandlerRegistry<H> receiver;
	protected final Set<Identifier> sendableChannels;
	protected final Set<Identifier> sendableChannelsView;

	protected AbstractChanneledNetworkAddon(SimpleChannelHandlerRegistry<H> receiver, ClientConnection connection) {
		this(receiver, connection, new HashSet<>());
	}

	protected AbstractChanneledNetworkAddon(SimpleChannelHandlerRegistry<H> receiver, ClientConnection connection, Set<Identifier> sendableChannels) {
		super(connection);
		this.receiver = receiver;
		this.sendableChannels = sendableChannels;
		this.sendableChannelsView = Collections.unmodifiableSet(sendableChannels);
	}

	protected void registerPendingChannels(ChannelInfoHolder holder) {
		final Collection<Identifier> pending = holder.getChannels();

		if (!pending.isEmpty()) {
			register(new ArrayList<>(pending));
			pending.clear();
		}
	}

	// always supposed to handle async!
	protected boolean handle(Identifier channel, PacketByteBuf originalBuf) {
		// Handle reserved packets
		if (NetworkingImpl.REGISTER_CHANNEL.equals(channel)) {
			receiveRegistration(true, PacketByteBufs.slice(originalBuf));
			return true;
		}

		if (NetworkingImpl.UNREGISTER_CHANNEL.equals(channel)) {
			receiveRegistration(false, PacketByteBufs.slice(originalBuf));
			return true;
		}

		@Nullable H handler = this.receiver.get(channel);

		if (handler == null) {
			return false;
		}

		PacketByteBuf buf = PacketByteBufs.slice(originalBuf);

		try {
			receive(handler, buf);
		} catch (Throwable ex) {
			NetworkingImpl.LOGGER.error("Encountered exception while handling in channel \"{}\"", channel, ex);
			throw ex;
		}

		return true;
	}

	protected abstract void receive(H handler, PacketByteBuf buf);

	public void sendChannelRegistrationPacket() {
		Collection<Identifier> channels = this.receiver.getChannels();

		if (channels.isEmpty()) {
			return;
		}

		PacketByteBuf buf = PacketByteBufs.create();
		boolean first = true;

		for (Identifier channel : channels) {
			if (first) {
				first = false;
			} else {
				buf.writeByte(0);
			}

			buf.writeBytes(channel.toString().getBytes(StandardCharsets.US_ASCII));
		}

		this.sendPacket(NetworkingImpl.REGISTER_CHANNEL, buf);
	}

	// wrap in try with res (buf)
	protected void receiveRegistration(boolean register, PacketByteBuf buf) {
		List<Identifier> ids = new ArrayList<>();
		StringBuilder active = new StringBuilder();

		while (buf.isReadable()) {
			byte b = buf.readByte();

			if (b != 0) {
				active.append(AsciiString.b2c(b));
			} else {
				this.addId(ids, active);
				active = new StringBuilder();
			}
		}

		this.addId(ids, active);
		this.schedule(register ? () -> register(ids) : () -> unregister(ids));
	}

	public void register(List<Identifier> ids) {
		this.sendableChannels.addAll(ids);
		this.postRegisterEvent(ids);
	}

	public void unregister(List<Identifier> ids) {
		this.sendableChannels.removeAll(ids);
		this.postUnregisterEvent(ids);
	}

	protected abstract void schedule(Runnable task);

	protected abstract void postRegisterEvent(List<Identifier> ids);

	protected abstract void postUnregisterEvent(List<Identifier> ids);

	private void addId(List<Identifier> ids, StringBuilder sb) {
		String literal = sb.toString();

		try {
			ids.add(new Identifier(literal));
		} catch (InvalidIdentifierException ex) {
			NetworkingImpl.LOGGER.warn("Received invalid channel identifier \"{}\" from connection {}", literal, this.connection, ex);
		}
	}

	@Override
	public Collection<Identifier> getChannels() {
		return this.sendableChannelsView;
	}

	@Override
	public boolean hasChannel(Identifier channel) {
		return this.sendableChannels.contains(channel);
	}
}
