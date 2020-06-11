/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.tag.extra;

import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public final class ExtraTagNetworking {
	/**
	 * Identifier used by packets to send additional tags.
	 */
	public static final Identifier PACKET_ID = new Identifier("fabric", "extra_tags/v1");
	/**
	 * Minor version of the packet.
	 */
	public static final int MINOR_VERSION = 0;

	public static void sendPacketsToAll(ExtraTagManager manager, List<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			sendPackets(manager, player);
		}
	}

	public static void sendPackets(ExtraTagManager manager, ServerPlayerEntity player) {
		MinecraftServer server = player.getServer();

		if (server == null) {
			// We can't send it to players with no set world TODO: Debug
			System.out.println("FAIL -- " + player.toString());
			return;
		}

		sendPacket(manager.getBiomes(), player);
		sendPacket(manager.getBlockEntities(), player);
		sendPacket(manager.getEnchantments(), player);
		sendPacket(manager.getPaintingMotives(), player);
		sendPacket(manager.getParticleTypes(), player);
		sendPacket(manager.getPointsOfInterest(), player);
		sendPacket(manager.getPotions(), player);
		sendPacket(manager.getSounds(), player);
		sendPacket(manager.getStatusEffects(), player);
		sendPacket(manager.getVillagerProfessions(), player);
		sendPacket(manager.getVillagerTypes(), player);
	}

	public static void sendPacket(ExtraTagHandler<?> handler, ServerPlayerEntity player) {
		// Only send if the receiver can understand it
		if (ServerSidePacketRegistry.INSTANCE.canPlayerReceive(player, handler.getFabricId())) {
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, handler.createPacket());
		}
	}

	public static void sendPacket(Identifier identifier, Packet<?> packet, ServerPlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet);
	}

	private ExtraTagNetworking() {
	}
}
