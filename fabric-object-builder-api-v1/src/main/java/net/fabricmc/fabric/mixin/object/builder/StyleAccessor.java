package net.fabricmc.fabric.mixin.object.builder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

@Mixin(Style.class)
public interface StyleAccessor {
	// All methods must be prefixed or else the methods on style will be overwritten, thereby causing the default font to not work
	@Accessor("bold")
	Boolean accessor$isBold();

	@Accessor("italic")
	Boolean accessor$isItalic();

	@Accessor("underlined")
	Boolean accessor$isUnderlined();

	@Accessor("strikethrough")
	Boolean accessor$isStrikethrough();

	@Accessor("obfuscated")
	Boolean accessor$isObfuscated();

	@Accessor("font")
	Identifier accessor$getFont();
}
