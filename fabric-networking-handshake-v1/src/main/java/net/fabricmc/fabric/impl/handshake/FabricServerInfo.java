package net.fabricmc.fabric.impl.handshake;

public interface FabricServerInfo {
	boolean isFabricServer();
	
	FabricServerMetadata getFabricMeta();
}
