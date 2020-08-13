package net.fabricmc.fabric.mixin.item.group.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemGroup;

import net.fabricmc.fabric.impl.item.group.client.FabricCreativeInventoryScreenWidgets;

@Mixin(ItemGroup.class)
abstract class ClientItemGroupMixin {
	@Shadow
	public abstract int getIndex();

	@Shadow
	public abstract boolean isTopRow();

	@Inject(method = "isTopRow", cancellable = true, at = @At("HEAD"))
	private void isTopRow(CallbackInfoReturnable<Boolean> info) {
		// If the idx is > 11, we are not on the root page
		if (this.getIndex() > 11) {
			// Get idx relative to second page.
			info.setReturnValue((this.getIndex() - 12) % (12 - FabricCreativeInventoryScreenWidgets.ALWAYS_VISIBLE_GROUPS.size()) < 4);
		}
	}

	@Inject(method = "getColumn", cancellable = true, at = @At("HEAD"))
	private void getColumn(CallbackInfoReturnable<Integer> info) {
		// If the idx is > 11, we are not on the root page
		if (this.getIndex() > 11) {
			// Get idx relative to second page.
			final int relativeIdx = this.getIndex() - 12;

			// Get amount of visible usable item groups we can
			final int usableItemGroups = 12 - FabricCreativeInventoryScreenWidgets.ALWAYS_VISIBLE_GROUPS.size();

			if (this.isTopRow()) {
				info.setReturnValue(relativeIdx % usableItemGroups);
			} else {
				// Subtract 4 to correct column positioning from groups on top of second+ page
				info.setReturnValue(relativeIdx % usableItemGroups - 4);
			}
		}
	}
}
