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

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.poi.PointOfInterestType;

public final class ExtraTagManager {
	private ExtraTagHandler<Biome> biomes = new ExtraTagHandler<>(ExtraContainers.BIOME_PACKET, ExtraContainers::createBiomeContainer, ApplicationConsumers::applyBiome);
	private ExtraTagHandler<BlockEntityType<?>> blockEntities = new ExtraTagHandler<>(ExtraContainers.BLOCK_ENTITY_TYPE_PACKET, ExtraContainers::createBlockEntityContainer, ApplicationConsumers::applyBlockEntity);
	private ExtraTagHandler<Enchantment> enchantments = new ExtraTagHandler<>(ExtraContainers.ENCHANTMENT_PACKET, ExtraContainers::createEnchantmentContainer, ApplicationConsumers::applyEnchantment);
	private ExtraTagHandler<PaintingMotive> paintingMotives = new ExtraTagHandler<>(ExtraContainers.PAINTING_MOTIVE_PACKET, ExtraContainers::createPaintingContainer, ApplicationConsumers::applyPainting);
	private ExtraTagHandler<ParticleType<?>> particleTypes = new ExtraTagHandler<>(ExtraContainers.PARTICLE_TYPE_PACKET, ExtraContainers::createParticleTypeContainer, ApplicationConsumers::applyParticleType);
	private ExtraTagHandler<PointOfInterestType> pointsOfInterest = new ExtraTagHandler<>(ExtraContainers.POINT_OF_INTEREST_TYPE_PACKET, ExtraContainers::createPointOfInterestTypeContainer, ApplicationConsumers::applyPointOfInterestType);
	private ExtraTagHandler<Potion> potions = new ExtraTagHandler<>(ExtraContainers.POTION_PACKET, ExtraContainers::createPotionContainer, ApplicationConsumers::applyPotion);
	private ExtraTagHandler<SoundEvent> sounds = new ExtraTagHandler<>(ExtraContainers.SOUND_EVENTS_PACKET, ExtraContainers::createSoundEventContainer, ApplicationConsumers::applySounds);
	private ExtraTagHandler<StatusEffect> statusEffects = new ExtraTagHandler<>(ExtraContainers.STATUS_EFFECTS_PACKET, ExtraContainers::createStatusEffectContainer, ApplicationConsumers::applyStatusEffects);
	private ExtraTagHandler<VillagerProfession> villagerProfessions = new ExtraTagHandler<>(ExtraContainers.VILLAGER_PROFESSION_PACKET, ExtraContainers::createVillagerProfessionContainer, ApplicationConsumers::applyVillagerProfession);
	private ExtraTagHandler<VillagerType> villagerTypes = new ExtraTagHandler<>(ExtraContainers.VILLAGER_TYPE_PACKET, ExtraContainers::createVillagerTypeContainer, ApplicationConsumers::applyVillagerType);

	public ExtraTagHandler<Biome> getBiomes() {
		return this.biomes;
	}

	public void setBiomes(ExtraTagHandler<Biome> biomes) {
		this.biomes = biomes;
	}

	public ExtraTagHandler<BlockEntityType<?>> getBlockEntities() {
		return this.blockEntities;
	}

	public void setBlockEntities(ExtraTagHandler<BlockEntityType<?>> blockEntities) {
		this.blockEntities = blockEntities;
	}

	public ExtraTagHandler<Enchantment> getEnchantments() {
		return this.enchantments;
	}

	public void setEnchantments(ExtraTagHandler<Enchantment> enchantments) {
		this.enchantments = enchantments;
	}

	public ExtraTagHandler<PaintingMotive> getPaintingMotives() {
		return this.paintingMotives;
	}

	public void setPaintingMotives(ExtraTagHandler<PaintingMotive> paintingMotives) {
		this.paintingMotives = paintingMotives;
	}

	public ExtraTagHandler<ParticleType<?>> getParticleTypes() {
		return this.particleTypes;
	}

	public void setParticleTypes(ExtraTagHandler<ParticleType<?>> particleTypes) {
		this.particleTypes = particleTypes;
	}

	public ExtraTagHandler<PointOfInterestType> getPointsOfInterest() {
		return this.pointsOfInterest;
	}

	public void setPointsOfInterest(ExtraTagHandler<PointOfInterestType> pointsOfInterest) {
		this.pointsOfInterest = pointsOfInterest;
	}

	public ExtraTagHandler<Potion> getPotions() {
		return this.potions;
	}

	public void setPotions(ExtraTagHandler<Potion> potions) {
		this.potions = potions;
	}

	public ExtraTagHandler<SoundEvent> getSounds() {
		return this.sounds;
	}

	public void setSounds(ExtraTagHandler<SoundEvent> sounds) {
		this.sounds = sounds;
	}

	public ExtraTagHandler<StatusEffect> getStatusEffects() {
		return this.statusEffects;
	}

	public void setStatusEffects(ExtraTagHandler<StatusEffect> statusEffects) {
		this.statusEffects = statusEffects;
	}

	public ExtraTagHandler<VillagerProfession> getVillagerProfessions() {
		return this.villagerProfessions;
	}

	public void setVillagerProfessions(ExtraTagHandler<VillagerProfession> villagerProfessions) {
		this.villagerProfessions = villagerProfessions;
	}

	public ExtraTagHandler<VillagerType> getVillagerTypes() {
		return this.villagerTypes;
	}

	public void setVillagerTypes(ExtraTagHandler<VillagerType> villagerTypes) {
		this.villagerTypes = villagerTypes;
	}
	
	public void registerListeners(ReloadableResourceManager resourceManager) {
		resourceManager.registerListener(this.getBiomes());
		resourceManager.registerListener(this.getBlockEntities());
		resourceManager.registerListener(this.getEnchantments());
		resourceManager.registerListener(this.getPaintingMotives());
		resourceManager.registerListener(this.getParticleTypes());
		resourceManager.registerListener(this.getPointsOfInterest());
		resourceManager.registerListener(this.getPotions());
		resourceManager.registerListener(this.getSounds());
		resourceManager.registerListener(this.getVillagerProfessions());
		resourceManager.registerListener(this.getVillagerTypes());
	}

	public void applyAll() {
		this.getBiomes().apply();
		this.getBlockEntities().apply();
		this.getEnchantments().apply();
		this.getPaintingMotives().apply();
		this.getParticleTypes().apply();
		this.getPointsOfInterest().apply();
		this.getPotions().apply();
		this.getSounds().apply();
		this.getVillagerProfessions().apply();
		this.getVillagerTypes().apply();
	}
}
