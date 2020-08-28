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

package net.fabricmc.fabric.api.registry.v1;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.event.Event;

public interface RegistryExtensions<T> {
	/**
	 * Provides access to extensions applied to a registry.
	 *
	 * @param registry the registry
	 * @param <T> the type of object stored in the registry
	 * @return TODO
	 */
	static <T> RegistryExtensions<T> getExtensions(Registry<T> registry) {
		//noinspection unchecked
		return ((RegistryExtensions<T>) registry);
	}

	void addAttribute(Identifier attribute);

	boolean hasAttribute(Identifier attribute);

	Event<RegistryEvents.EntryAdded<T>> getEntryAddedEvent();

	Event<RegistryEvents.EntryRemoved<T>> getEntryRemovedEvent();
}
