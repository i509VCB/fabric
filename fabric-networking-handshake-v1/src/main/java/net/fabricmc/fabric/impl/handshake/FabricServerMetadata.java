package net.fabricmc.fabric.impl.handshake;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.mixin.handshake.QueryResponseS2CPacketAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.Tag;
import net.minecraft.util.JsonHelper;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

public class FabricServerMetadata {
	private final int majorVersion;
	private final int minorVersion;
	private final CompoundTag requiredModList;

	public FabricServerMetadata(int majorVersion, int minorVersion, CompoundTag requiredModList) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.requiredModList = requiredModList;
	}

	public int getMajorVersion() {
		return this.majorVersion;
	}

	public int getMinorVersion() {
		return this.minorVersion;
	}

	public CompoundTag getRequiredModList() {
		return this.requiredModList;
	}

	public static class Serializer implements JsonDeserializer<FabricServerMetadata>, JsonSerializer<FabricServerMetadata>  {
		@Override
		public FabricServerMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = JsonHelper.asObject(json, "fabric");

			if(!object.has("majorVersion") || !object.has("minorVersion") || !object.has("requiredModList")) {
				return null;
			}

			CompoundTag tag;
			try {
				tag = new StringNbtReader(new StringReader(object.get("requiredModList").getAsString())).parseCompoundTag();
			} catch (CommandSyntaxException e) {
				System.err.println("Failed to read modTag from FabricServerMetadata");
				return null;
			}

			return new FabricServerMetadata(object.get("majorVersion").getAsInt(), object.get("minorVersion").getAsInt(), tag);
		}

		@Override
		public JsonElement serialize(FabricServerMetadata src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject object = new JsonObject();
			object.addProperty("majorVersion", src.majorVersion);
			object.addProperty("minorVersion", src.minorVersion);
			object.addProperty("requiredModList", src.requiredModList.toString());
			return object;
		}
	}
}
