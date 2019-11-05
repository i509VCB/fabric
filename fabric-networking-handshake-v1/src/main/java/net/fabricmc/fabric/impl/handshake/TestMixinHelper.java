package net.fabricmc.fabric.impl.handshake;

import net.fabricmc.fabric.mixin.handshake.QueryResponseS2CPacketAccessor;
import net.minecraft.client.network.packet.QueryResponseS2CPacket;
import net.minecraft.server.ServerMetadata;

public class TestMixinHelper {
	public static void readResponse(QueryResponseS2CPacket packet) {
		System.out.println(QueryResponseS2CPacketAccessor.getGSON().toJson(packet.getServerMetadata()));
	}

	public static void addData(StringBuilder builder, FabricServerMetadata meta) {
		builder.append(QueryResponseS2CPacketAccessor.getGSON().toJson(meta));
	}

	// TODO When Mixin 0.8 comes out, get rid of this hack.
	public static <T> T cast(Object o) {
		return (T) o;
	}
}
