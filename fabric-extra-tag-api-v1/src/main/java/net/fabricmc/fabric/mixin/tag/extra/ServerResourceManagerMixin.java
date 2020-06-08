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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.world.dimension.DimensionTracker;

@Mixin(ServerResourceManager.class)
public abstract class ServerResourceManagerMixin implements ExtraTagManagerInternals, ServerResourceManagerInternals {
	@Shadow
	@Final
	private ReloadableResourceManager resourceManager;

	@Unique
	private ExtraTagManager extraTagManager = new ExtraTagManager();
	@Unique
	private DimensionTagManager dimensionTagManager;
	/**
	 * We need access to the dimension tracker in order to use dimension tags
	 */
	@Unique
	private DimensionTracker dimensionTracker;

	@Override
	public ExtraTagManager fabric_getExtraTagsManager() {
		return this.extraTagManager;
	}

	@Override
	public DimensionTagManager fabric_getDimensionTagManager() {
		return this.dimensionTagManager;
	}

	// Bridge methods to handle dimensions

	@Override
	public void fabric_setTracker(DimensionTracker tracker) {
		this.dimensionTracker = tracker;
	}

	@Override
	public void fabric_lateInit() {
		this.dimensionTagManager = new DimensionTagManager(this.dimensionTracker);
		this.resourceManager.registerListener(this.dimensionTagManager.getHandler());
	}

	/**
	 * @author i509VCB
	 * @reason Why register the listener here rather than using ResourceManagerHelper?
	 * In order to sync the custom tag containers that fabric creates, we need to have a way to access the current FabricTagManager on a server instance.
	 * However registering in ResourceManagerHelper is insufficient as this leaves no way to access the FabricTagManager on the server.
	 * A way to avoid registration here would be much preferred.
	 * Since we implement IdentifiableResourceReloadListener, people can depend on us in their own resouce reload listeners
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	private void initExtraTagManager(CommandManager.RegistrationEnvironment registrationEnvironment, int i, CallbackInfo ci) {
		this.extraTagManager.registerListeners(this.resourceManager);
	}

	// Apply extra tags after vanilla
	@Inject(method = "loadRegistryTags", at = @At("TAIL"))
	private void applyExtraTags(CallbackInfo ci) {
		this.extraTagManager.applyAll();
		this.dimensionTagManager.apply();
	}
}
