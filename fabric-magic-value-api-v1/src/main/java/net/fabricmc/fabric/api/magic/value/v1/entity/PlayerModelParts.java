package net.fabricmc.fabric.api.magic.value.v1.entity;

import net.fabricmc.api.EnvType;

import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;

/**
 * An enumeration of all vanilla values related to a the enabled model parts on a {@link PlayerEntity player}.
 *
 * <p>For use on a {@link EnvType#CLIENT client environment}, {@link PlayerModelPart} is available.
 */
public final class PlayerModelParts {
	public static final int CAPE = 1;
	public static final int JACKET = 2;
	public static final int LEFT_SLEEVE = 4;
	public static final int RIGHT_SLEEVE = 8;
	public static final int LEFT_PANTS_LEG = 16;
	public static final int RIGHT_PANTS_LEG = 32;
	public static final int HAT = 64;

	private PlayerModelParts() {
	}
}
