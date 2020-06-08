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

package net.fabricmc.fabric.mixin.tag.extra.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManager;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManagerInternals;
import net.fabricmc.fabric.impl.tag.extra.dimension.DimensionTagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.world.dimension.DimensionTracker;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ExtraTagManagerInternals {
	@Shadow
	private DimensionTracker dimensionTracker;

	@Unique
	private ExtraTagManager extraTagManager = new ExtraTagManager();
	@Unique
	private DimensionTagManager dimensionTagManager;

	@Override
	public ExtraTagManager fabric_getExtraTagsManager() {
		return this.extraTagManager;
	}

	@Override
	public DimensionTagManager fabric_getDimensionTagManager() {
		return this.dimensionTagManager;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initTagManager(MinecraftClient minecraftClient, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo ci) {
		this.dimensionTagManager = new DimensionTagManager(this.dimensionTracker);
	}
}
