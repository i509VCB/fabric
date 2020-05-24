package net.fabricmc.fabric.impl.registry.tag;

import net.minecraft.class_5318;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public final class DimensionTagContainer extends TagContainer<DimensionType> {
	private boolean bound;

	public DimensionTagContainer() {
		this(new EmptyRegistry<>());
	}

	public DimensionTagContainer(class_5318 dimensionTracker) {
		this(dimensionTracker.method_29116());
		this.bound = true;
	}

	private DimensionTagContainer(Registry<DimensionType> registry) {
		super(registry::getOrEmpty, "tags/dimensions", "dimensions");
	}

	public boolean isBound() {
		return this.bound;
	}
}
