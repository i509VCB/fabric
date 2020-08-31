package net.fabricmc.fabric.client.api.resource.v1;

import net.minecraft.client.MinecraftClient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.v1.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.impl.resource.client.ClientReloadListenerInternals;

@Environment(EnvType.CLIENT)
public final class ClientReloadListenerRegistry {
	public static void registerReloadListener(ReloadListenerFactory factory) {
		ClientReloadListenerInternals.registerReloadListener(factory);
	}

	/**
	 * A factory used to create a resource reload listener with a Minecraft client for context,
	 * The provided client instance may be stored to allow for access to the current Minecraft client.
	 */
	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface ReloadListenerFactory {
		IdentifiableResourceReloadListener create(MinecraftClient client);
	}
}
