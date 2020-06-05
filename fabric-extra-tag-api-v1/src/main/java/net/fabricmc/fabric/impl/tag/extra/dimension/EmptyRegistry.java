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

package net.fabricmc.fabric.impl.tag.extra.dimension;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

/**
 * An empty registry.
 * @param <T> The type this registry should hold.
 */
final class EmptyRegistry<T> extends Registry<T> {
	EmptyRegistry() {
		super(null, Lifecycle.experimental());
	}

	@Override
	public Identifier getId(T entry) {
		return null;
	}

	@Override
	public Optional<RegistryKey<T>> getKey(T value) {
		return Optional.empty();
	}

	@Override
	public int getRawId(T entry) {
		return 0;
	}

	@Override
	public T get(RegistryKey<T> key) {
		return null;
	}

	@Override
	public T get(Identifier id) {
		return null;
	}

	@Override
	public Optional<T> getOrEmpty(Identifier id) {
		return Optional.empty();
	}

	@Override
	public Set<Identifier> getIds() {
		return Collections.emptySet();
	}

	@Override
	public boolean containsId(Identifier id) {
		return false;
	}

	@Override
	public boolean containsId(int id) {
		return false;
	}

	@Override
	public T get(int index) {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return Collections.emptyIterator();
	}

	public boolean isEmpty() {
		return true;
	}
}
