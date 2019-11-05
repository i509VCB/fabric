package net.fabricmc.fabric.mixin.handshake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.minecraft.client.network.packet.QueryResponseS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.LowercaseEnumTypeAdapterFactory;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(QueryResponseS2CPacket.class)
public class MixinQueryResponseS2CPacket {
	@Shadow
	@Final
	@Mutable
	private static Gson GSON;

	static {
		GSON = (new GsonBuilder()) // We add our custom Serializer to GSON instance.
			.registerTypeAdapter(ServerMetadata.Version.class, new ServerMetadata.Version.Serializer())
			.registerTypeAdapter(ServerMetadata.Players.class, new ServerMetadata.Players.Deserializer())
			.registerTypeAdapter(ServerMetadata.class, new net.minecraft.server.ServerMetadata.Deserializer())
			.registerTypeAdapter(FabricServerMetadata.class, new FabricServerMetadata.Serializer())
			.registerTypeHierarchyAdapter(Text.class, new net.minecraft.text.Text.Serializer())
			.registerTypeHierarchyAdapter(Style.class, new net.minecraft.text.Style.Serializer())
			.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
			.create();
	}
}
