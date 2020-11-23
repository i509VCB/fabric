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

package net.fabricmc.fabric.mixin.item;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.item.v1.BundleOccupancyProvider;
import net.fabricmc.fabric.impl.item.ItemExtensions;

@Mixin(BundleItem.class)
abstract class BundleItemMixin {
	@Inject(method = "getItemOccupancy", at = @At("HEAD"), cancellable = true)
	private static void applyCustomItemOccupancyProvider(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		@Nullable
		final BundleOccupancyProvider bundleOccupancyProvider = ((ItemExtensions) stack.getItem()).fabric_getBundleOccupancyProvider();

		if (bundleOccupancyProvider != null) {
			info.setReturnValue(bundleOccupancyProvider.getOccupancy(stack));
		}
	}
}
