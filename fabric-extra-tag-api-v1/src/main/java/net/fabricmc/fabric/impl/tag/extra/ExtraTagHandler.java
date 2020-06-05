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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.netty.buffer.Unpooled;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

public final class ExtraTagHandler<T> implements IdentifiableResourceReloadListener {
	private final RegistryTagContainer<T> container;
	private final Consumer<RegistryTagContainer<T>> applicationConsumer;
	private final Identifier id;

	public ExtraTagHandler(Identifier id, Supplier<RegistryTagContainer<T>> factory, Consumer<RegistryTagContainer<T>> applicationConsumer) {
		this.id = id;
		this.container = factory.get();
		this.applicationConsumer = applicationConsumer;
	}

	@Override
	public Identifier getFabricId() {
		return this.id;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		final CompletableFuture<Map<Identifier, Tag.Builder>> builderFuture = this.container.prepareReload(manager, prepareExecutor);
		synchronizer.getClass();

		return builderFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(this.container::applyReload, applyExecutor);
	}

	public void toPacket(PacketByteBuf buf) {
		this.container.toPacket(buf);
	}

	public Packet<?> createPacket() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		this.toPacket(buf);

		return ServerSidePacketRegistry.INSTANCE.toPacket(this.id, buf);
	}

	public void apply() {
		this.applicationConsumer.accept(this.container);
	}

	public static <T> ExtraTagHandler<T> fromPacket(Identifier id, Supplier<RegistryTagContainer<T>> factory, Consumer<RegistryTagContainer<T>> applicationConsumer, PacketByteBuf buf) {
		final ExtraTagHandler<T> tagManager = new ExtraTagHandler<>(id, factory, applicationConsumer);
		tagManager.container.fromPacket(buf);

		return tagManager;
	}
}
