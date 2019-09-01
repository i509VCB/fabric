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

package net.fabricmc.fabric.impl.network;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.network.login.ClientLoginQueryResponseRegistry;

public class FabricClientNetworkInitializer implements ClientModInitializer {
	protected static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient() {
	    LOGGER.info("[fabric-networking-handshake] Initalized Client Mod");
		ClientLoginQueryResponseRegistry.INSTANCE.register(FabricHelloPacketBuilder.ID, (handler, connection, id, buffer) -> {
			LOGGER.debug("fabric:hello received - connected to Fabric server!");

			return Optional.of(FabricHelloPacketBuilder.buildHelloPacket());
		});
	}
}
