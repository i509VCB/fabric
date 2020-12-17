package net.fabricmc.fabric.impl.interaction;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Internal event listeners for use to reimplement parts of {@code fabric-events-interaction-v0}.
 */
@ApiStatus.Internal
public final class InternalEvents {
	public static final Event<BetweenBlockCancelAndBreak> BETWEEN_BLOCK_CANCEL_AND_BREAK = EventFactory.createArrayBacked(BetweenBlockCancelAndBreak.class, callbacks -> (world, player, pos, state, blockEntity) -> {
		for (BetweenBlockCancelAndBreak callback : callbacks) {
			if (!callback.betweenBlockCancelAndBreak(world, player, pos, state, blockEntity)) {
				return false;
			}
		}

		return true;
	});

	private InternalEvents() {
	}

	@FunctionalInterface
	public interface BetweenBlockCancelAndBreak {
		boolean betweenBlockCancelAndBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
	}
}
