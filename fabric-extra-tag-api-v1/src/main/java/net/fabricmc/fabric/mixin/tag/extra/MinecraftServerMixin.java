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

import net.fabricmc.fabric.impl.tag.extra.ExtraTagManager;
import net.fabricmc.fabric.impl.tag.extra.ExtraTagManagerInternals;
import net.fabricmc.fabric.impl.tag.extra.ServerResourceManagerInternals;
import net.fabricmc.fabric.impl.tag.extra.dimension.DimensionTagManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionTracker;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ExtraTagManagerInternals {
	@Shadow
	private ServerResourceManager serverResourceManager;

	@Shadow
	@Final
	protected DimensionTracker.Modifiable dimensionTracker;

	@Override
	public ExtraTagManager fabric_getExtraTagsManager() {
		// Get the extra tag manager from the resource manager
		return ((ExtraTagManagerInternals) this.serverResourceManager).fabric_getExtraTagsManager();
	}

	@Override
	public DimensionTagManager fabric_getDimensionTagManager() {
		return ((ExtraTagManagerInternals) this.serverResourceManager).fabric_getDimensionTagManager();
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initDimensionTagManager(CallbackInfo ci) {
		// The ServerResourceManager does not have a RegistryTracker in scope. So we give it one
		((ServerResourceManagerInternals) this.serverResourceManager).fabric_setTracker(this.dimensionTracker);
		// Initialize the dimension tag manager now since we would need the tracker in ctor
		((ServerResourceManagerInternals) this.serverResourceManager).fabric_lateInit();
	}
}
