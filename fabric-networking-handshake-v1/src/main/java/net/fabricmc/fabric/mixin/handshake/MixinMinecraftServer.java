package net.fabricmc.fabric.mixin.handshake;

import net.fabricmc.fabric.api.handshake.FabricHelloPacketHandler;
import net.fabricmc.fabric.impl.handshake.FabricMetaAccess;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Shadow
	@Final
	private ServerMetadata metadata;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V"), method = "run()V")
	public void addFabricMetadata(CallbackInfo ci) {
		FabricMetaAccess fabricData = (FabricMetaAccess) this.metadata;
		fabricData.setFabricMetadata(new FabricServerMetadata(FabricHelloPacketHandler.MAJOR_VERSION, FabricHelloPacketHandler.MINOR_VERSION, FabricHelloPacketHandler.buildModList(true)));
	}
}

