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

package net.fabricmc.fabric.test.tag.extra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagNetworking;

@Environment(EnvType.CLIENT)
public class ExtraTagClientTest implements ClientModInitializer {
	private static int ticks;

	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(client -> {
			if (true) return; // TODO: IMPL Actual networking first

			if (ticks % 1000 == 0) {
				// We can only test this when connected to a dedicated server
				if (client.player != null && !client.isIntegratedServerRunning()) {
					// Only test if the server can understand us
					if (ClientSidePacketRegistry.INSTANCE.canServerReceive(ExtraTagNetworking.PACKET_ID)) {
						ExtraTagTest.testBlockEntityTags();
					}
				}
			}

			ticks++;
		});
	}
}
