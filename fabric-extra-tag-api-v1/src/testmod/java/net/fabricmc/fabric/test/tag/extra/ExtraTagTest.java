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

package net.fabricmc.fabric.test.tag.extra;

import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.function.Supplier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.tag.extra.v1.ExtraTagRegistry;
import net.fabricmc.fabric.impl.tag.extra.ExtraContainers;

public class ExtraTagTest implements ModInitializer {
	private static final DynamicCommandExceptionType INVALID_TAG = new DynamicCommandExceptionType(identifier -> new LiteralText(String.format("Tag %s is not loaded", identifier)));
	private static final DynamicCommandExceptionType INVALID_ENTRY = new DynamicCommandExceptionType(identifier -> new LiteralText(String.format("%s is not a valid registry entry", identifier)));
	private static final Dynamic2CommandExceptionType NOT_PRESENT = new Dynamic2CommandExceptionType((tag, entry) -> new LiteralText(String.format("%s is not inside of tag %s", tag, entry)));
	public static final Tag.Identified<BlockEntityType<?>> FURNACES = ExtraTagRegistry.blockEntity(new Identifier("testmod", "furnaces"));

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(this::registerTagCommand);
		ServerTickCallback.EVENT.register(server -> testBlockEntityTags());
	}

	static void testBlockEntityTags() {
		if (FURNACES.values().isEmpty()) {
			throw new AssertionError("FURNACES should not be empty. (Hint: check resource loader)");
		}

		if (!FURNACES.contains(BlockEntityType.BLAST_FURNACE)) {
			throw new AssertionError("Furnaces tag did not contain BLAST_FURNACE");
		}

		if (!FURNACES.contains(BlockEntityType.FURNACE)) {
			throw new AssertionError("Furnaces tag did not contain FURNACE");
		}

		if (!FURNACES.contains(BlockEntityType.SMOKER)) {
			throw new AssertionError("Furnaces tag did not contain SMOKER");
		}
	}

	private void registerTagCommand(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(literal("fabric_tag_tests")
				.then(createInspectNode()));
	}

	private LiteralArgumentBuilder<ServerCommandSource> createInspectNode() {
		final LiteralArgumentBuilder<ServerCommandSource> inspect = literal("inspect");
		inspect.then(literal("biome").then(this.createBiomeNode()));
		inspect.then(literal("blockEntity").then(this.createBlockEntityNode()));
		inspect.then(literal("enchantment").then(this.createEnchantmentNode()));
		inspect.then(literal("painting").then(this.createPaintingNode()));
		inspect.then(literal("particleType").then(this.createParticleTypeNode()));
		inspect.then(literal("potion").then(this.createPotionNode()));
		inspect.then(literal("sound").then(this.createSoundNode()));
		inspect.then(literal("statusEffect").then(this.createStatusEffectNode()));
		inspect.then(literal("villagerProfession").then(this.createProfessionNode()));
		inspect.then(literal("villagerType").then(this.createVillagerTypeNode()));
		// TODO: Dimensions

		return inspect;
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createBiomeNode() {
		return this.createTagNode(Registry.BIOME, ExtraContainers::getBiomeContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createBlockEntityNode() {
		return this.createTagNode(Registry.BLOCK_ENTITY_TYPE, ExtraContainers::getBlockEntityContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createEnchantmentNode() {
		return this.createTagNode(Registry.ENCHANTMENT, ExtraContainers::getEnchantmentContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createPaintingNode() {
		return this.createTagNode(Registry.PAINTING_MOTIVE, ExtraContainers::getPaintingContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createParticleTypeNode() {
		return this.createTagNode(Registry.PARTICLE_TYPE, ExtraContainers::getParticleTypeContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createPotionNode() {
		return this.createTagNode(Registry.POTION, ExtraContainers::getPotionContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createSoundNode() {
		return this.createTagNode(Registry.SOUND_EVENT, ExtraContainers::getSoundEventContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createStatusEffectNode() {
		return this.createTagNode(Registry.STATUS_EFFECT, ExtraContainers::getStatusEffectContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createProfessionNode() {
		return this.createTagNode(Registry.VILLAGER_PROFESSION, ExtraContainers::getVillagerProfessionContainer);
	}

	private ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createVillagerTypeNode() {
		return this.createTagNode(Registry.VILLAGER_TYPE, ExtraContainers::getVillagerTypeContainer);
	}

	private <T> ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, Identifier>> createTagNode(Registry<T> registry, Supplier<RegistryTagContainer<T>> tagContainerSupplier) {
		return argument("tag", identifier())
				.suggests((context, builder) -> {
					return CommandSource.suggestIdentifiers(tagContainerSupplier.get().getKeys(), builder);
				})
				.then(argument("entry", identifier()).suggests((context, builder) -> {
					return CommandSource.suggestIdentifiers(registry.getIds(), builder);
				}).executes(context -> this.excecuteInspect(context, registry, tagContainerSupplier)));
	}

	private <T> int excecuteInspect(CommandContext<ServerCommandSource> context, Registry<?> registry, Supplier<RegistryTagContainer<T>> tagContainerSupplier) throws CommandSyntaxException {
		final Identifier tagId = getIdentifier(context, "tag");

		final RegistryTagContainer<T> tagContainer = tagContainerSupplier.get();

		if (!tagContainer.getKeys().contains(tagId)) {
			throw INVALID_TAG.create(tagId);
		}

		final Tag<T> tag = tagContainer.get(tagId);

		final Identifier entryId = getIdentifier(context, "entry");

		final T o = (T) registry.getOrEmpty(entryId).orElseThrow(() -> INVALID_ENTRY.create(entryId));

		if (tag.contains(o)) {
			context.getSource().sendFeedback(new LiteralText(String.format("%s is inside of tag %s", entryId, tagId)).styled(style -> style.withColor(Formatting.GREEN)), false);
			return 1;
		}

		throw NOT_PRESENT.create(entryId, tagId);
	}
}
