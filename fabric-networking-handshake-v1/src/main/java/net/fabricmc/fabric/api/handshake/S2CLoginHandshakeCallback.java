package net.fabricmc.fabric.api.handshake;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.handshake.S2CLoginQueryQueue;

public interface S2CLoginHandshakeCallback {
	static final Event<S2CLoginHandshakeCallback> EVENT = EventFactory.createArrayBacked(
		S2CLoginHandshakeCallback.class,
		(callbacks) -> (queue) -> {
			for (S2CLoginHandshakeCallback callback : callbacks) {
				callback.accept(queue);
			}
		}
	);

	void accept(S2CLoginQueryQueue queue);
}
