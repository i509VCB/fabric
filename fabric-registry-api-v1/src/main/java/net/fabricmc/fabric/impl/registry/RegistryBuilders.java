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

import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.SimpleRegistry;

import net.fabricmc.fabric.api.registry.v1.RegistryBuilder;

public final class RegistryBuilders {
	public static <T> RegistryBuilder.WithFactory<T, SimpleRegistry<T>> createSimple() {
		return new SimpleRegistryBuilder<>();
	}

	public static <T> RegistryBuilder.Defaulted<T, DefaultedRegistry<T>> createDefaulted() {
		return new DefaultedRegistryBuilder<>();
	}

	private RegistryBuilders() {
	}

	private static final class DefaultedRegistryBuilder<T> extends RegistryBuilder.Defaulted<T, DefaultedRegistry<T>> {
		@Override
		protected DefaultedRegistry<T> supplyRegistry() {
			return new DefaultedRegistry<>(this.defaultId.toString(), this.key, this.lifecycle);
		}
	}

	private static final class SimpleRegistryBuilder<T> extends RegistryBuilder.WithFactory<T, SimpleRegistry<T>> {
		@Override
		protected SimpleRegistry<T> supplyRegistry() {
			return new SimpleRegistry<>(this.key, this.lifecycle);
		}
	}
}
