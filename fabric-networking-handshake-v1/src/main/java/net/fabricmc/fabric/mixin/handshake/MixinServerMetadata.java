package net.fabricmc.fabric.mixin.handshake;

import net.fabricmc.fabric.impl.handshake.FabricMetaAccess;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerMetadata.class)
public class MixinServerMetadata implements FabricMetaAccess {
	@Unique
	private FabricServerMetadata fabricData;

	@Override
	public FabricServerMetadata getFabricMetadata() {
		return this.fabricData;
	}

	@Override
	public void setFabricMetadata(FabricServerMetadata metadata) {
		this.fabricData = metadata;
	}
}
