package net.fabricmc.fabric.mixin.handshake.client;

import net.fabricmc.fabric.impl.handshake.FabricMetaAccess;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.fabricmc.fabric.impl.handshake.ModifableServerInfo;
import net.fabricmc.fabric.impl.handshake.TestMixinHelper;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.packet.QueryResponseS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/network/MultiplayerServerListPinger$1")
public class MixinMultiplayerServerListPinger_Inner {
	@Shadow
	ServerInfo field_3776; // serverInfo_1 from outside of class

	@Inject(at = @At("HEAD"), method = "onResponse(Lnet/minecraft/client/network/packet/QueryResponseS2CPacket;)V")
	public void testResponseCheck(QueryResponseS2CPacket packet, CallbackInfo ci) {
		TestMixinHelper.readResponse(packet); // TODO Remove testing

		FabricMetaAccess access = TestMixinHelper.cast(packet.getServerMetadata());

		FabricServerMetadata fServerMeta = access.getFabricMetadata();

		ModifableServerInfo fabricServerInfo = TestMixinHelper.cast(this.field_3776);
		fabricServerInfo.setFabricMeta(fServerMeta);
	}


}
