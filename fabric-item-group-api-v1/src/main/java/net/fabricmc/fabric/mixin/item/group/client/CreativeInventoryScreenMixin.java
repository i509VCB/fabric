package net.fabricmc.fabric.mixin.item.group.client;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.item.group.v1.CreativeInventoryScreenExtensions;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.fabricmc.fabric.impl.item.group.client.CreativeInventoryScreenInternals;
import net.fabricmc.fabric.impl.item.group.client.FabricCreativeInventoryScreenWidgets;

@Mixin(CreativeInventoryScreen.class)
abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements CreativeInventoryScreenExtensions, CreativeInventoryScreenInternals {
	private CreativeInventoryScreenMixin() {
		super(null, null, null);
	}

	/**
	 * Tracks the current page.
	 * This must be "static" since the game remembers the previously selected item group after closing and reopening the screen.
	 * This matches the behaviour of "selectedTab"
	 */
	@Unique
	private static int fabric_currentPage = 0;

	@Shadow
	protected abstract void setSelectedTab(ItemGroup itemGroup);

	@Shadow
	public abstract int getSelectedTab(); /* XXX getSelectedTab XXX */

	@Override
	public int getCurrentPage() {
		return fabric_currentPage;
	}

	@Override
	public boolean nextPage() {
		// If the page offset is >= 12, we have no other page to advance to.
		if (this.fabric_getGroupOffsetFromPage(fabric_currentPage + 1) >= ItemGroup.GROUPS.length) {
			return false;
		}

		fabric_currentPage++;
		this.fabric_updateSelection();
		return true;
	}

	@Override
	public boolean previousPage() {
		if (fabric_currentPage == 0) {
			return false; // Sadly negative indices crash the game, so no going back further.
		}

		fabric_currentPage--;
		this.fabric_updateSelection();
		return true;
	}

	@Override
	public boolean isVisible(ButtonType button) {
		// If the item group count is not greater than 12, do not show.
		return FabricCreativeInventoryScreenWidgets.shouldShowButtons() && ItemGroup.GROUPS.length > 12;
	}

	@Override
	public boolean isEnabled(ButtonType button) {
		switch (button) {
			case NEXT:
				return !(fabric_getGroupOffsetFromPage(fabric_currentPage + 1) >= ItemGroup.GROUPS.length);
			case PREVIOUS:
				return fabric_currentPage != 0;
		}

		// Just return false as a fallback
		return false;
	}

	/**
	 * Gets the item group index offset.
	 *
	 * @param page the page number
	 */
	@Unique
	private int fabric_getGroupOffsetFromPage(int page) {
		switch (page) {
			case 0:
				return 0;
			case 1:
				return 12;
			default:
				return 12 + ((12 - FabricCreativeInventoryScreenWidgets.ALWAYS_VISIBLE_GROUPS.size()) * (page - 1));
		}
	}

	/**
	 * Gets the
	 * @param offset
	 *
	 * @return
	 */
	@Unique
	private int fabric_getOffsetPage(int offset) {
		if (offset < 12) {
			return 0;
		} else {
			return 1 + ((offset - 12) / (12 - FabricCreativeInventoryScreenWidgets.ALWAYS_VISIBLE_GROUPS.size()));
		}
	}

	@Unique
	private void fabric_updateSelection() {
		int minPos = this.fabric_getGroupOffsetFromPage(fabric_currentPage);
		int maxPos = this.fabric_getGroupOffsetFromPage(fabric_currentPage + 1) - 1;
		int curPos = this.getSelectedTab();

		if (curPos < minPos || curPos > maxPos) {
			this.setSelectedTab(ItemGroup.GROUPS[this.fabric_getGroupOffsetFromPage(fabric_currentPage)]);
		}
	}

	@Unique
	private boolean fabric_isGroupVisible(ItemGroup group) {
		// Always visible groups are always visible
		if (FabricCreativeInventoryScreenWidgets.ALWAYS_VISIBLE_GROUPS.contains(group)) {
			return true;
		}

		// Check if the group is on the current page
		return fabric_currentPage == this.fabric_getOffsetPage(group.getIndex());
	}

	// Actual mixin injections

	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo info) {
		// Update our selection before the buttons are added
		this.fabric_updateSelection();

		int xPos = this.x + 116;
		int yPos = this.y - 10;

		this.addButton(new FabricCreativeInventoryScreenWidgets.ItemGroupButtonWidget(this.client, this, this, ButtonType.NEXT, xPos + 11, yPos));
		this.addButton(new FabricCreativeInventoryScreenWidgets.ItemGroupButtonWidget(this.client, this, this, ButtonType.PREVIOUS, xPos, yPos));
	}

	// The injections below are to prevent every tab from rendering at the same time.
	// This is because vanilla iterates `ItemGroup.GROUPS`.
	// Any entries added to this list will render over other entries otherwise which is not intended.

	@Inject(method = "setSelectedTab", at = @At("HEAD"), cancellable = true)
	private void setSelectedTab(ItemGroup itemGroup, CallbackInfo info) {
		// Do not allow selection of pages not currently on this page
		if (!this.fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	@Inject(method = "renderTabTooltipIfHovered", at = @At("HEAD"), cancellable = true)
	private void dontRenderNonVisibleTabs(MatrixStack matrices, ItemGroup itemGroup, int mx, int my, CallbackInfoReturnable<Boolean> info) {
		// Don't render tooltip of groups that are not visible
		if (!this.fabric_isGroupVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "isClickInTab", at = @At("HEAD"), cancellable = true)
	private void isClickInTab(ItemGroup itemGroup, double mx, double my, CallbackInfoReturnable<Boolean> info) {
		// Do not handle clicking for non-visible tabs
		if (!this.fabric_isGroupVisible(itemGroup)) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "renderTabIcon", at = @At("HEAD"), cancellable = true)
	private void renderTabIcon(MatrixStack matrixStack, ItemGroup itemGroup, CallbackInfo info) {
		// Do not render tab icons for groups which are not visible.
		if (!this.fabric_isGroupVisible(itemGroup)) {
			info.cancel();
		}
	}

	// Additional features

	// Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V
	@Redirect(method = "renderTabTooltipIfHovered", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V"))
	private void onRenderItemGroupTooltip(CreativeInventoryScreen screen, MatrixStack matrices, Text text, int mouseX, int mouseY, MatrixStack ignored, ItemGroup itemGroup) {
		final ItemGroupExtensions group = (ItemGroupExtensions) itemGroup;

		// Fallback to vanilla
		if (group.fabric_getTooltipProvider() == null) {
			screen.renderTooltip(matrices, text, mouseX, mouseY);
		} else {
			final List<Text> tooltipList = new ArrayList<>();
			group.fabric_getTooltipProvider().provideTooltip(text, tooltipList);
			screen.method_30901(matrices, tooltipList, mouseX, mouseY);
		}
	}
}
