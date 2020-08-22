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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import net.fabricmc.fabric.impl.registry.RegistryBuilders;
import net.fabricmc.fabric.mixin.registry.RegistryAccessor;

public abstract class RegistryBuilder<T, R extends MutableRegistry<T>> {
	protected Lifecycle lifecycle = Lifecycle.stable();
	protected final List<Identifier> attributes = new ArrayList<>();

	public static <T> RegistryBuilder.WithFactory<T, SimpleRegistry<T>> createSimple() {
		return RegistryBuilders.createSimple();
	}

	public static <T> RegistryBuilder.Defaulted<T, DefaultedRegistry<T>> createDefaulted() {
		return RegistryBuilders.createDefaulted();
	}

	protected RegistryBuilder() {
	}

	public RegistryBuilder<T, R> attribute(Identifier id) {
		Objects.requireNonNull(id, "Attribute cannot be null");
		this.attributes.add(id);
		return this;
	}

	public RegistryBuilder<T, R> lifecycle(Lifecycle lifecycle) {
		Objects.requireNonNull(lifecycle, "Cannot specify a null lifecycle");
		this.lifecycle = lifecycle;
		return this;
	}

	public R buildAndRegister() {
		// Supply the registry being built
		final R registry = Objects.requireNonNull(this.supplyRegistry(), "Supplied registry to build was null");
		final RegistryExtensions<T> extensions = RegistryExtensions.getExtensions(registry);

		// Apply all attributes
		for (Identifier attribute : this.attributes) {
			extensions.addAttribute(attribute);
		}

		this.register(registry);

		return registry;
	}

	/**
	 * Gets or creates the registry to use to "build".
	 *
	 * @return a registry.
	 */
	protected abstract R supplyRegistry();

	/**
	 * This is intentionally "protected final" as this method is only intended for use by for implementors of the builder.
	 * This is intended for registering a registry via the "root" registry.
	 *
	 * @param registry the registry to register
	 */
	protected final void register(MutableRegistry<T> registry) {
		//noinspection unchecked,rawtypes
		RegistryAccessor.getRootRegistry().add((RegistryKey) registry.getKey(), (MutableRegistry) registry, this.lifecycle);
	}

	/**
	 * A registry builder which creates new registry instances.
	 *
	 * @param <T> the type of object stored in the registry
	 * @param <R> the type of the registry
	 */
	public static abstract class WithFactory<T, R extends MutableRegistry<T>> extends RegistryBuilder<T, R> {
		protected RegistryKey<R> key;

		public RegistryBuilder.WithFactory<T, R> attribute(Identifier id) {
			super.attribute(id);
			return this;
		}

		public RegistryBuilder.WithFactory<T, R> lifecycle(Lifecycle lifecycle) {
			super.lifecycle(lifecycle);
			return this;
		}

		public RegistryBuilder.WithFactory<T, R> key(RegistryKey<R> key) {
			Objects.requireNonNull(key);
			this.key = key;
			return this;
		}

		@Override
		public R buildAndRegister() {
			if (this.key == null) {
				throw new IllegalArgumentException("Registry key must be specified.");
			}

			return super.buildAndRegister();
		}
	}

	/**
	 * A registry builder which creates a new defaulted registry instance.
	 *
	 * @param <T> the type of object stored in the registry
	 * @param <R> the type of the registry
	 */
	public static abstract class Defaulted<T, R extends DefaultedRegistry<T>> extends RegistryBuilder.WithFactory<T, R> {
		protected Identifier defaultId;

		public RegistryBuilder.Defaulted<T, R> attribute(Identifier id) {
			super.attribute(id);
			return this;
		}

		public RegistryBuilder.Defaulted<T, R> lifecycle(Lifecycle lifecycle) {
			super.lifecycle(lifecycle);
			return this;
		}

		public RegistryBuilder.Defaulted<T, R> key(RegistryKey<R> key) {
			super.key(key);
			return this;
		}

		public RegistryBuilder.Defaulted<T, R> defaultValue(Identifier defaultId) {
			this.defaultId = defaultId;
			return this;
		}

		@Override
		public R buildAndRegister() {
			if (this.defaultId == null) {
				throw new IllegalArgumentException("Default id must be specified.");
			}

			return super.buildAndRegister();
		}
	}
}
