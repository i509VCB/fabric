package net.fabricmc.fabric.impl.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.resource.ServerResourceManager;

import net.fabricmc.fabric.api.resource.v1.ServerReloadListenerRegistry;

public final class ServerReloadListenerInternals {
	private static final List<ServerReloadListenerRegistry.ReloadListenerFactory> RELOAD_LISTENER_FACTORIES = new ArrayList<>();

	public static void registerReloadListener(ServerReloadListenerRegistry.ReloadListenerFactory factory) {
		Objects.requireNonNull(factory, "Reload listener factory cannot be null");
		ServerReloadListenerInternals.RELOAD_LISTENER_FACTORIES.add(factory);
	}

	private ServerReloadListenerInternals() {
	}
}
