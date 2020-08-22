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

package net.fabricmc.fabric.mixin.registry;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Lifecycle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.registry.v1.RegistryEvents;
import net.fabricmc.fabric.api.registry.v1.RegistryExtensions;
import net.fabricmc.fabric.impl.registry.RegistryDelegate;
import net.fabricmc.fabric.impl.registry.RegistryEventFactory;

@Mixin(Registry.class)
abstract class RegistryMixin<T> implements RegistryExtensions<T> {
	@Shadow
	public abstract RegistryKey<? extends Registry<T>> getKey();

	@Unique
	private Event<RegistryEvents.EntryAdded<T>> entryAddedEvent;
	@Unique
	private Event<RegistryEvents.EntryRemoved<T>> entryRemovedEvent;
	@Unique
	private List<Identifier> attributes;

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle, CallbackInfo ci) {
		this.entryAddedEvent = RegistryEventFactory.createAddedEvent();
		this.entryRemovedEvent = RegistryEventFactory.createRemovedEvent();
		this.attributes = new ArrayList<>();

		// Verify this is not a dynamic registry. We attach attributes to dynamic registries later.
		if (!DynamicRegistryManagerAccessor.getDynamicRegistriesInfo().containsKey(this.getKey())) {
			// We are not using a dynamic registry; create the delegate and bind.
			RegistryDelegate<T> delegate = (RegistryDelegate<T>) RegistryDelegate.getOrCreateDelegate((RegistryKey) this.getKey());
			delegate.bindTo((Registry<T>) (Object) this);
		}
	}

	@Override
	public Event<RegistryEvents.EntryAdded<T>> getEntryAddedEvent() {
		return this.entryAddedEvent;
	}

	@Override
	public Event<RegistryEvents.EntryRemoved<T>> getEntryRemovedEvent() {
		return this.entryRemovedEvent;
	}

	@Override
	public void addAttribute(Identifier attribute) {
		this.attributes.add(attribute);
	}

	@Override
	public boolean hasAttribute(Identifier attribute) {
		return this.attributes.contains(attribute);
	}
}
