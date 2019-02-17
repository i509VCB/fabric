/*
 * Copyright (c) 2016, 2017, 2018 FabricMC
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

package net.fabricmc.fabric.mixin.networking;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.impl.network.ServerSidePacketRegistryImpl;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class MixinServerLoginNetworkHandler {
	@Inject(at = @At("HEAD"), method = "onQueryResponse", cancellable = true)
	public void onQueryResponse(LoginQueryResponseC2SPacket packet, CallbackInfo info) {
		if (((ServerSidePacketRegistryImpl) ServerSidePacketRegistry.INSTANCE).handleLoginResponse(packet)) {
			info.cancel();
		}
	}
}