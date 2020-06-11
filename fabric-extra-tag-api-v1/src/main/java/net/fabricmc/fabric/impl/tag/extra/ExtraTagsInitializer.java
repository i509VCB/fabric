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

import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;

public class ExtraTagsInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		// We need to sync some registries in order to use tags on them:
		RegistryAttributeHolder.get(Registry.POTION).addAttribute(RegistryAttribute.SYNCED);
		RegistryAttributeHolder.get(Registry.BLOCK_ENTITY_TYPE).addAttribute(RegistryAttribute.SYNCED);
		RegistryAttributeHolder.get(Registry.POINT_OF_INTEREST_TYPE).addAttribute(RegistryAttribute.SYNCED);
	}
}
