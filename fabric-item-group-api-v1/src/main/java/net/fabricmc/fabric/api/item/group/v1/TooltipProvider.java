package net.fabricmc.fabric.api.item.group.v1;

import java.util.List;

import net.minecraft.text.Text;

@FunctionalInterface
public interface TooltipProvider {
	void provideTooltip(Text itemGroupName, List<Text> tooltipList);
}
