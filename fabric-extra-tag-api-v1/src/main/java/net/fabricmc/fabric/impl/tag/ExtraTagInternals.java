/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.tag;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionTracker;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

import net.fabricmc.fabric.mixin.tag.ModifiableDimensionTrackerAccessor;

public final class ExtraTagInternals {
	/**
	 * Identifier used by packets to send additional tags.
	 */
	public static final Identifier PACKET_ID = new Identifier("fabric", "extra_tags/v1");
	/**
	 * Minor version of the packet.
	 */
	public static final int MINOR_VERSION = 0;

	static RegistryTagContainer<Biome> BIOME_TAG_CONTAINER = createBiomeContainer();
	static RegistryTagContainer<BlockEntityType<?>> BLOCK_ENTITY_TAG_CONTAINER = createBlockEntityContainer();
	static RegistryTagContainer<Enchantment> ENCHANTMENT_TAG_CONTAINER = createEnchantmentContainer();
	static RegistryTagContainer<PaintingMotive> PAINTING_MOTIVE_TAG_CONTAINER = createPaintingContainer();
	static RegistryTagContainer<ParticleType<?>> PARTICLE_TYPE_TAG_CONTAINER = createParticleTypeContainer();
	static RegistryTagContainer<PointOfInterestType> POINT_OF_INTEREST_TYPE_TAG_CONTAINER = createPointOfInterestTypeContainer();
	static RegistryTagContainer<Potion> POTION_TAG_CONTAINER = createPotionContainer();
	static RegistryTagContainer<SoundEvent> SOUND_EVENTS_CONTAINER = createSoundEventContainer();
	static RegistryTagContainer<StatusEffect> STATUS_EFFECTS_CONTAINER = createStatusEffectContainer();
	static RegistryTagContainer<VillagerProfession> VILLAGER_PROFESSION_CONTAINER = createVillagerProfessionContainer();
	static RegistryTagContainer<VillagerType> VILLAGER_TYPE_CONTAINER = createVillagerTypeContainer();

	// Dimensions require special handling
	static RegistryTagContainer<DimensionType> DIMENSION_TYPE_TAG_CONTAINER = createEmptyDimensionContainer();

	public static RegistryTagContainer<Biome> getBiomeContainer() {
		return BIOME_TAG_CONTAINER;
	}

	static RegistryTagContainer<Biome> createBiomeContainer() {
		return new RegistryTagContainer<>(Registry.BIOME, "tags/biomes", "biomes");
	}

	public static RegistryTagContainer<BlockEntityType<?>> getBlockEntityContainer() {
		return BLOCK_ENTITY_TAG_CONTAINER;
	}

	static RegistryTagContainer<BlockEntityType<?>> createBlockEntityContainer() {
		return new RegistryTagContainer<>(Registry.BLOCK_ENTITY_TYPE, "tags/blockentities", "blockentities");
	}

	public static RegistryTagContainer<DimensionType> getDimensionTypeContainer() {
		return DIMENSION_TYPE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<DimensionType> createAndSetDimensionContainer(DimensionTracker dimensionTracker) {
		return new RegistryTagContainer<>(((ModifiableDimensionTrackerAccessor) dimensionTracker).getRegistry(), "tags/dimensions", "dimensions");
	}

	private static RegistryTagContainer<DimensionType> createEmptyDimensionContainer() {
		return new RegistryTagContainer<>(new EmptyRegistry<>(), "tags/dimensions", "dimensions");
	}

	public static RegistryTagContainer<Enchantment> getEnchantmentContainer() {
		return ENCHANTMENT_TAG_CONTAINER;
	}

	static RegistryTagContainer<Enchantment> createEnchantmentContainer() {
		return new RegistryTagContainer<>(Registry.ENCHANTMENT, "tags/enchantments", "enchantments");
	}

	public static RegistryTagContainer<PaintingMotive> getPaintingContainer() {
		return PAINTING_MOTIVE_TAG_CONTAINER;
	}

	static RegistryTagContainer<PaintingMotive> createPaintingContainer() {
		return new RegistryTagContainer<>(Registry.PAINTING_MOTIVE, "tags/painting_motives", "painting_motives");
	}

	public static RegistryTagContainer<ParticleType<?>> getParticleTypeContainer() {
		return PARTICLE_TYPE_TAG_CONTAINER;
	}

	static RegistryTagContainer<ParticleType<?>> createParticleTypeContainer() {
		return new RegistryTagContainer<>(Registry.PARTICLE_TYPE, "tags/particle_types", "particle_types");
	}

	public static RegistryTagContainer<PointOfInterestType> getPointOfInterestTypeContainer() {
		return POINT_OF_INTEREST_TYPE_TAG_CONTAINER;
	}

	static RegistryTagContainer<PointOfInterestType> createPointOfInterestTypeContainer() {
		return new RegistryTagContainer<>(Registry.POINT_OF_INTEREST_TYPE, "tags/points_of_interest", "points_of_interest");
	}

	public static RegistryTagContainer<Potion> getPotionContainer() {
		return POTION_TAG_CONTAINER;
	}

	static RegistryTagContainer<Potion> createPotionContainer() {
		return new RegistryTagContainer<>(Registry.POTION, "tags/potions", "potions");
	}

	public static RegistryTagContainer<SoundEvent> getSoundEventContainer() {
		return SOUND_EVENTS_CONTAINER;
	}

	static RegistryTagContainer<SoundEvent> createSoundEventContainer() {
		return new RegistryTagContainer<>(Registry.SOUND_EVENT, "tags/sounds", "sounds");
	}

	public static RegistryTagContainer<StatusEffect> getStatusEffectContainer() {
		return STATUS_EFFECTS_CONTAINER;
	}

	static RegistryTagContainer<StatusEffect> createStatusEffectContainer() {
		return new RegistryTagContainer<>(Registry.STATUS_EFFECT, "tags/status_effects", "status_effects");
	}

	public static RegistryTagContainer<VillagerProfession> getVillagerProfessionContainer() {
		return VILLAGER_PROFESSION_CONTAINER;
	}

	static RegistryTagContainer<VillagerProfession> createVillagerProfessionContainer() {
		return new RegistryTagContainer<>(Registry.VILLAGER_PROFESSION, "tags/villager_professions", "villager_professions");
	}

	public static RegistryTagContainer<VillagerType> getVillagerTypeContainer() {
		return VILLAGER_TYPE_CONTAINER;
	}

	static RegistryTagContainer<VillagerType> createVillagerTypeContainer() {
		return new RegistryTagContainer<>(Registry.VILLAGER_TYPE, "tags/villager_types", "villager_types");
	}
}
