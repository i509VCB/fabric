package net.fabricmc.fabric.mixin.handshake;

import com.google.gson.Gson;
import net.minecraft.client.network.packet.QueryResponseS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(QueryResponseS2CPacket.class)
public interface QueryResponseS2CPacketAccessor {
	@Accessor
	static Gson getGSON() {
		throw new AssertionError("Mixin Dummy");
	}
}
