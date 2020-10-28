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

package net.fabricmc.fabric.test.networking.play;

import net.minecraft.text.Text;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.ClientNetworking;

public final class NetworkingPlayPacketClientTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientNetworking.getPlayReceivers().register(NetworkingPlayPacketTest.TEST_CHANNEL, (handler, client, sender, buf) -> {
			Text text = buf.readText();
			client.send(() -> client.inGameHud.setOverlayMessage(text, true));
		});
	}
}