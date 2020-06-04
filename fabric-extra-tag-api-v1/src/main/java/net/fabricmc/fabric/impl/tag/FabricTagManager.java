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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionTracker;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

public final class FabricTagManager implements IdentifiableResourceReloadListener {
	private final RegistryTagContainer<Biome> biomes = ExtraTagInternals.createBiomeContainer();
	private final RegistryTagContainer<BlockEntityType<?>> blockEntities = ExtraTagInternals.createBlockEntityContainer();
	private final RegistryTagContainer<Enchantment> enchantments = ExtraTagInternals.createEnchantmentContainer();
	private final RegistryTagContainer<PaintingMotive> paintings = ExtraTagInternals.createPaintingContainer();
	private final RegistryTagContainer<ParticleType<?>> particleTypes = ExtraTagInternals.createParticleTypeContainer();
	private final RegistryTagContainer<PointOfInterestType> pointsOfInterest = ExtraTagInternals.createPointOfInterestTypeContainer();
	private final RegistryTagContainer<Potion> potions = ExtraTagInternals.createPotionContainer();
	private final RegistryTagContainer<SoundEvent> soundEvents = ExtraTagInternals.createSoundEventContainer();
	private final RegistryTagContainer<StatusEffect> statusEffects = ExtraTagInternals.createStatusEffectContainer();
	private final RegistryTagContainer<VillagerProfession> villagerProfessions = ExtraTagInternals.createVillagerProfessionContainer();
	private final RegistryTagContainer<VillagerType> villagerTypes = ExtraTagInternals.createVillagerTypeContainer();
	private final RegistryTagContainer<DimensionType> dimensions;

	public FabricTagManager(DimensionTracker dimensionTracker) {
		if (!(dimensionTracker instanceof DimensionTracker.Modifiable)) {
			throw new IllegalArgumentException("Do not know how to get registry from non-modifiable dimension tracker");
		}

		this.dimensions = ExtraTagInternals.createAndSetDimensionContainer(dimensionTracker);
	}

	@Override
	public Identifier getFabricId() {
		return ExtraTagInternals.PACKET_ID;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		final CompletableFuture<Map<Identifier, Tag.Builder>> biomesFuture = this.biomes.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> blockEntitiesFuture = this.blockEntities.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> enchantmentsFuture = this.enchantments.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> paintingsFuture = this.paintings.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> particleTypesFuture = this.particleTypes.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> pointsOfInterestFuture = this.pointsOfInterest.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> potionsFuture = this.potions.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> soundEventsFuture = this.soundEvents.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> statusEffectsFuture = this.statusEffects.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> villagerProfessionsFuture = this.villagerProfessions.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> villagerTypesFuture = this.villagerTypes.prepareReload(manager, prepareExecutor);
		final CompletableFuture<Map<Identifier, Tag.Builder>> dimensionsFutures = this.dimensions.prepareReload(manager, prepareExecutor);

		// Take a long break
		final CompletableFuture<Void> allFutures = CompletableFuture.allOf(
				biomesFuture,
				blockEntitiesFuture,
				enchantmentsFuture,
				paintingsFuture,
				particleTypesFuture,
				pointsOfInterestFuture,
				potionsFuture,
				soundEventsFuture,
				statusEffectsFuture,
				villagerProfessionsFuture,
				villagerTypesFuture,
				dimensionsFutures
		);

		synchronizer.getClass();
		return allFutures.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(v -> {
			this.biomes.applyReload(biomesFuture.join());
			this.blockEntities.applyReload(blockEntitiesFuture.join());
			this.enchantments.applyReload(enchantmentsFuture.join());
			this.paintings.applyReload(paintingsFuture.join());
			this.particleTypes.applyReload(particleTypesFuture.join());
			this.pointsOfInterest.applyReload(pointsOfInterestFuture.join());
			this.potions.applyReload(potionsFuture.join());
			this.soundEvents.applyReload(soundEventsFuture.join());
			this.statusEffects.applyReload(statusEffectsFuture.join());
			this.villagerProfessions.applyReload(villagerProfessionsFuture.join());
			this.villagerTypes.applyReload(villagerTypesFuture.join());
			this.dimensions.applyReload(dimensionsFutures.join());
		});
	}

	public void toPacket(PacketByteBuf buf) {
		// TODO: WIP
		buf.writeInt(ExtraTagInternals.MINOR_VERSION);

		buf.writeBoolean(false);

		// End of Packet
		buf.writeBoolean(false);
	}

	public static FabricTagManager fromPacket(DimensionTracker dimensionTracker, PacketByteBuf buf) {
		final int minorVersion = buf.readInt();

		final FabricTagManager fabricTagManager = new FabricTagManager(dimensionTracker);

		buf.release(); // TODO: Remove after impl is finished
		return fabricTagManager;
	}

	public void apply() { // TODO: Apply these
		ExtraTagInternals.BIOME_TAG_CONTAINER = this.biomes;
		ExtraTagInternals.BLOCK_ENTITY_TAG_CONTAINER = this.blockEntities;
		ExtraTagInternals.ENCHANTMENT_TAG_CONTAINER = this.enchantments;
		ExtraTagInternals.PAINTING_MOTIVE_TAG_CONTAINER = this.paintings;
		ExtraTagInternals.PARTICLE_TYPE_TAG_CONTAINER = this.particleTypes;
		ExtraTagInternals.POINT_OF_INTEREST_TYPE_TAG_CONTAINER = this.pointsOfInterest;
		ExtraTagInternals.POTION_TAG_CONTAINER = this.potions;
		ExtraTagInternals.SOUND_EVENTS_CONTAINER = this.soundEvents;
		ExtraTagInternals.STATUS_EFFECTS_CONTAINER = this.statusEffects;
		ExtraTagInternals.VILLAGER_PROFESSION_CONTAINER = this.villagerProfessions;
		ExtraTagInternals.VILLAGER_TYPE_CONTAINER = this.villagerTypes;

		ExtraTagInternals.DIMENSION_TYPE_TAG_CONTAINER = this.dimensions;
	}

	public static List<Registry<?>> getSyncedRegistries() {
		final List<Registry<?>> registries = new ArrayList<>();
		// Supported already synced registries for tags v1.0
		registries.add(Registry.BIOME); // Synced ID
		registries.add(Registry.PAINTING_MOTIVE); // Synced ID
		registries.add(Registry.PARTICLE_TYPE); // Synced ID
		registries.add(Registry.SOUND_EVENT); // Synced ID
		registries.add(Registry.STATUS_EFFECT); // Synced ID
		registries.add(Registry.VILLAGER_PROFESSION); // Synced ID
		registries.add(Registry.VILLAGER_TYPE); // Synced ID
		registries.add(Registry.ENCHANTMENT); // Synced ID

		return registries;
	}

	// TODO: Mark these as synced or find a way to transport their info without a giant ballooning packet
	public static List<Registry<?>> getUnsyncedRegistries() {
		// Supported but unsynced registries for v1.0
		final List<Registry<?>> registries = new ArrayList<>();

		// Special cases
		registries.add(Registry.POTION); // Not synced by ID, register to sync ids
		registries.add(Registry.BLOCK_ENTITY_TYPE); // Not synced by ID, register to sync ids
		registries.add(Registry.POINT_OF_INTEREST_TYPE); // TODO: Not synced by ID, not even sent to clients

		return registries;
	}
}
