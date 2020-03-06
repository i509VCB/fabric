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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.fabricmc.fabric.impl.item.ItemExplosionResistanceRegistryImpl;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {
	/**
	 * So this is a fun little hack, since the game does a getItem() == Items.NETHER_STAR, we redirect the getItem call and return.
	 *
	 * @reason Explosion Immunity registry
	 * @param itemStack The item stack to test
	 * @return {@link Items#NETHER_STAR} if the item is on the registry, otherwise {@link Items#AIR}
	 */
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private Item fabric_isExplosionImmune(ItemStack itemStack) {
		Item item = itemStack.getItem();

		if (ItemExplosionResistanceRegistryImpl.INSTANCE.getExplosionImmuneItems().contains(item)) {
			return Items.NETHER_STAR;
		}

		return Items.AIR;
	}
}
