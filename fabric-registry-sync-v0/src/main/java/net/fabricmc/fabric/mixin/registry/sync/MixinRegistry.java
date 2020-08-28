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

package net.fabricmc.fabric.mixin.registry.sync;

import java.util.Set;

import com.mojang.serialization.Lifecycle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryRemovedCallback;
import net.fabricmc.fabric.api.registry.v1.RegistryExtensions;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;

// Must be lower priority (higher number) than v1 or we may NPE when reimplementing old events
@Mixin(value = Registry.class, priority = 1001)
public abstract class MixinRegistry<T> implements RegistryAttributeHolder, FabricRegistry {
	@Unique
	private RegistryExtensions<T> newExtensions;

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerOldEvents(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle, CallbackInfo ci) {
		this.newExtensions = (RegistryExtensions<T>) this;
		this.newExtensions.getEntryAddedEvent().register((rawId, id, key, object) -> RegistryEntryAddedCallback.event((Registry<T>) (Object) this).invoker().onEntryAdded(rawId, id, object));
		this.newExtensions.getEntryRemovedEvent().register((rawId, id, key, object) -> RegistryEntryRemovedCallback.event((Registry<T>) (Object) this).invoker().onEntryRemoved(rawId, id, object));
	}

	@Override
	public RegistryAttributeHolder addAttribute(RegistryAttribute attribute) {
		this.newExtensions.addAttribute(attribute.getNewAttributeId());
		return this;
	}

	@Override
	public boolean hasAttribute(RegistryAttribute attribute) {
		return this.newExtensions.hasAttribute(attribute.getNewAttributeId());
	}

	@Override
	public void build(Set<RegistryAttribute> attributes) {
		for (RegistryAttribute attribute : attributes) {
			this.newExtensions.addAttribute(attribute.getNewAttributeId());
		}
	}
}
