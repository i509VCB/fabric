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

package net.fabricmc.fabric.impl.tag.extra.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.tag.extra.ApplicationConsumers;
import net.fabricmc.fabric.impl.tag.extra.ExtraContainers;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagHandler;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManager;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManagerInternals;

@Environment(EnvType.CLIENT)
public class ExtraTagsClientNetworking implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("FabricExtraTags");

	@Override
	public void onInitializeClient() {
		this.registerHandler(ExtraContainers.BIOME_PACKET, ExtraContainers::createBiomeContainer, ApplicationConsumers::applyBiome, ExtraTagManager::setBiomes);
		this.registerHandler(ExtraContainers.BLOCK_ENTITY_TYPE_PACKET, ExtraContainers::createBlockEntityContainer, ApplicationConsumers::applyBlockEntity, ExtraTagManager::setBlockEntities);
		this.registerHandler(ExtraContainers.ENCHANTMENT_PACKET, ExtraContainers::createEnchantmentContainer, ApplicationConsumers::applyEnchantment, ExtraTagManager::setEnchantments);
		this.registerHandler(ExtraContainers.PAINTING_MOTIVE_PACKET, ExtraContainers::createPaintingContainer, ApplicationConsumers::applyPainting, ExtraTagManager::setPaintingMotives);
		this.registerHandler(ExtraContainers.PARTICLE_TYPE_PACKET, ExtraContainers::createParticleTypeContainer, ApplicationConsumers::applyParticleType, ExtraTagManager::setParticleTypes);
		this.registerHandler(ExtraContainers.POINT_OF_INTEREST_TYPE_PACKET, ExtraContainers::createPointOfInterestTypeContainer, ApplicationConsumers::applyPointOfInterestType, ExtraTagManager::setPointsOfInterest);
		this.registerHandler(ExtraContainers.POTION_PACKET, ExtraContainers::createPotionContainer, ApplicationConsumers::applyPotion, ExtraTagManager::setPotions);
		this.registerHandler(ExtraContainers.SOUND_EVENTS_PACKET, ExtraContainers::createSoundEventContainer, ApplicationConsumers::applySounds, ExtraTagManager::setSounds);
		this.registerHandler(ExtraContainers.STATUS_EFFECTS_PACKET, ExtraContainers::createStatusEffectContainer, ApplicationConsumers::applyStatusEffects, ExtraTagManager::setStatusEffects);
		this.registerHandler(ExtraContainers.VILLAGER_PROFESSION_PACKET, ExtraContainers::createVillagerProfessionContainer, ApplicationConsumers::applyVillagerProfession, ExtraTagManager::setVillagerProfessions);
		this.registerHandler(ExtraContainers.VILLAGER_TYPE_PACKET, ExtraContainers::createVillagerTypeContainer, ApplicationConsumers::applyVillagerType, ExtraTagManager::setVillagerTypes);
	}

	private <T> void registerHandler(Identifier identifier, Supplier<RegistryTagContainer<T>> factory, Consumer<RegistryTagContainer<T>> applicationConsumer, BiConsumer<ExtraTagManager, ExtraTagHandler<T>> loadConsumer) {
		ClientSidePacketRegistry.INSTANCE.register(identifier, (context, buffer) -> {
			LOGGER.info("Received sync packet for {} tags", identifier);
			final MinecraftClient client = (MinecraftClient) context.getTaskQueue();
			final ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

			if (networkHandler == null) { // If we don't have a network handler, release and ignore the packet
				buffer.release();
				return;
			}

			final ExtraTagHandler<T> extraTagHandler = ExtraTagHandler.fromPacket(identifier, factory, applicationConsumer, buffer);
			final ExtraTagManagerInternals tagManagerInternals = (ExtraTagManagerInternals) networkHandler;

			client.execute(() -> {
				LOGGER.info("Loading {} tags", identifier);
				loadConsumer.accept(tagManagerInternals.fabric_getExtraTagsManager(), extraTagHandler);

				// Do not apply if we are connected to our integrated server as server already does this for us on resource reloadd
				if (!networkHandler.getConnection().isLocal()) {
					LOGGER.info("Applying {} tags since we are remotely connected", identifier);
					extraTagHandler.apply();
				}
			});
		});
	}
}
