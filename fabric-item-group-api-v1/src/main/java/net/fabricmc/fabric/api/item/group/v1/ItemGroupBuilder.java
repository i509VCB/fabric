package net.fabricmc.fabric.api.item.group.v1;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;

public final class ItemGroupBuilder {
	private static final Supplier<ItemStack> EMPTY_SUPPLIER = Items.AIR::getStackForRender;
	private final Set<EnchantmentTarget> enchantments = EnumSet.noneOf(EnchantmentTarget.class);
	private Identifier id;
	private Supplier<ItemStack> iconSupplier = ItemGroupBuilder.EMPTY_SUPPLIER;
	private Consumer<List<ItemStack>> stacksToAppend;
	private Consumer<List<ItemStack>> stacksToPrepend;
	private String texture;
	private boolean renderName = true;
	private boolean scrollbar = true;
	private TooltipProvider tooltipProvider;

	/**
	 * Creates an item group using the supplied item's {@link Item#getStackForRender() default rendering item stack}.
	 *
	 * @param id the id of the group
	 * @param stackSupplier a supplier which gets the item to render as the icon.
	 * @return a new item group.
	 */
	public static ItemGroup createGroup(Identifier id, Supplier<ItemStack> stackSupplier) {
		return ItemGroupBuilder.create()
				.id(id)
				.icon(stackSupplier)
				.buildAndRegister();
	}

	public static ItemGroupBuilder create() {
		return new ItemGroupBuilder();
	}

	private ItemGroupBuilder() {
	}

	public ItemGroupBuilder id(Identifier id) {
		this.id = Objects.requireNonNull(id, "ItemGroup id cannot be null");
		return this;
	}

	public ItemGroupBuilder icon(Supplier<ItemStack> stackSupplier) {
		this.iconSupplier = Objects.requireNonNull(stackSupplier, "Supplier cannot be null");
		return this;
	}

	public ItemGroupBuilder texture(String texture) {
		this.texture = texture;
		return this;
	}

	public ItemGroupBuilder enchantment(EnchantmentTarget target) {
		this.enchantments.add(target);
		return this;
	}

	public ItemGroupBuilder enchantments(Iterable<EnchantmentTarget> targets) {
		for (EnchantmentTarget target : targets) {
			this.enchantments.add(target);
		}

		return this;
	}

	public ItemGroupBuilder prependItems(Consumer<List<ItemStack>> stacksToPrepend) {
		Objects.requireNonNull(stacksToPrepend, "Consumer prepending items cannot be null");

		// Combine the consumers if an entry already exists
		if (this.stacksToPrepend != null) {
			// Always apply the new entry after the current entry
			this.stacksToPrepend = this.stacksToPrepend.andThen(stacksToPrepend);
			return this;
		}

		this.stacksToPrepend = stacksToPrepend;
		return this;
	}

	public ItemGroupBuilder appendItems(Consumer<List<ItemStack>> stacksToAppend) {
		Objects.requireNonNull(stacksToAppend, "Consumer appending items cannot be null");

		// Combine the consumers if an entry already exists
		if (this.stacksToAppend != null) {
			// Always apply the new entry after the current entry
			this.stacksToAppend = this.stacksToAppend.andThen(stacksToAppend);
			return this;
		}

		this.stacksToAppend = stacksToAppend;
		return this;
	}

	public ItemGroupBuilder hideName() {
		this.renderName = false;
		return this;
	}

	public ItemGroupBuilder noScrollbar() {
		this.scrollbar = false;
		return this;
	}

	/**
	 * Specifies a custom tooltip renderer to render the tooltip of an item group with.
	 * @return
	 */
	public ItemGroupBuilder tooltip(TooltipProvider provider) {
		this.tooltipProvider = Objects.requireNonNull(provider, "Tooltip provider cannot be null");
		return this;
	}

	public ItemGroup buildAndRegister() {
		if (this.id == null) {
			throw new IllegalArgumentException("Item Group id is required");
		}

		// Use an instance of ItemGroup to expand the array
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		final String id = String.format("%s.%s", this.id.getNamespace(), this.id.getPath());

		final ItemGroup itemGroup = new ItemGroup(ItemGroup.GROUPS.length - 1, id) {
			@Override
			public ItemStack createIcon() {
				return ItemGroupBuilder.this.iconSupplier.get();
			}

			@Override
			public void appendStacks(DefaultedList<ItemStack> stacks) {
				if (ItemGroupBuilder.this.stacksToPrepend != null) {
					ItemGroupBuilder.this.stacksToPrepend.accept(stacks);
				}

				super.appendStacks(stacks);

				if (ItemGroupBuilder.this.stacksToAppend != null) {
					ItemGroupBuilder.this.stacksToAppend.accept(stacks);
				}
			}
		};

		if (!this.renderName) {
			itemGroup.setNoTooltip();
		}

		if (!this.scrollbar) {
			itemGroup.setNoScrollbar();
		}

		if (this.texture != null) {
			itemGroup.setTexture(this.texture);
		}

		if (!this.enchantments.isEmpty()) {
			itemGroup.setEnchantments(this.enchantments.toArray(new EnchantmentTarget[0]));
		}

		if (this.tooltipProvider != null) {
			((ItemGroupExtensions) itemGroup).fabric_setTooltipProvider(this.tooltipProvider);
		}

		return itemGroup;
	}
}
