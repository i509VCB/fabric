package net.fabricmc.fabric.mixin.tag.legacy;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.tag.v1.FabricTag;

@Mixin(FabricTag.class)
public interface LegacyFabricTagMixin<T> extends net.fabricmc.fabric.api.tag.FabricTag<T> {
}
