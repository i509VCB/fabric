package net.fabricmc.fabric.api.object.builder.v1.text;

import net.fabricmc.fabric.mixin.object.builder.StyleAccessor;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FabricStyle extends Style {
	protected FabricStyle(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, Identifier font) {
		super(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
	}

	public static FabricStyle empty() {
		return new FabricStyle(null, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public FabricStyle withColor(TextColor color) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				color,
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withColor(Formatting color) {
		return this.withColor(color != null ? TextColor.fromFormatting(color) : null);
	}

	@Override
	public FabricStyle withBold(Boolean bold) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				bold,
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withItalic(Boolean italic) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				italic,
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withClickEvent(ClickEvent clickEvent) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				clickEvent,
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withHoverEvent(HoverEvent hoverEvent) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				hoverEvent,
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withInsertion(String insertion) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				insertion,
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withFont(Identifier font) {
		// This is client only normally, we reimplement it here on common
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				font
		);
	}

	@Override
	public FabricStyle withFormatting(Formatting formatting) {
		// TODO: This may not work?
		// Convert the resulting style into a fabric style. Easier than copying a bunch of code
		final Style style = super.withFormatting(formatting);
		final StyleAccessor accessor = (StyleAccessor) style;
		return new FabricStyle(
				style.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				style.getClickEvent(),
				style.getHoverEvent(),
				style.getInsertion(),
				accessor.accessor$getFont()
		);

	}

	@Override
	public FabricStyle withExclusiveFormatting(Formatting formatting) {
		// Cannot call super since this is normally client only. So we reimplement it here
		final StyleAccessor accessor = (StyleAccessor) this;
		TextColor textColor = this.getColor();
		Boolean bold = accessor.accessor$isBold();
		Boolean italic = accessor.accessor$isItalic();
		Boolean strikethrough = accessor.accessor$isStrikethrough();
		Boolean underlined = accessor.accessor$isUnderlined();
		Boolean obfuscated = accessor.accessor$isObfuscated();

		switch (formatting) {
		case OBFUSCATED:
			obfuscated = true;
			break;
		case BOLD:
			bold = true;
			break;
		case STRIKETHROUGH:
			strikethrough = true;
			break;
		case UNDERLINE:
			underlined = true;
			break;
		case ITALIC:
			italic = true;
			break;
		case RESET:
			return empty();
		default:
			obfuscated = false;
			bold = false;
			strikethrough = false;
			underlined = false;
			italic = false;
			textColor = TextColor.fromFormatting(formatting);
		}

		return new FabricStyle(textColor, bold, italic, underlined, strikethrough, obfuscated, this.getClickEvent(), this.getHoverEvent(), this.getInsertion(), accessor.accessor$getFont());
	}

	@Override
	public FabricStyle withFormatting(Formatting... formattings) {
		// Convert the resulting style into a fabric style. Easier than copying a bunch of code
		final Style style = super.withFormatting(formattings);
		final StyleAccessor accessor = (StyleAccessor) style;
		return new FabricStyle(
				style.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				style.getClickEvent(),
				style.getHoverEvent(),
				style.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	@Override
	public FabricStyle withParent(Style parent) {
		// Convert the resulting style into a fabric style. Easier than copying a bunch of code
		final Style style = super.withParent(parent);
		final StyleAccessor accessor = (StyleAccessor) style;
		return new FabricStyle(
				style.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				style.getClickEvent(),
				style.getHoverEvent(),
				style.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	// Fabric added methods

	public FabricStyle withUnderline(Boolean underline) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				underline,
				accessor.accessor$isStrikethrough(),
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	public FabricStyle withStrikethrough(Boolean strikethrough) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				strikethrough,
				accessor.accessor$isObfuscated(),
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}

	public FabricStyle withObfuscated(Boolean obfuscated) {
		final StyleAccessor accessor = (StyleAccessor) this;
		return new FabricStyle(
				this.getColor(),
				accessor.accessor$isBold(),
				accessor.accessor$isItalic(),
				accessor.accessor$isUnderlined(),
				accessor.accessor$isStrikethrough(),
				obfuscated,
				this.getClickEvent(),
				this.getHoverEvent(),
				this.getInsertion(),
				accessor.accessor$getFont()
		);
	}
}
