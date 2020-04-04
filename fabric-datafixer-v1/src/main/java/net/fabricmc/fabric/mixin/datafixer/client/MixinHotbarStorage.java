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

package net.fabricmc.fabric.mixin.datafixer.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.options.HotbarStorage;
import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.impl.datafixer.FabricDataFixesImpl;

@Mixin(HotbarStorage.class)
public abstract class MixinHotbarStorage {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;write(Lnet/minecraft/nbt/CompoundTag;Ljava/io/File;)V"), method = "save()V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void dataFixer_addModdedFixerVersions(CallbackInfo ci, CompoundTag tag) {
		FabricDataFixesImpl.INSTANCE.addFixerVersions(tag);
	}
}