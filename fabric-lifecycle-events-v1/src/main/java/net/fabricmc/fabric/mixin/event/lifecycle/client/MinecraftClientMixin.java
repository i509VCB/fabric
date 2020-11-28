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

package net.fabricmc.fabric.mixin.event.lifecycle.client;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	@Final
	private ResourcePackManager resourcePackManager;

	@Inject(at = @At("HEAD"), method = "tick")
	private void onStartTick(CallbackInfo info) {
		ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick((MinecraftClient) (Object) this);
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void onEndTick(CallbackInfo info) {
		ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick((MinecraftClient) (Object) this);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER), method = "stop")
	private void onStopping(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoker().onClientStopping((MinecraftClient) (Object) this);
	}

	// We inject after the thread field is set so `ThreadExecutor#getThread` will work
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;thread:Ljava/lang/Thread;", shift = At.Shift.AFTER), method = "run")
	private void onStart(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STARTED.invoker().onClientStarted((MinecraftClient) (Object) this);
	}

	// Only start reload if we have created a future (2nd and 3rd branch).
	@Inject(method = "reloadResources", at = @At(value = "NEW", target = "java/util/concurrent/CompletableFuture"))
	private void startResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		ClientLifecycleEvents.START_RESOURCE_PACK_RELOAD.invoker().startResourcePackReload((MinecraftClient) (Object) this, this.resourcePackManager);
	}

	// Resource reloads only attach handleAsync callback to branches which create a future
	@Inject(method = "reloadResources", at = {
			@At(value = "RETURN", ordinal = 1), // Overlay is splashscreen
			@At("TAIL") // Resource packs proper
	})
	private void endResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> info) {
		info.getReturnValue().handleAsync((value, throwable) -> {
			ClientLifecycleEvents.END_RESOURCE_PACK_RELOAD.invoker().endResourcePackReload((MinecraftClient) (Object) this, this.resourcePackManager, throwable == null);
			return value;
		}, (MinecraftClient) (Object) this);
	}
}
