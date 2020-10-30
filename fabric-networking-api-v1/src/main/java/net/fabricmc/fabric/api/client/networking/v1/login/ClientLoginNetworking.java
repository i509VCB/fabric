package net.fabricmc.fabric.api.client.networking.v1.login;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl;

@Environment(EnvType.CLIENT)
public final class ClientLoginNetworking {
	/**
	 * Registers a handler to a channel.
	 *
	 * <p>If a handler is already registered to the {@code channel}, this method will return {@code false}, and no change will be made.
	 * Use {@link #unregister(Identifier)} to unregister the existing handler.</p>
	 *
	 * @param channel the id of the channel
	 * @param handler the handler
	 * @return false if a handler is already registered to the channel
	 */
	public static boolean register(Identifier channel, LoginChannelHandler handler) {
		Objects.requireNonNull(channel, "Channel cannot be null");
		Objects.requireNonNull(handler, "Channel login handler cannot be null");

		return ClientNetworkingImpl.LOGIN.register(channel, handler);
	}

	/**
	 * Removes the handler of a channel.
	 *
	 * <p>The {@code channel} is guaranteed not to have a handler after this call.</p>
	 *
	 * @param channel the id of the channel
	 * @return the previous handler, or {@code null} if no handler was bound to the channel
	 */
	@Nullable
	public static LoginChannelHandler unregister(Identifier channel) {
		Objects.requireNonNull(channel, "Channel cannot be null");

		return ClientNetworkingImpl.LOGIN.unregister(channel);
	}

	public static Collection<Identifier> getGlobalChannels() {
		return ClientNetworkingImpl.LOGIN.getChannels();
	}

	public static boolean hasGlobalChannel(Identifier channel) {
		return ClientNetworkingImpl.LOGIN.hasChannel(channel);
	}

	private ClientLoginNetworking() {
	}

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface LoginChannelHandler {
		/**
		 * Handles an incoming query request from a server.
		 *
		 * <p>This method is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
		 * Modification to the game should be {@linkplain net.minecraft.util.thread.ThreadExecutor#submit(Runnable) scheduled} using the provided Minecraft client instance.
		 *
		 * <p>The return value of this method is a completable future that may be used to delay the login process to the server until a task {@link CompletableFuture#isDone() is done}.
		 *
		 * @param handler the network handler that received this packet
		 * @param client the client
		 * @param buf the payload of the packet
		 * @param listenerAdder listeners to be called when the response packet is sent to the server
		 * @return a completable future which contains the payload to respond to the server with.
		 * If the future contains {@code null}, then the server will be notified that the client did not understand the query.
		 */
		CompletableFuture<@Nullable PacketByteBuf> receive(ClientLoginNetworkHandler handler, MinecraftClient client, PacketByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder);
	}
}
