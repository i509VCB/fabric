package net.fabricmc.fabric.api.registry.tag.v1;

import net.fabricmc.fabric.api.tag.v1.TagRegistry;
import net.fabricmc.fabric.impl.registry.tag.ExtraTagInternals;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public final class ExtraTagRegistry {
	private ExtraTagRegistry() {
	}

	public static Tag<Biome> biome(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals.getContainer(Registry.BIOME));
	}

	public static Tag<BlockEntityType<?>> blockEntity(Identifier id) {

	}

	public static Tag<Enchantment> enchantment(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals.getContainer(Registry.ENCHANTMENT));
	}

	public static Tag<Potion> potion(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals.getContainer(Registry.POTION));
	}
}
