package net.fabricmc.fabric.impl.handshake;

import net.fabricmc.fabric.impl.handshake.FabricServerMetadata;

public interface FabricMetaAccess {
	FabricServerMetadata getFabricMetadata();

	void setFabricMetadata(FabricServerMetadata metadata);
}
