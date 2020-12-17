package net.fabricmc.fabric.impl.event.interaction;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.interaction.v1.event.player.ServerPlayerBreakBlockEvents;
import net.fabricmc.fabric.impl.interaction.InternalEvents;

public final class LegacyInteractionEventsRouter implements ModInitializer {
	@Override
	public void onInitialize() {
		// Must be lambdas in order to allow for proper invalidation of events to work

		// Cannot use ServerPlayerBreakBlockEvents.ALLOW or BEFORE since old event mixes notification and cancellation.
		// This internal event is between to allow cancellation without affecting world before BEFORE
		InternalEvents.BETWEEN_BLOCK_CANCEL_AND_BREAK.register((world, player, pos, state, blockEntity) -> {
			return PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, pos, state, blockEntity);
		});

		ServerPlayerBreakBlockEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(world, player, pos, state, blockEntity);
		});

		ServerPlayerBreakBlockEvents.CANCELED.register((world, player, pos, state, blockEntity) -> {
			PlayerBlockBreakEvents.CANCELED.invoker().onBlockBreakCanceled(world, player, pos, state, blockEntity);
		});
	}
}
