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

package net.fabricmc.fabric.api.tag.extra.v1;

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
import net.minecraft.world.poi.PointOfInterestType;

import net.fabricmc.fabric.api.tag.v1.TagRegistry;
import net.fabricmc.fabric.impl.tag.extra.ExtraContainers;

public final class ExtraTagRegistry {
	// Please note, all tag entries should be alphabetical.
	private ExtraTagRegistry() {
	}

	public static Tag.Identified<Biome> biome(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getBiomeContainer);
	}

	public static Tag.Identified<BlockEntityType<?>> blockEntity(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getBlockEntityContainer);
	}

	public static Tag.Identified<Enchantment> enchantment(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getEnchantmentContainer);
	}

	public static Tag.Identified<PaintingMotive> painting(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getPaintingContainer);
	}

	public static Tag.Identified<ParticleType<?>> particleType(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getParticleTypeContainer);
	}

	public static Tag.Identified<PointOfInterestType> pointOfInterestType(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getPointOfInterestTypeContainer);
	}

	public static Tag.Identified<Potion> potion(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getPotionContainer);
	}

	public static Tag.Identified<SoundEvent> sound(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getSoundEventContainer);
	}

	public static Tag.Identified<StatusEffect> statusEffect(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getStatusEffectContainer);
	}

	public static Tag.Identified<VillagerProfession> villagerProfession(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getVillagerProfessionContainer);
	}

	public static Tag.Identified<VillagerType> villagerType(Identifier id) {
		return TagRegistry.create(id, ExtraContainers::getVillagerTypeContainer);
	}
}
