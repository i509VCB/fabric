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

package net.fabricmc.fabric.mixin.tag.extra;

import java.util.List;

import net.fabricmc.fabric.impl.tag.extra.ExtraTagManager;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManagerInternals;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Shadow
	public abstract MinecraftServer getServer();
	@Shadow
	public abstract List<ServerPlayerEntity> getPlayerList();

	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
	private void syncExtraTagsOnConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ExtraTagNetworking.sendPackets(this.fabric_getExtraTagManager(), player);
	}

	@Inject(method = "onDataPacksReloaded", at = @At("TAIL"))
	private void syncExtraTagsOnReload(CallbackInfo ci) {
		ExtraTagNetworking.sendPacketsToAll(this.fabric_getExtraTagManager(), this.getPlayerList());
	}

	@Unique
	private ExtraTagManager fabric_getExtraTagManager() {
		ExtraTagManagerInternals manager = (ExtraTagManagerInternals) this.getServer();
		return manager.fabric_getExtraTagsManager();
	}
}
