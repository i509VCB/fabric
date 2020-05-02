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

package net.fabricmc.fabric.api.object.builder.v1.world.poi;

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;

/**
 * This class provides utilities to create a {@link PointOfInterestType}.
 *
 * <p>A point of interest is typically used by villagers to specify their workstation blocks, meeting zones and homes.
 * Points of interest are also used by bees to specify where their bee hive is and nether portals to find existing portals.
 */
public final class PointOfInterestHelper {
	private PointOfInterestHelper() {
		throw new IllegalStateException("You shouldn't be initializing this!");
	}

	/**
	 * Creates and registers a {@link PointOfInterestType}.
	 *
	 * @param id The id of this {@link PointOfInterestType}.
	 * @param ticketCount The amount of tickets.
	 * @param searchDistance The search distance.
	 * @param blocks All the blocks this {@link PointOfInterestType} can be present on.
	 * @return a new {@link PointOfInterestType}.
	 */
	public static PointOfInterestType register(Identifier id, int ticketCount, int searchDistance, Block... blocks) {
		final ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();

		for (Block block : blocks) {
			builder.addAll(block.getStateManager().getStates());
		}

		return register(id, ticketCount, searchDistance, builder.build());
	}

	/**
	 * Creates and registers a {@link PointOfInterestType}.
	 *
	 * @param id The id of this {@link PointOfInterestType}.
	 * @param ticketCount The amount of tickets.
	 * @param completionCondition A {@link Predicate} which determines whether this point of interest type should be present.
	 * @param searchDistance The search distance.
	 * @param blocks All the blocks this {@link PointOfInterestType} can be present on.
	 * @return a new {@link PointOfInterestType}.
	 */
	public static PointOfInterestType register(Identifier id, int ticketCount, Predicate<PointOfInterestType> completionCondition, int searchDistance, Block... blocks) {
		final ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();

		for (Block block : blocks) {
			builder.addAll(block.getStateManager().getStates());
		}

		return register(id, ticketCount, completionCondition, searchDistance, builder.build());
	}

	/**
	 * Creates and registers a {@link PointOfInterestType}.
	 *
	 * @param id The id of this {@link PointOfInterestType}.
	 * @param ticketCount The amount of tickets.
	 * @param searchDistance The search distance.
	 * @param blocks A list of {@link BlockState BlockStates} which this {@link PointOfInterestType} can be present on.
	 * @return a new {@link PointOfInterestType}.
	 */
	public static PointOfInterestType register(Identifier id, int ticketCount, int searchDistance, Iterable<BlockState> blocks) {
		final ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();

		return register(id, ticketCount, searchDistance, builder.addAll(blocks).build());
	}

	/**
	 * Creates and registers a {@link PointOfInterestType}.
	 *
	 * @param id The id of this {@link PointOfInterestType}.
	 * @param ticketCount The amount of tickets.
	 * @param typePredicate A {@link Predicate} which determines whether this point of interest type should be present.
	 * @param searchDistance The search distance.
	 * @param states A list of {@link BlockState BlockStates} which this {@link PointOfInterestType} can be present on.
	 * @return a new {@link PointOfInterestType}.
	 */
	public static PointOfInterestType register(Identifier id, int ticketCount, Predicate<PointOfInterestType> typePredicate, int searchDistance, Iterable<BlockState> states) {
		final ImmutableSet.Builder<BlockState> builder = ImmutableSet.builder();

		return register(id, ticketCount, typePredicate, searchDistance, builder.addAll(states).build());
	}

	// INTERNAL METHODS

	private static PointOfInterestType register(Identifier id, int ticketCount, int searchDistance, Set<BlockState> states) {
		return Registry.register(Registry.POINT_OF_INTEREST_TYPE, id, PointOfInterestTypeAccessor.callSetup(
				PointOfInterestTypeAccessor.callCreate(id.toString(), states, ticketCount, searchDistance)));
	}

	private static PointOfInterestType register(Identifier id, int ticketCount, Predicate<PointOfInterestType> typePredicate, int searchDistance, Set<BlockState> states) {
		return Registry.register(Registry.POINT_OF_INTEREST_TYPE, id, PointOfInterestTypeAccessor.callSetup(
				PointOfInterestTypeAccessor.callCreate(id.toString(), states, ticketCount, typePredicate, searchDistance)));
	}
}
