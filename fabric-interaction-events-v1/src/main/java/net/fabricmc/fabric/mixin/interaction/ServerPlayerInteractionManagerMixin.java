package net.fabricmc.fabric.mixin.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.interaction.v1.event.player.ServerPlayerBreakBlockEvents;
import net.fabricmc.fabric.impl.interaction.InternalEvents;

@Mixin(ServerPlayerInteractionManager.class)
abstract class ServerPlayerInteractionManagerMixin {
	@Shadow
	public ServerWorld world;
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void beginBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> info, BlockState state, BlockEntity blockEntity) {
		boolean result = ServerPlayerBreakBlockEvents.ALLOW.invoker().allowBlockBreak(this.world, this.player, pos, state, blockEntity);

		// If ALLOW == false, fire cancel and return
		if (!result) {
			ServerPlayerBreakBlockEvents.CANCELED.invoker().onBlockBreakCanceled(this.world, this.player, pos, state, blockEntity);
			info.setReturnValue(false);
			return;
		}

		// Between is for fabric-events-interaction-v0: This is an internal event and is only fired if the true ALLOW event is allowed
		if (!InternalEvents.BETWEEN_BLOCK_CANCEL_AND_BREAK.invoker().betweenBlockCancelAndBreak(this.world, this.player, pos, state, blockEntity)) {
			ServerPlayerBreakBlockEvents.CANCELED.invoker().onBlockBreakCanceled(this.world, this.player, pos, state, blockEntity);
			info.setReturnValue(false);
			return;
		}

		// All events have fired and allow the block change: Fire BEFORE and then after
		ServerPlayerBreakBlockEvents.BEFORE.invoker().beforeBlockBreak(this.world, this.player, pos, state, blockEntity);
	}

	// Only fired if ALLOW returns true
	@Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void afterBlockBroken(BlockPos pos, CallbackInfoReturnable<Boolean> info, BlockState state, BlockEntity blockEntity) {
		ServerPlayerBreakBlockEvents.AFTER.invoker().afterBlockBreak(this.world, this.player, pos, state, blockEntity);
	}
}
