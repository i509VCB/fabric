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

package net.fabricmc.fabric.impl.item;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import net.fabricmc.fabric.api.item.v1.ItemExplosionResistanceRegistry;

public class ItemExplosionResistanceRegistryImpl implements ItemExplosionResistanceRegistry {
	private List<Item> explosionImmuneItems = new ArrayList<>();

	public static final ItemExplosionResistanceRegistryImpl INSTANCE = new ItemExplosionResistanceRegistryImpl();

	private ItemExplosionResistanceRegistryImpl() {
		explosionImmuneItems.add(Items.NETHER_STAR); // For vanilla functionality.
	}

	@Override
	public void register(Item item) {
		checkState(item != null, "Item cannot be null");
		checkState(item != Items.AIR, "Item cannot be air");

		this.explosionImmuneItems.add(item);
	}

	public Collection<Item> getExplosionImmuneItems() {
		return this.explosionImmuneItems;
	}
}
