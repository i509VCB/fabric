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

package net.fabricmc.fabric.impl.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.registry.v1.RegistryEvents;
import net.fabricmc.fabric.api.registry.v1.RegistryExtensions;

public final class RegistryDelegate<T> implements RegistryExtensions.Delegated<T> {
	private static final Map<RegistryKey<Registry<?>>, RegistryDelegate<?>> DELEGATES = new HashMap<>();
	private final RegistryKey<Registry<T>> key;
	private final List<Identifier> attributes = new ArrayList<>();
	private Event<RegistryEvents.EntryAdded<T>> entryAddedEvent = RegistryEventFactory.createAddedEvent();
	private Event<RegistryEvents.EntryRemoved<T>> entryRemovedEvent = RegistryEventFactory.createRemovedEvent();
	/**
	 * The currently bound, live registry.
	 * Every registry when constructed is given it's own delegate.
	 * If the registry is a dynamic registry, then
	 */
	/* @Nullable */
	private Registry<T> boundRegistry;

	public static <T> RegistryDelegate<T> getOrCreateDelegate(RegistryKey<Registry<T>> key) {
		//noinspection unchecked,rawtypes
		return RegistryDelegate.DELEGATES.computeIfAbsent((RegistryKey) key, k -> new RegistryDelegate<T>((RegistryKey) k));
	}

	public static boolean isDynamicRegistry(RegistryKey<Registry<?>> key) {

	}

	private RegistryDelegate(RegistryKey<Registry<T>> key) {
		this.key = key;
	}

	@Override
	public void addAttribute(Identifier attribute) {
		this.attributes.add(attribute); // Add to the delegate

		// If the registry is bound, add the attribute to the currently live registry as well.
		if (this.boundRegistry != null) {
			RegistryExtensions.getExtensions(this.boundRegistry).addAttribute(attribute);
		}
	}

	@Override
	public boolean hasAttribute(Identifier attribute) {
		// Query current registry if bound
		if (this.boundRegistry != null) {
			return RegistryExtensions.getExtensions(this.boundRegistry).hasAttribute(attribute);
		}

		return this.attributes.contains(attribute);
	}

	@Override
	public Event<RegistryEvents.EntryAdded<T>> getEntryAddedEvent() {
		return this.entryAddedEvent;
	}

	@Override
	public Event<RegistryEvents.EntryRemoved<T>> getEntryRemovedEvent() {
		return this.entryRemovedEvent;
	}

	public RegistryKey<Registry<T>> getKey() {
		return this.key;
	}

	public void bindTo(Registry<T> registry) {
		this.boundRegistry = registry;
		RegistryExtensions<T> extensions = RegistryExtensions.getExtensions(registry);

		// We've been bound to a new registry. We need to register all attributes and event listeners we have
		for (Identifier attribute : this.attributes) {
			extensions.addAttribute(attribute);
		}

		// Forwarding these events must be lambdas.
		extensions.getEntryAddedEvent().register((rawId, id, object) -> this.getEntryAddedEvent().invoker().onEntryAdded(rawId, id, object));
		extensions.getEntryRemovedEvent().register((rawId, id, object) -> this.getEntryRemovedEvent().invoker().onEntryRemoved(rawId, id, object));
	}
}
