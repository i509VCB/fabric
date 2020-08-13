package net.fabricmc.fabric.impl.item.group.client;

import java.util.HashSet;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import net.fabricmc.fabric.api.client.item.group.v1.CreativeInventoryScreenExtensions;

public final class FabricCreativeInventoryScreenWidgets {
	private static final Identifier BUTTON_TEXTURE = new Identifier("fabric", "textures/gui/creative_buttons.png");
	public static final Set<ItemGroup> ALWAYS_VISIBLE_GROUPS = Util.make(new HashSet<>(), set -> {
		set.add(ItemGroup.SEARCH);
		set.add(ItemGroup.INVENTORY);
		set.add(ItemGroup.HOTBAR);
	});

	/**
	 * Specifies whether fabric api should display item group buttons.
	 */
	public static boolean shouldShowButtons() {
		return true; // TODO: Configurable button visibility?
	}

	private static LiteralText getButtonText(CreativeInventoryScreenExtensions.ButtonType button) {
		switch (button) {
			case NEXT:
				return new LiteralText(">");
			case PREVIOUS:
				return new LiteralText("<");
		}

		return new LiteralText("");
	}

	public static class ItemGroupButtonWidget extends AbstractPressableButtonWidget {
		private final MinecraftClient client;
		private final Screen screen;
		private final CreativeInventoryScreenExtensions extensions;
		private final CreativeInventoryScreenExtensions.ButtonType type;

		public ItemGroupButtonWidget(MinecraftClient client, Screen screen, CreativeInventoryScreenExtensions extensions, CreativeInventoryScreenExtensions.ButtonType type, int x, int y) {
			super(x, y, 10, 11, getButtonText(type));
			this.client = client;
			this.screen = screen;
			this.extensions = extensions;
			this.type = type;
		}

		@Override
		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.visible = this.extensions.isVisible(this.type);
			this.active = this.extensions.isEnabled(this.type);

			if (this.visible) {
				int u = (this.active && this.isHovered() ? 22 : 0);
				u += this.type == CreativeInventoryScreenExtensions.ButtonType.NEXT ? 11 : 0;
				final int v = this.active ? 0 : 10;

				this.client.getTextureManager().bindTexture(BUTTON_TEXTURE);
				RenderSystem.disableLighting();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexture(matrices, this.x, this.y, u, v, this.width, this.height);

				if (this.hovered) {
					final int currentPage = this.extensions.getCurrentPage() + 1;
					final int totalPages = ((ItemGroup.GROUPS.length - 12) / 9) + 2;
					final Text text = new TranslatableText("fabric.gui.creativeTabPage", currentPage, totalPages);

					this.screen.renderTooltip(matrices, text, mouseX, mouseY);
				}
			}
		}

		@Override
		public void onPress() {
			switch (this.type) {
				case NEXT:
					this.extensions.nextPage();
					break;
				case PREVIOUS:
					this.extensions.previousPage();
					break;
			}
		}
	}

	private FabricCreativeInventoryScreenWidgets() {
	}
}
