package net.fabricmc.fabric.api.registry.tag.v1;

import net.fabricmc.fabric.api.tag.v1.TagRegistry;
import net.fabricmc.fabric.impl.registry.tag.ExtraTagInternals;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

public final class ExtraTagRegistry {
	// Please note, all tag entries should be alphabetical.
	private ExtraTagRegistry() {
	}

	public static Tag.Identified<Biome> biome(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getBiomeContainer);
	}

	public static Tag.Identified<BlockEntityType<?>> blockEntity(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getBlockEntityContainer);
	}

	public static Tag.Identified<DimensionType> dimension(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getDimensionTypeContainer);
	}

	public static Tag.Identified<Enchantment> enchantment(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getEnchantmentContainer);
	}

	public static Tag.Identified<PaintingMotive> painting(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getPaintingContainer);
	}

	public static Tag.Identified<ParticleType<?>> particleType(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getParticleTypeContainer);
	}

	public static Tag.Identified<PointOfInterestType> pointOfInterestType(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getPointOfInterestTypeContainer);
	}

	public static Tag.Identified<Potion> potion(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getPotionContainer);
	}

	public static Tag.Identified<SoundEvent> sound(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getSoundEventContainer);
	}

	public static Tag.Identified<StatusEffect> statusEffect(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getStatusEffectContainer);
	}

	public static Tag.Identified<VillagerProfession> villagerProfession(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getVillagerProfessionContainer);
	}

	public static Tag.Identified<VillagerType> villagerType(Identifier id) {
		return TagRegistry.create(id, ExtraTagInternals::getVillagerTypeContainer);
	}
}
