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

package net.fabricmc.fabric.impl.tag.extra.dimension;

import net.fabricmc.fabric.impl.tag.extra.ApplicationConsumers;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagHandler;

import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionTracker;
import net.minecraft.world.dimension.DimensionType;

public final class DimensionTagManager {
	public static final Identifier PACKET = new Identifier("fabric", "extra_tags/dimensions/v1");
	static RegistryTagContainer<DimensionType> DIMENSION_TYPE_TAG_CONTAINER = createEmptyDimensionContainer();

	private ExtraTagHandler<DimensionType> dimensions;

	public DimensionTagManager(DimensionTracker dimensionTracker) {
		final RegistryTagContainer<DimensionType> dimensionTypeContainer = DimensionTagManager.createDimensionTypeContainer(dimensionTracker);
		this.dimensions = new ExtraTagHandler<>(PACKET, () -> dimensionTypeContainer, ApplicationConsumers::applyDimensions);
	}

	public DimensionTagManager() {
		this.dimensions = new ExtraTagHandler<>(PACKET, DimensionTagManager::createEmptyDimensionContainer, ApplicationConsumers::applyDimensions);
	}

	public ExtraTagHandler<DimensionType> getHandler() {
		return this.dimensions;
	}

	public void setHandler(ExtraTagHandler<DimensionType> dimensions) {
		this.dimensions = dimensions;
	}

	public void apply() {
		this.dimensions.apply();
	}

	public static RegistryTagContainer<DimensionType> getDimensionTypeContainer() {
		return DIMENSION_TYPE_TAG_CONTAINER;
	}

	public static void setDimensionTypeContainer(RegistryTagContainer<DimensionType> container) {
		DIMENSION_TYPE_TAG_CONTAINER = container;
	}

	public static RegistryTagContainer<DimensionType> createDimensionTypeContainer(DimensionTracker dimensionTracker) {
		final MutableRegistry<DimensionType> dimensionTypes = dimensionTracker.method_29726(Registry.DIMENSION_TYPE_KEY).orElseThrow(() -> new RuntimeException("Expected to find DimensionType registry in registry tracker"));
		return new RegistryTagContainer<>(dimensionTypes, "tags/dimensions", "dimensions");
	}

	/**
	 * Creates an empty tag container. This should be used when not in game.
	 */
	public static RegistryTagContainer<DimensionType> createEmptyDimensionContainer() {
		return new RegistryTagContainer<>(new EmptyRegistry<>(), "tags/dimensions", "dimensions");
	}
}
