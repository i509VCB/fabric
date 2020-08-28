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

package net.fabricmc.fabric.api.event.registry;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.registry.v1.FabricRegistryAttributes;

@Deprecated
public enum RegistryAttribute {
	/**
	 * Registry will be saved to disk when modded.
	 */
	PERSISTED {
		@Override
		public Identifier getNewAttributeId() {
			return null; // TODO
		}
	},

	/**
	 * Registry will be synced to the client when modded.
	 */
	SYNCED {
		@Override
		public Identifier getNewAttributeId() {
			return null; // TODO
		}
	},

	/**
	 * Registry has been modded.
	 */
	MODDED {
		@Override
		public Identifier getNewAttributeId() {
			return FabricRegistryAttributes.MODDED;
		}
	};

	/**
	 * Gets the id of this registry attribute.
	 * For use in new registry api.
	 *
	 * @return the id that represents this attribute
	 */
	public abstract Identifier getNewAttributeId();
}
