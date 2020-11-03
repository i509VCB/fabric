package net.fabricmc.fabric.api.chat.v1;

import java.util.Optional;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ServerChatEvents {
	public static final Event<Allow> ALLOW = EventFactory.createArrayBacked(Allow.class, callbacks -> (player, message) -> {
		for (Allow callback : callbacks) {
			if (!callback.allowChatMessage(player, message)) {
				return false;
			}
		}

		return true;
	});

	public static final Event<Modify> MODIFY = EventFactory.createArrayBacked(Modify.class, callbacks -> (player, message) -> {
		for (Modify callback : callbacks) {
			final Optional<Text> modified = callback.modifyChatMessage(player, message);

			if (modified.isPresent()) {
				return modified;
			}
		}

		return Optional.empty();
	});

	public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class, callbacks -> (player, text) -> {
		for (Before callback : callbacks) {
			callback.beforeChatMessage(player, text);
		}
	});

	public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class, callbacks -> (player, text) -> {
		for (After callback : callbacks) {
			callback.afterChatMessage(player, text);
		}
	});

	@FunctionalInterface
	public interface Allow {
		boolean allowChatMessage(ServerPlayerEntity player, String message);
	}

	@FunctionalInterface
	public interface Modify {
		Optional<Text> modifyChatMessage(ServerPlayerEntity player, String message);
	}

	@FunctionalInterface
	public interface Before {
		void beforeChatMessage(ServerPlayerEntity player, Text text);
	}

	@FunctionalInterface
	public interface After {
		void afterChatMessage(ServerPlayerEntity player, Text text);
	}

	private ServerChatEvents() {
	}
}
