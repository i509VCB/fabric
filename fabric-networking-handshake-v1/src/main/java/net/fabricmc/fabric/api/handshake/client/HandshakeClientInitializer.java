package net.fabricmc.fabric.api.handshake.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.handshake.FabricHelloPacketHandler;
import net.fabricmc.fabric.impl.handshake.ClientLoginQueryResponseRegistry;
import net.fabricmc.fabric.impl.handshake.S2CLoginQueryQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class HandshakeClientInitializer implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger(HandshakeClientInitializer.class);

	@Override
	public void onInitializeClient() {
		//LOGGER.info("[fabric-networking-handshake] Initalized Client Mod");
		//ClientLoginQueryResponseRegistry.INSTANCE.register(FabricHelloPacketHandler.ID, (handler, connection, id, buffer) -> {
			//LOGGER.debug("fabric:hello received - connected to Fabric server!");
//
		//	return Optional.of(FabricHelloPacketHandler.buildHelloByteBuf());
		//});
	}

	private void onQuery(S2CLoginQueryQueue s2CLoginQueryQueue) {
	}
}
