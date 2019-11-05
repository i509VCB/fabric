package net.fabricmc.fabric.impl.handshake;

public interface ModifableServerInfo extends FabricServerInfo {
	void setFabricMeta(FabricServerMetadata meta);
}
