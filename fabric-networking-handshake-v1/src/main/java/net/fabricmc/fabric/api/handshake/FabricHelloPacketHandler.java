package net.fabricmc.fabric.api.handshake;

import io.netty.buffer.Unpooled;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.fabricmc.loader.util.version.VersionPredicateParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.listener.ClientQueryPacketListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.HashMap;
import java.util.Map;

public class  FabricHelloPacketHandler {
	public static final Identifier ID = new Identifier("fabric", "hello");

	// Version properties, these should only change when the standard changes.
	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 0;

	// Custom Value constants.
	private static final String REQUIRES_SERVER = "fabric:clientRequiresServer";
	private static final String REQUIRES_CLIENT = "fabric:serverRequiresClient";
	private static final String REQUIRES_VERSION = "fabric:requiresVersion";

	// These are here so we don't have to recalculate mod metadata every single time.
	private static CompoundTag cachedClient;
	private static CompoundTag cachedServer;

	public static PacketByteBuf buildHelloByteBuf(boolean isServer) {
		CompoundTag tag = new CompoundTag();
		tag.putInt("majorVersion", MAJOR_VERSION);
		tag.putInt("minorVersion", MINOR_VERSION);

		tag.put("mods", buildModList(isServer));
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeCompoundTag(tag);
		return buf;
	}

	public static CompoundTag buildModList(boolean isServer) {
		if (isServer) {
			return buildModListServer();
		}

		return buildModListClient();
	}

	private static CompoundTag buildModListClient() {
		if(cachedClient != null) { // We cache these after the first grab to save cpu.
			return cachedClient;
		}

		cachedClient = new CompoundTag();

		for(ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata meta = container.getMetadata();

			if(meta.containsCustomValue(FabricHelloPacketHandler.REQUIRES_SERVER)) {
				CustomValue value = meta.getCustomValue(FabricHelloPacketHandler.REQUIRES_SERVER);

				if (value.getType() == CustomValue.CvType.BOOLEAN) {
					if(value.getAsBoolean()) {
						cachedClient.put(meta.getId(), handleVersionTag(meta));
					}
				}
			}
		}

		return cachedClient;
	}

	private static CompoundTag buildModListServer() {
		if(cachedServer != null) { // We cache these after the first grab to save cpu.
			return cachedServer;
		}

		cachedServer = new CompoundTag();

		for(ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata meta = container.getMetadata();

			if(meta.containsCustomValue(FabricHelloPacketHandler.REQUIRES_CLIENT)) {
				CustomValue value = meta.getCustomValue(FabricHelloPacketHandler.REQUIRES_CLIENT);

				if (value.getType() == CustomValue.CvType.BOOLEAN) {
					if(value.getAsBoolean()) {
						cachedServer.put(meta.getId(), handleVersionTag(meta));
					}
				}
			}
		}

		return cachedServer;
	}

	private static CompoundTag handleVersionTag(ModMetadata meta) {
		CompoundTag tag = new CompoundTag();
		tag.putString("version", meta.getVersion().getFriendlyString());

		if(meta.containsCustomValue(FabricHelloPacketHandler.REQUIRES_VERSION)) {
			CustomValue versionValue = meta.getCustomValue(FabricHelloPacketHandler.REQUIRES_VERSION);

			if(versionValue.getType() == CustomValue.CvType.STRING) {
				String versionTest = versionValue.getAsString();
				tag.putString("acceptableVersions", versionTest);
			}
		}
		return tag;
	}

	public class DepEntry implements Map.Entry<String, String> {
		private String value;
		private String key;

		DepEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String getKey() {
			return this.key;
		}

		@Override
		public String getValue() {
			return this.value;
		}

		@Override
		public String setValue(String value) {
			return this.value = value;
		}
	}
}
