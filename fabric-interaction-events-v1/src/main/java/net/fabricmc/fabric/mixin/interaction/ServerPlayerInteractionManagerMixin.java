package net.fabricmc.fabric.mixin.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.interaction.v1.event.player.ServerPlayerBreakBlockEvents;
import net.fabricmc.fabric.impl.interaction.InternalEvents;

@Mixin(ServerPlayerInteractionManager.class)
abstract class ServerPlayerInteractionManagerMixin {
	@Shadow
	public ServerWorld world;
	@Shadow
	public ServerPlayerEntity player;

	// Block break

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

	// Block place
	@Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
	private void beforeBlockPlace(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info) {
		final Item item = stack.getItem();

		if (item instanceof BlockItem) {
			final ItemUsageContext usageContext = new ItemUsageContext(player, hand, blockHitResult);
			final ItemPlacementContext placementContext = new ItemPlacementContext(usageContext);
			final BlockState futureState = ((BlockItem) item).getBlock().getPlacementState(placementContext);

			// TODO: Implement block break events
		}
	}

	// Attack block
	@Inject(method = "processBlockBreakingAction", at = @At("HEAD"), cancellable = true)
	private void startAttackingBlock(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo info) {
		// Only handle events where player attacks a block
		if (action != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
			return;
		}

		// TODO: Implement attack block events
	}

	// Use block
	@Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
	private void startUsingBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> info) {
		// TODO: Implement attack use events (might be interact for name)?
	}

	// Use item
	@Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
	private void startUsingItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		// TODO: Implement item use events
	}
}
