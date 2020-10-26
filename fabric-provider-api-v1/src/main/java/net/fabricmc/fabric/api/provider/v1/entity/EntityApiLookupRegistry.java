package net.fabricmc.fabric.api.provider.v1.entity;

import java.util.Objects;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.provider.v1.ContextKey;
import net.fabricmc.fabric.impl.provider.entity.EntityApiLookupRegistryImpl;

public final class EntityApiLookupRegistry {
	public static <T, C> EntityApiLookup<T, C> getLookup(Identifier apiId, ContextKey<C> contextKey) {
		Objects.requireNonNull(apiId, "Id of API cannot be null");
		Objects.requireNonNull(contextKey, "Context key cannot be null");

		return EntityApiLookupRegistryImpl.getLookup(apiId, contextKey);
	}

	private EntityApiLookupRegistry() {
	}
}
