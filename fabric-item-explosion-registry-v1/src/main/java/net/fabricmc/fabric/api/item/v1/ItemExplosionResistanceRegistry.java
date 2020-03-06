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

package net.fabricmc.fabric.api.item.v1;

import net.minecraft.item.Item;

import net.fabricmc.fabric.impl.item.ItemExplosionResistanceRegistryImpl;

/**
 * A registry for items which are immune to explosions.
 *
 * <p>This is an attribute already held by the nether star.
 */
public interface ItemExplosionResistanceRegistry {
	ItemExplosionResistanceRegistry INSTANCE = ItemExplosionResistanceRegistryImpl.INSTANCE;

	/**
	 * Registers an item as immune to explosions.
	 * @param item The item to make immune to explosions.
	 */
	void register(Item item);
}
