package net.fabricmc.fabric.mixin.handshake;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import net.minecraft.util.PacketByteBuf;

@Mixin(LoginQueryResponseC2SPacket.class)
public interface LoginQueryResponseC2SPacketAccessor {
	@Accessor
	int getQueryId();
	@Accessor
	PacketByteBuf getResponse();
}
