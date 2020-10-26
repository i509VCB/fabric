package net.fabricmc.fabric.api.provider.v1.entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.fabricmc.fabric.api.provider.v1.ApiLookup;

public interface EntityApiLookup<T, C> extends ApiLookup<C> {
	@Nullable
	T get(Entity entity, C context);

	<E extends Entity> void registerForEntityType(EntityApiProvider<E, T, C> provider, EntityType<E> entityType);

	<E extends Entity> void registerForEntityClass(EntityApiProvider<E, T, C> provider, Class<E> type);

	interface EntityApiProvider<E, T, C> {
		@Nullable
		T get(E entity, C context);
	}
}
