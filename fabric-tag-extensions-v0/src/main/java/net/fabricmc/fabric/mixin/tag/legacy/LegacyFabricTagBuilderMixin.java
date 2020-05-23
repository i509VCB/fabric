package net.fabricmc.fabric.mixin.tag.legacy;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.tag.v1.FabricTagBuilder;

@Mixin(FabricTagBuilder.class)
public interface LegacyFabricTagBuilderMixin<T> extends net.fabricmc.fabric.api.tag.FabricTagBuilder<T> {
}
