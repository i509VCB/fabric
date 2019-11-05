package net.fabricmc.fabric.impl.handshake;

import net.fabricmc.fabric.mixin.handshake.LoginQueryRequestS2CPacketAccessor;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.packet.LoginQueryRequestS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientLoginQueryResponseRegistry {
	public static final ClientLoginQueryResponseRegistry INSTANCE = new ClientLoginQueryResponseRegistry();
	protected static final Logger LOGGER = LogManager.getLogger();
	private final Map<Identifier, ClientLoginQueryResponder> responderMap = new HashMap<>();

	protected ClientLoginQueryResponseRegistry() {

	}

	public void register(Identifier id, ClientLoginQueryResponder responder) {
		if (responderMap.containsKey(id)) {
			LOGGER.warn("Registered duplicate client-side login query responder " + id + "!");
			LOGGER.trace(new Throwable());
		}

		responderMap.put(id, responder);
	}

	public Optional<LoginQueryResponseC2SPacket> respond(ClientLoginNetworkHandler handler, ClientConnection connection, LoginQueryRequestS2CPacket packet) {
		LoginQueryRequestS2CPacketAccessor packetAccessor = (LoginQueryRequestS2CPacketAccessor) packet;
		ClientLoginQueryResponder responder = responderMap.get(packetAccessor.getChannel());
		if (responder != null) {
			Optional<PacketByteBuf> buf = responder.respond(handler, connection, packetAccessor.getChannel(), packetAccessor.getPayload());
			if (buf.isPresent()) {
				LoginQueryResponseC2SPacket response = new LoginQueryResponseC2SPacket(packetAccessor.getQueryId(), buf.get());
				return Optional.of(response);
			}
		}

		return Optional.empty();
	}
}
