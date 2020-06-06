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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo info) {
		ClientTickEvents.END_CLIENT_TICK.invoker().onTick((MinecraftClient) (Object) this);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER), method = "stop")
	private void onStopping(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoker().onChangeLifecycle((MinecraftClient) (Object) this);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;close()V"), method = "stop")
	private void onStop(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STOPPED.invoker().onChangeLifecycle((MinecraftClient) (Object) this);
	}

	// We inject after the thread field is set so `ThreadExecutor#getThread` will work
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;thread:Ljava/lang/Thread;", shift = At.Shift.AFTER), method = "run")
	private void onStart(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STARTED.invoker().onChangeLifecycle((MinecraftClient) (Object) this);
	}
}