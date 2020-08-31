package net.fabricmc.fabric.impl.resource.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.client.api.resource.v1.ClientReloadListenerRegistry;

@Environment(EnvType.CLIENT)
public final class ClientReloadListenerInternals {
	private static final List<ClientReloadListenerRegistry.ReloadListenerFactory> RELOAD_LISTENER_FACTORIES = new ArrayList<>();

	public static void registerReloadListener(ClientReloadListenerRegistry.ReloadListenerFactory factory) {
		Objects.requireNonNull(factory, "Reload listener factory cannot be null");
		ClientReloadListenerInternals.RELOAD_LISTENER_FACTORIES.add(factory);
	}

	private ClientReloadListenerInternals() {
	}
}
