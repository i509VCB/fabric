package net.fabricmc.fabric.api.resource.v1;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.fabric.impl.resource.ServerReloadListenerInternals;

public final class ServerReloadListenerRegistry {
	public static void registerReloadListener(ReloadListenerFactory factory) {
		ServerReloadListenerInternals.registerReloadListener(factory);
	}

	/**
	 * A factory used to create a resource reload listener with a Minecraft server for context.
	 * The provided server instance may be stored in your resource reload listener for access to the current Minecraft server.
	 *
	 * <p>If the resulting resource reload listener is stored anywhere, it should be cleared when the server has finished shutting down for reference cleanup by using {@link net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents#SERVER_STOPPED}.
	 */
	@FunctionalInterface
	public interface ReloadListenerFactory {
		IdentifiableResourceReloadListener create(MinecraftServer server);
	}
}
