package net.fabricmc.fabric.api.interaction.v1.event.player;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.client.interaction.v1.event.player.ClientPlayerBreakBlockEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events related to a player breaking a block on a logical server.
 * Below is a diagram showing how the events are called, depending on whether {@link ServerPlayerBreakBlockEvents#ALLOW} returns {@code true} or {@code false}.
 *
 * <pre>{@code
 *         (false) --> CANCELLED
 * ALLOW --|
 *         (true) --> BEFORE --> AFTER
 * }</pre>
 *
 * @see ServerPlayerPlaceBlockEvents
 * @see ClientPlayerBreakBlockEvents
 */
public final class ServerPlayerBreakBlockEvents {
	/**
	 * Callback before a block is broken.
	 * Only called on the server, however updates are synced with the client.
	 *
	 * <p>If any listener cancels a block breaking action, that block breaking action is cancelled and {@link ServerPlayerBreakBlockEvents#CANCELED} event is fired.
	 * Otherwise {@link ServerPlayerBreakBlockEvents#BEFORE} event is called.
	 */
	public static final Event<Allow> ALLOW = EventFactory.createArrayBacked(Allow.class, callbacks -> (world, player, pos, state, blockEntity) -> {
		for (Allow callback : callbacks) {
			if (!callback.allowBlockBreak(world, player, pos, state, blockEntity)) {
				return false;
			}
		}

		return true;
	});

	/**
	 * Indication before a block has been broken.
	 * This event is not cancellable.
	 * To cancel a block being broken, use {@link ServerPlayerBreakBlockEvents#ALLOW}.
	 */
	public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class, callbacks -> (world, player, pos, state, blockEntity) -> {
		for (Before callback : callbacks) {
			callback.beforeBlockBreak(world, player, pos, state, blockEntity);
		}
	});

	/**
	 * Indication that a block has successfully been broken.
	 */
	public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class, callbacks -> (world, player, pos, state, blockEntity) -> {
		for (After callback : callbacks) {
			callback.afterBlockBreak(world, player, pos, state, blockEntity);
		}
	});

	/**
	 * Callback when a block break has been canceled.
	 *
	 * <p>May be used to send packets to revert client-side block changes.
	 */
	public static final Event<Canceled> CANCELED = EventFactory.createArrayBacked(Canceled.class, callbacks -> (world, player, pos, state, blockEntity) -> {
		for (Canceled callback : callbacks) {
			callback.onBlockBreakCanceled(world, player, pos, state, blockEntity);
		}
	});

	private ServerPlayerBreakBlockEvents() {
	}

	@FunctionalInterface
	public interface Allow {
		/**
		 * Checks if a block should be allowed to be broken.
		 *
		 * <p>If any listener cancels a block breaking action, that block breaking action is cancelled and {@link ServerPlayerBreakBlockEvents#CANCELED} event is fired.
		 * Otherwise, the {@link ServerPlayerBreakBlockEvents#BEFORE} event is fired.
		 *
		 * @param world the world in which the block is broken
		 * @param player the player breaking the block
		 * @param pos the position at which the block is broken
		 * @param state the block state <strong>before</strong> the block is broken
		 * @param blockEntity the block entity <strong>before</strong> the block is broken, can be {@code null}
		 * @return {@code false} to cancel block breaking action, or {@code true} to pass to {@link ServerPlayerBreakBlockEvents#BEFORE}
		 */
		boolean allowBlockBreak(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
	}

	@FunctionalInterface
	public interface Before {
		/**
		 * Called before a block is broken.
		 *
		 * @param world the world in which the block is broken
		 * @param player the player breaking the block
		 * @param pos the position at which the block is broken
		 * @param state the block state <strong>before</strong> the block is broken
		 * @param blockEntity the block entity <strong>before</strong> the block is broken, can be {@code null}
		 */
		void beforeBlockBreak(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
	}

	@FunctionalInterface
	public interface After {
		/**
		 * Called after a block is successfully broken.
		 *
		 * @param world the world where the block was broken
		 * @param player the player who broke the block
		 * @param pos the position where the block was broken
		 * @param state the block state <strong>before</strong> the block was broken
		 * @param blockEntity the block entity of the broken block, can be {@code null}
		 */
		void afterBlockBreak(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
	}

	@FunctionalInterface
	public interface Canceled {
		/**
		 * Called when a block break has been canceled.
		 *
		 * @param world the world where the block was going to be broken
		 * @param player the player who was going to break the block
		 * @param pos the position where the block was going to be broken
		 * @param state the block state of the block that was going to be broken
		 * @param blockEntity the block entity of the block that was going to be broken, can be {@code null}
		 */
		void onBlockBreakCanceled(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
	}
}
