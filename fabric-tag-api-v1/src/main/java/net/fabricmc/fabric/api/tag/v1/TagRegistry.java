package net.fabricmc.fabric.api.tag.v1;

import java.util.function.Supplier;

import net.fabricmc.fabric.impl.tag.TagDelegate;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.Identifier;

/**
 * Helper methods for registering Tags.
 */
public final class TagRegistry {
	private TagRegistry() {
	}

	public static <T> Tag.Identified<T> create(Identifier id, Supplier<TagContainer<T>> containerSupplier) {
		return new TagDelegate<>(id, containerSupplier);
	}

	public static Tag.Identified<Block> block(Identifier id) {
		return create(id, BlockTags::getContainer);
	}

	public static Tag.Identified<EntityType<?>> entityType(Identifier id) {
		return create(id, EntityTypeTags::getContainer);
	}

	public static Tag.Identified<Fluid> fluid(Identifier id) {
		return create(id, FluidTags::getContainer);
	}

	public static Tag.Identified<Item> item(Identifier id) {
		return create(id, ItemTags::getContainer);
	}
}
