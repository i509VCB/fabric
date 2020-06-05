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

import net.fabricmc.fabric.mixin.tag.extra.ModifiableDimensionTrackerAccessor;

import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.world.dimension.DimensionTracker;
import net.minecraft.world.dimension.DimensionType;

public final class DimensionTagManager {
	static RegistryTagContainer<DimensionType> DIMENSION_TYPE_TAG_CONTAINER = createEmptyDimensionContainer();

	private final RegistryTagContainer<DimensionType> dimensions;

	public DimensionTagManager(DimensionTracker dimensionTracker) {
		if (!(dimensionTracker instanceof DimensionTracker.Modifiable)) {
			throw new IllegalArgumentException("Do not know how to get registry from non-modifiable dimension tracker");
		}

		this.dimensions = createDimensionTypeContainer(dimensionTracker);
	}

	public static RegistryTagContainer<DimensionType> getDimensionTypeContainer() {
		return DIMENSION_TYPE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<DimensionType> createDimensionTypeContainer(DimensionTracker dimensionTracker) {
		return new RegistryTagContainer<>(((ModifiableDimensionTrackerAccessor) dimensionTracker).getRegistry(), "tags/dimensions", "dimensions");
	}

	public static RegistryTagContainer<DimensionType> createEmptyDimensionContainer() {
		return new RegistryTagContainer<>(new EmptyRegistry<>(), "tags/dimensions", "dimensions");
	}
}
