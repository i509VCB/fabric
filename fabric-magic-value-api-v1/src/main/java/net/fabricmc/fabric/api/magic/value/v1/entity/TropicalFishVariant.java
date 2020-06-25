package net.fabricmc.fabric.api.magic.value.v1.entity;

import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.DyeColor;

/**
 * An enumeration of all vanilla values related to a the appearance and shape of {@link TropicalFishEntity tropical fish}.
 *
 * <p>These values can be combined using bitwise operators to define the appearance of a tropical fish like below.
 * <p><blockquote><pre>
 * int variant = TropicalFishVariant.SPOTTY | TropicalFishVariant.baseColor(DyeColor.PURPLE) | TropicalFishVariant.patternColor(DyeColor.PINK);
 * tropicalFish.setVariant(variant)
 * </pre></blockquote>
 */
public final class TropicalFishVariant {
	// Sizes

	/**
	 * Specifies a tropical fish should be small.
	 *
	 * <p>Note changing the size of a tropical fish will also change it's shape.
	 */
	public static final int SMALL = 0;
	/**
	 * Specifies a tropical fish should be large.
	 *
	 * <p>Note changing the size of a tropical fish will also change it's shape.
	 */
	public static final int LARGE = 1;

	// Small Fish

	public static final int KOB = 0;

	public static final int SUNSTREAK = 256;

	public static final int SNOOPER = 512;

	public static final int DASHER = 768;

	public static final int BRINELY = 1024;

	public static final int SPOTTY = 1280;

	// Big Fish

	public static final int FLOPPER = 1;

	public static final int STRIPEY = 257;

	public static final int GLITTER = 513;

	public static final int BLOCKFISH = 769;

	public static final int BETTA = 1025;

	public static final int CLAYFISH = 1281;

	public static int baseColor(DyeColor color) {
		return color.getId() & 255 << 16;
	}

	public static DyeColor getBaseColor(int variant) {
		return DyeColor.byId((variant & 16711680) >> 16);
	}

	public static int patternColor(DyeColor color) {
		return color.getId() & 255 << 24;
	}

	public static DyeColor getPatternColor(int variant) {
		return DyeColor.byId((variant & -16777216) >> 24);
	}

	private TropicalFishVariant() {
	}
}
