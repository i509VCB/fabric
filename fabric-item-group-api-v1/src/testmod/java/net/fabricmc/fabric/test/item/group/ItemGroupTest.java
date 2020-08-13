package net.fabricmc.fabric.test.item.group;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.group.v1.ItemGroupBuilder;

public final class ItemGroupTest implements ModInitializer {
	@Override
	public void onInitialize() {
		ItemGroupBuilder.create()
				.id(new Identifier("fabric-testmod", "test_group"))
				.icon(() -> new ItemStack(Items.ACACIA_BOAT))
				.tooltip((itemGroupName, list) -> {
					list.add(itemGroupName);
					list.add(new LiteralText("Hmm this is taller").styled(style -> style.withBold(true)));
				})
				.appendItems(list -> {
					Registry.ITEM.stream().map(ItemStack::new).forEach(list::add);
				})
				.buildAndRegister();
	}
}
