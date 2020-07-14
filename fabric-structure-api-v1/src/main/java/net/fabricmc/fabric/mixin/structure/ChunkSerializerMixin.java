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

package net.fabricmc.fabric.mixin.structure;

import java.util.Map;

import com.google.common.collect.BiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.ChunkSerializer;

/**
 * @deprecated Experimental. May be removed without notice.
 */
@Deprecated
@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
	@Unique
	private static final Logger LOGGER = LogManager.getLogger("FabricStructures");

	/**
	 * @author i509VCB
	 * @reason Emit log error when we find a missing structure.
	 */
	@Redirect(method = "readStructureReferences", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/BiMap;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static <K, V> V emitMissingStructureMessage(BiMap<K, V> map, K key) {
		final V value = map.get(key);

		if (value == null) {
			LOGGER.error("Found missing structure reference \"{}\"! Ignoring this structure.", key);
		}

		return value;
	}

	/**
	 * @author i509VCB
	 * @reason Do not load missing structures. Missing structures can cause lag, endless logs of exceptions and make chunks unable to save.
	 */
	@Redirect(method = "readStructureReferences", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private static <K, V> V ignoreMissingStructures(Map<K, V> map, K key, V value) {
		if (key == null) {
			// The key is null, do not add to the map
			return null;
		}

		return map.put(key, value);
	}
}

