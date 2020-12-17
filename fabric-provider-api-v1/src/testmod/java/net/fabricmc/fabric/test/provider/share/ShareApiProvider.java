package net.fabricmc.fabric.test.provider.share;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.world.ServerWorld;

@FunctionalInterface
public interface ShareApiProvider<T, C> {
	@Nullable
	T provide(ServerWorld world, C context);
}
