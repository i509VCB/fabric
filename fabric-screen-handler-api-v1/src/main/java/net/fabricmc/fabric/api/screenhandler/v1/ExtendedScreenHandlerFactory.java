/*
 * Copyright (c) 2018, 2020 FabricMC
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

package net.fabricmc.fabric.api.screenhandler.v1;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * An extension of {@code NamedScreenHandlerFactory} that can write additional data to a screen opening packet.
 *
 * @see ScreenHandlerRegistry#registerExtended(net.minecraft.util.Identifier, ScreenHandlerRegistry.ExtendedClientHandlerFactory)
 */
public interface ExtendedScreenHandlerFactory extends NamedScreenHandlerFactory {
	/**
	 * Writes additional server -&gt; client screen opening data to the buffer.
	 *
	 * @param player the player that is opening the screen
	 * @param buf    the packet buffer
	 */
	void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf);
}
