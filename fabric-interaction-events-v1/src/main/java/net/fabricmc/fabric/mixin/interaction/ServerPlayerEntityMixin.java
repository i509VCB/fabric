package net.fabricmc.fabric.mixin.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin {
	@Inject(method = "attack", at = @At("INVOKE"), cancellable = true)
	private void handleAttackEntity(Entity target, CallbackInfo ci) {
		// TODO: Implement entity attack event
	}
}
