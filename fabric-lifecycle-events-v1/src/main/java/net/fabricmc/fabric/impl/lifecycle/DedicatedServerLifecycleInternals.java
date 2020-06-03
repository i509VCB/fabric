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

package net.fabricmc.fabric.impl.lifecycle;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class DedicatedServerLifecycleInternals extends ServerLifecycleInternals implements DedicatedServerModInitializer {
	public DedicatedServerLifecycleInternals() {
		// On a dedicated server, the game instance is the server
		super(() -> (MinecraftServer) FabricLoader.getInstance().getGameInstance());
	}

	@Override
	public void onInitializeServer() {
	}
}
