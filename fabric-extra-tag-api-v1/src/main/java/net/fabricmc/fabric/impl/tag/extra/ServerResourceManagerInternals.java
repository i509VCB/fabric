package net.fabricmc.fabric.impl.tag.extra;

import net.minecraft.world.dimension.DimensionTracker;

public interface ServerResourceManagerInternals {
	void fabric_setTracker(DimensionTracker tracker);

	void fabric_lateInit();
}
