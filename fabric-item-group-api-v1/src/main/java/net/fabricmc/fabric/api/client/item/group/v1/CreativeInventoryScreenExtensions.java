package net.fabricmc.fabric.api.client.item.group.v1;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;

/**
 * Extensions to the builtin {@link CreativeInventoryScreen}.
 */
public interface CreativeInventoryScreenExtensions {
	static CreativeInventoryScreenExtensions getExtensions(CreativeInventoryScreen screen) {
		return ((CreativeInventoryScreenExtensions) screen);
	}

	/**
	 * Gets the current page the screen is on.
	 *
	 * @return
	 */
	int getCurrentPage();

	/**
	 * Opens the next page on this screen.
	 *
	 * @return true if the page changed.
	 */
	boolean nextPage();

	/**
	 * Opens the previous page on this screen.
	 *
	 * @return true if the page changed.
	 */
	boolean previousPage();

	/**
	 * Checks if a page button is visible.
	 *
	 * @param button the type of button
	 * @return true if the button is visible
	 */
	boolean isVisible(ButtonType button);

	/**
	 * Checks if a page button is enabled.
	 *
	 * @param button the type of button
	 * @return true if the button is enabled.
	 */
	boolean isEnabled(ButtonType button);

	/**
	 * Represents the type of button used to change pages.
	 */
	enum ButtonType {
		/**
		 * A button which moves to the next page.
		 */
		NEXT,
		/**
		 * A button which moves to the previous page.
		 */
		PREVIOUS;
	}
}
