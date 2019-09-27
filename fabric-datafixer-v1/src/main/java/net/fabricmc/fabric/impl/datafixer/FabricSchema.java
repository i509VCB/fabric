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

package net.fabricmc.fabric.impl.datafixer;

import java.util.Map;
import java.util.function.BiFunction;

import com.mojang.datafixers.schemas.Schema;

import net.minecraft.datafixers.Schemas;

/**
 * This is the Schema that all custom DataFixers use or fixing will fail because the TypeReferences would have not been registered to the fixer.
 * <p>
 * Please note when updating the API when a new Schema is added, any new registeredTypes in {@link #registerTypes(Schema, Map, Map)} should be added with a comment above it specifying the Schema Version name.
 * </p>
 */
public class FabricSchema {
	public static final BiFunction<Integer, Schema, Schema> MC = (version, parent) -> Schemas.getFixer().getSchema(19610); // Add logic to automatically grab version
}
