package net.fabricmc.fabric.impl.provider.entity;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.provider.v1.ApiLookupMap;
import net.fabricmc.fabric.api.provider.v1.ContextKey;
import net.fabricmc.fabric.api.provider.v1.entity.EntityApiLookup;

public final class EntityApiLookupRegistryImpl {
	private static final ApiLookupMap<EntityApiLookupImpl<?, ?>> PROVIDERS = ApiLookupMap.create(EntityApiLookupImpl::new);

	public static <T, C> EntityApiLookup<T, C> getLookup(Identifier key, ContextKey<C> contextKey) {
		//noinspection unchecked
		return (EntityApiLookup<T, C>) PROVIDERS.getLookup(key, contextKey);
	}

	private EntityApiLookupRegistryImpl() {
	}
}
