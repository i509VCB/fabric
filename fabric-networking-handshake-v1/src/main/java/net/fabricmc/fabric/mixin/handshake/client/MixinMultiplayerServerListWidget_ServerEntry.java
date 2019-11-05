package net.fabricmc.fabric.mixin.handshake.client;

import net.fabricmc.fabric.api.handshake.FabricHelloPacketHandler;
import net.fabricmc.fabric.impl.handshake.FabricServerInfo;
import net.fabricmc.fabric.impl.handshake.TestMixinHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class MixinMultiplayerServerListWidget_ServerEntry {
	@Shadow
	private ServerInfo server;

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	protected abstract void draw(int int_1, int int_2, Identifier identifier_1);

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;touchscreen:Z"), method = "render(IIIIIIIZF)V")
	private void renderFabricLogo(int int_1, int int_2, int xWidth, int int_4, int int_5, int cursorX, int cursorZ, boolean boolean_1, float float_1, CallbackInfo ci) {
		FabricServerInfo fabricInfo = TestMixinHelper.cast(this.server);

		if(fabricInfo.isFabricServer()) {
			if((fabricInfo.getFabricMeta().getMajorVersion() == FabricHelloPacketHandler.MAJOR_VERSION) && fabricInfo.getFabricMeta().getMinorVersion() == FabricHelloPacketHandler.MINOR_VERSION) {
				draw(xWidth+260, int_2+6, new Identifier("fabric-networking-handshake-v1", "textures/gui/fabricserverlogo.png")); // TODO how to shrink logo down
				return;
			}
			// Mismatched version if your manage to get here.
		}
	}

}
