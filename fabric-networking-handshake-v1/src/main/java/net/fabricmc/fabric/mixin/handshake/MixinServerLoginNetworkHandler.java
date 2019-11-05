package net.fabricmc.fabric.mixin.handshake;

import net.fabricmc.fabric.impl.handshake.S2CLoginQueryQueueImpl;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class MixinServerLoginNetworkHandler {

	private S2CLoginQueryQueueImpl loginQueue;

	@Inject(at = @At("HEAD"), method = "acceptPlayer()V", cancellable = true)
	private void cancelAcceptIfAwaitingResponse(CallbackInfo info) {
		if (loginQueue == null) {
			//noinspection ConstantConditions
			ServerLoginNetworkHandler self = (ServerLoginNetworkHandler) (Object) this;
			loginQueue = new S2CLoginQueryQueueImpl(self);
		}

		if (loginQueue.tick()) {
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "onQueryResponse", cancellable = true)
	public void onQueryResponse(LoginQueryResponseC2SPacket packet, CallbackInfo info) {
		if (loginQueue.receiveResponse(packet)) {
			info.cancel(); // We must cancel or else it will kick client immediately.
		}
	}
}
