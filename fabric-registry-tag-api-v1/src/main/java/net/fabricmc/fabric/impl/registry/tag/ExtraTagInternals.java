package net.fabricmc.fabric.impl.registry.tag;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.registry.Registry;

public final class ExtraTagInternals {
	private static Map<Registry<?>, Supplier<TagContainer<?>>> TAG_CONTAINERS = new IdentityHashMap<>();

	public static <T> Supplier<TagContainer<T>> getContainer(Registry<T> registry) {
		// fixme: Proper path and type.
		//noinspection unchecked
		return (Supplier) TAG_CONTAINERS.computeIfAbsent(registry, key ->
				() -> new RegistryTagContainer<>(key, "tags/fixme", "fixme"));
	}
}
