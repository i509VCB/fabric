package net.fabricmc.fabric.impl.client.screen;

import java.util.AbstractList;
import java.util.List;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

/**
 * A list of child elements a screen contains. Does not allow removal of buttons.
 */
public final class ElementList<T extends Element> extends AbstractList<T> {
	private final List<T> children;

	public ElementList(List<T> children) {
		this.children = children;
	}

	@Override
	public T get(int index) {
		return this.children.get(index);
	}

	@Override
	public T set(int index, T element) {
		this.rangeCheck(index); // verify index bounds

		final T currentValue = this.children.get(index);

		// Verify that the element being removed is not a button
		this.validateNotButton(currentValue);

		// Verify that the element being added is not a button.
		this.validateNotButton(element);
		this.remove(element); // verify / ensure no duplicates

		return this.children.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		this.rangeCheckForAdd(index); // verify index bounds

		// Verify element being remove is not a button
		this.validateNotButton(element);
		this.remove(element); // ensure no duplicates

		this.children.add(index, element);
	}

	@Override
	public T remove(int index) {
		this.rangeCheck(index); // verify index bounds
		final T currentValue = this.children.get(index);

		// Verify that the element being removed is not a button
		this.validateNotButton(currentValue);

		return this.children.remove(index);
	}

	@Override
	public int size() {
		return this.children.size();
	}

	private void rangeCheck(int index) {
		if (index >= this.size()) {
			throw createOutOfBoundsException(index);
		}
	}

	private void rangeCheckForAdd(int index) {
		if (index > this.size() || index < 0) {
			throw createOutOfBoundsException(index);
		}
	}

	private void validateNotButton(Element element) {
		if (element instanceof AbstractButtonWidget) {
			throw new UnsupportedOperationException("Cannot add/remove buttons using \"getChildElements\". Please use \"getButtons\" instead.");
		}
	}

	private IndexOutOfBoundsException createOutOfBoundsException(int index) {
		return new IndexOutOfBoundsException("Index: " + index + ", Size: "+ this.size());
	}
}
