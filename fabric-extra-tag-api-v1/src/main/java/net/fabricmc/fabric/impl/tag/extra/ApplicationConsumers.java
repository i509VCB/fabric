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

package net.fabricmc.fabric.impl.tag.extra;

import net.fabricmc.fabric.impl.tag.extra.dimension.DimensionTagManager;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

public final class ApplicationConsumers {
	public static void applyBiome(RegistryTagContainer<Biome> container) {
		ExtraContainers.BIOME_TAG_CONTAINER = container;
	}

	public static void applyBlockEntity(RegistryTagContainer<BlockEntityType<?>> container) {
		ExtraContainers.BLOCK_ENTITY_TAG_CONTAINER = container;
	}

	public static void applyEnchantment(RegistryTagContainer<Enchantment> container) {
		ExtraContainers.ENCHANTMENT_TAG_CONTAINER = container;
	}

	public static void applyPainting(RegistryTagContainer<PaintingMotive> container) {
		ExtraContainers.PAINTING_MOTIVE_TAG_CONTAINER = container;
	}

	public static void applyParticleType(RegistryTagContainer<ParticleType<?>> container) {
		ExtraContainers.PARTICLE_TYPE_TAG_CONTAINER = container;
	}

	public static void applyPointOfInterestType(RegistryTagContainer<PointOfInterestType> container) {
		ExtraContainers.POINT_OF_INTEREST_TYPE_TAG_CONTAINER = container;
	}

	public static void applyPotion(RegistryTagContainer<Potion> container) {
		ExtraContainers.POTION_TAG_CONTAINER = container;
	}

	public static void applySounds(RegistryTagContainer<SoundEvent> container) {
		ExtraContainers.SOUND_EVENTS_CONTAINER = container;
	}

	public static void applyStatusEffects(RegistryTagContainer<StatusEffect> container) {
		ExtraContainers.STATUS_EFFECTS_CONTAINER = container;
	}

	public static void applyVillagerProfession(RegistryTagContainer<VillagerProfession> container) {
		ExtraContainers.VILLAGER_PROFESSION_CONTAINER = container;
	}

	public static void applyVillagerType(RegistryTagContainer<VillagerType> container) {
		ExtraContainers.VILLAGER_TYPE_CONTAINER = container;
	}

	public static void applyDimensions(RegistryTagContainer<DimensionType> container) {
		DimensionTagManager.setDimensionTypeContainer(container);
	}
}
