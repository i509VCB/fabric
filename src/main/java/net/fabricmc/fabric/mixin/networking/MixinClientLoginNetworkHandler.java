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

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.network.ClientSidePacketRegistryImpl;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.packet.LoginQueryRequestS2CPacket;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class MixinClientLoginNetworkHandler {
	private LoginQueryRequestS2CPacket fabric_loginQueryRequest;

	@Inject(at = @At("HEAD"), method = "onQueryRequest")
	public void captureLoginRequestPacket(LoginQueryRequestS2CPacket packet, CallbackInfo info) {
		if (fabric_loginQueryRequest != null) {
			throw new RuntimeException("Error: fabric_loginQueryRequest != null!");
		}

		fabric_loginQueryRequest = packet;
	}

	@ModifyArg(at = @At(value = "INVOKE", target = "net/minecraft/network/ClientConnection.sendPacket(Lnet/minecraft/network/Packet;)V"), method = "onQueryRequest")
	public Packet modifyLoginResponse(Packet packet) {
		if (fabric_loginQueryRequest == null) {
			throw new RuntimeException("Error: fabric_loginQueryRequest == null!");
		}

		Packet changedPacket = ((ClientSidePacketRegistryImpl) ClientSidePacketRegistry.INSTANCE).handleLoginRequest(fabric_loginQueryRequest);
		fabric_loginQueryRequest = null;
		return changedPacket != null ? changedPacket : packet;
	}
}
