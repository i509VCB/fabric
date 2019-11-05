package net.fabricmc.fabric.mixin.handshake;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.fabricmc.fabric.impl.handshake.FabricMetaAccess;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.fabricmc.fabric.impl.handshake.TestMixinHelper;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;

@Mixin(ServerMetadata.Deserializer.class)
public class MixinServerMetadata_Serializer {
	// We add a custom element to the Server List Ping so clients can see we are a fabric server before having to connect.
	@Inject(at = @At("RETURN"), method = "method_12691", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void deserialize(JsonElement jsonElement, Type type_1, JsonDeserializationContext jsonContext, CallbackInfoReturnable<ServerMetadata> cir, JsonObject packetData, ServerMetadata serverMetadata) {
		if(packetData.has("fabric")) {
			FabricMetaAccess fabricMeta = TestMixinHelper.cast(serverMetadata);
			fabricMeta.setFabricMetadata(jsonContext.deserialize(packetData.get("fabric"), FabricServerMetadata.class));
		}
	}

	@Inject(at = @At("RETURN"), method = "method_12692", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void serialize(ServerMetadata serverMetadata, Type type, JsonSerializationContext jsonContext, CallbackInfoReturnable<JsonElement> cir, JsonObject jsonObject) {
		FabricMetaAccess fabricMeta = TestMixinHelper.cast(serverMetadata);
		if(fabricMeta.getFabricMetadata() != null) {
			jsonObject.add("fabric", jsonContext.serialize(fabricMeta.getFabricMetadata()));
		}
	}
}
