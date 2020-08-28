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

/**
 * An enumeration of all built-in registry attributes fabric's registry api defines.
 */
public final class FabricRegistryAttributes {
	/**
	 * Specifies that a registry has been modded.
	 * This attribute is applied when a registry has any new entries added after bootstrap, or new entries are not in the Minecraft namespace.
	 */
	public static final Identifier MODDED = new Identifier("fabric-registry-api-v1", "modded");

	private FabricRegistryAttributes() {
	}
}
