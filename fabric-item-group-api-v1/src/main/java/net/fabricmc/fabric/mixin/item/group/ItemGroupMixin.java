package net.fabricmc.fabric.mixin.item.group;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.item.ItemGroup;

import net.fabricmc.fabric.api.item.group.v1.TooltipProvider;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;

@Mixin(ItemGroup.class)
abstract class ItemGroupMixin implements ItemGroupExtensions {
	@Shadow
	@Final
	@Mutable
	public static ItemGroup[] GROUPS;

	@Unique
	private TooltipProvider tooltipProvider;

	@Override
	public void fabric_expandArray() {
		ItemGroup[] tempGroups = ItemGroupMixin.GROUPS;
		ItemGroupMixin.GROUPS = new ItemGroup[ItemGroupMixin.GROUPS.length + 1];

		for (int i = 0; i < tempGroups.length; i++) {
			ItemGroupMixin.GROUPS[i] = tempGroups[i];
		}
	}

	@Override
	public TooltipProvider fabric_getTooltipProvider() {
		return this.tooltipProvider;
	}

	@Override
	public void fabric_setTooltipProvider(TooltipProvider provider) {
		this.tooltipProvider = provider;
	}
}
