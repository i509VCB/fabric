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

package net.fabricmc.fabric.api.networking.v1.play;

import java.util.Objects;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.networking.v1.play.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ChannelHandlerRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayPacketSender;
import net.fabricmc.fabric.impl.networking.server.ServerNetworkingImpl;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkHandlerHook;

/**
 * Offers access to server-side networking functionalities.
 *
 * <p>Server-side networking functionalities include receiving serverbound packets,
 * sending clientbound packets, and events related to server-side network handlers.</p>
 *
 * <p>This class should be only used for the logical server.</p>
 *
 * @see ClientPlayNetworking
 */
public final class ServerPlayNetworking {
	/**
	 * Returns the packet receiver for channel handler registration on server play network
	 * handlers, receiving {@link net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
	 * client to server custom payload packets}.
	 */
	public static ChannelHandlerRegistry<PlayChannelHandler> getPlayReceivers() {
		return ServerNetworkingImpl.PLAY;
	}

	/**
	 * Returns the packet sender for a server play network handler.
	 *
	 * @param handler a server play network handler
	 * @return the associated packet sender
	 */
	public static PlayPacketSender getPlaySender(ServerPlayNetworkHandler handler) {
		Objects.requireNonNull(handler, "Network handler cannot be null");

		return ((ServerPlayNetworkHandlerHook) handler).getAddon();
	}

	/**
	 * Returns the packet sender for a server player.
	 *
	 * <p>This is a shortcut for {@link #getPlaySender(ServerPlayNetworkHandler)}.</p>
	 *
	 * @param player a server player
	 * @return the associated packet sender
	 */
	public static PlayPacketSender getPlaySender(ServerPlayerEntity player) {
		Objects.requireNonNull(player, "Player cannot be null");
		Objects.requireNonNull(player.networkHandler, "Player's network handler cannot be null");

		return getPlaySender(player.networkHandler);
	}

	/**
	 * Sends a packet to a player.
	 *
	 * @param player the player to send the packet to
	 * @param channel the channel of the packet
	 * @param buf the payload of the packet.
	 */
	public static void send(ServerPlayerEntity player, Identifier channel, PacketByteBuf buf) {
		getPlaySender(player).sendPacket(channel, buf);
	}

	/**
	 * Returns the <i>Minecraft</i> Server of a server play network handler.
	 *
	 * @param handler the server play network handler
	 */
	public static MinecraftServer getServer(ServerPlayNetworkHandler handler) {
		Objects.requireNonNull(handler, "Network handler cannot be null");

		return handler.player.server;
	}

	@FunctionalInterface
	public interface PlayChannelHandler {
		/**
		 * Handles an incoming packet.
		 *
		 * <p>This method is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
		 * Modification to the game should be {@linkplain net.minecraft.util.thread.ThreadExecutor#submit(Runnable) scheduled} using the provided Minecraft server instance.
		 *
		 * <p>An example usage of this is to create an explosion where the player is looking:
		 * <pre>{@code
		 * ServerPlayNetworking.getPlayReceivers().register(new Identifier("mymod", "boom"), (handler, server, sender, buf) -&rt; {
		 * 	boolean fire = buf.readBoolean();
		 *
		 * 	// All operations on the server or world must be executed on the server thread
		 * 	server.execute(() -&rt; {
		 * 		ModPacketHandler.createExplosion(handler.player, fire);
		 * 	});
		 * });
		 * }</pre>
		 *
		 * @param handler the network handler that received this packet
		 * @param server the server
		 * @param sender the packet sender
		 * @param buf the payload of the packet
		 */
		void receive(ServerPlayNetworkHandler handler, MinecraftServer server, PlayPacketSender sender, PacketByteBuf buf);
	}
}
