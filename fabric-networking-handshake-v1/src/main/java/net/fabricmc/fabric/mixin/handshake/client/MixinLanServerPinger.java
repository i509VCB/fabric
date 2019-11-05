package net.fabricmc.fabric.mixin.handshake.client;

import net.fabricmc.fabric.api.handshake.FabricHelloPacketHandler;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.fabricmc.fabric.impl.handshake.TestMixinHelper;
import net.minecraft.server.LanServerPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LanServerPinger.class)
public class MixinLanServerPinger {
	@Inject(at = @At("HEAD"), method = "parseAnnouncementMotd")
	private static void parseAnnouncementMotdDetail(String message, CallbackInfoReturnable<String> cir) {
		System.out.println("Lan Game: ");
		System.out.println(message);
	}

	@Inject(at = @At("TAIL"), method = "createAnnouncement", cancellable = true)
	private static void createAnnouncement_addFabricData(String string_1, String string_2, CallbackInfoReturnable<String> cir) {
		String main = cir.getReturnValue();
		StringBuilder builder = new StringBuilder(main);
		builder.append("[FABRIC_MOD_DATA]");
		TestMixinHelper.addData(builder, new FabricServerMetadata(FabricHelloPacketHandler.MAJOR_VERSION, FabricHelloPacketHandler.MINOR_VERSION, FabricHelloPacketHandler.buildModList(true)));
		builder.append("[/FABRIC_MOD_DATA]");
		cir.setReturnValue(builder.toString());
	}
}
