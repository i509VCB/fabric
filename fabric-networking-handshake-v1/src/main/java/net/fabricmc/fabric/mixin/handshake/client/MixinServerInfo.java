package net.fabricmc.fabric.mixin.handshake.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;
import net.fabricmc.fabric.impl.handshake.ModifableServerInfo;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ServerInfo.class)
public class MixinServerInfo implements ModifableServerInfo {
	@Unique
	private boolean isFabricServer = false;
	@Unique
	private FabricServerMetadata meta;

	@Override
	public boolean isFabricServer() {
		return meta != null; // Vanilla will never provide this data, hence it being null.
	}

	@Override
	public FabricServerMetadata getFabricMeta() {
		return meta;
	}

	@Override
	public void setFabricMeta(FabricServerMetadata meta) {
		this.meta = meta;
	}
}
