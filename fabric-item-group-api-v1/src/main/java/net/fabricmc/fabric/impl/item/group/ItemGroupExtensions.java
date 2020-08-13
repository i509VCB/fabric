package net.fabricmc.fabric.impl.item.group;

import net.fabricmc.fabric.api.item.group.v1.TooltipProvider;

public interface ItemGroupExtensions {
	void fabric_expandArray();

	TooltipProvider fabric_getTooltipProvider();

	void fabric_setTooltipProvider(TooltipProvider provider);
}
