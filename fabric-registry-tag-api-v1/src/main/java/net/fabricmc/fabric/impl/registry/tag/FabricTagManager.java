package net.fabricmc.fabric.impl.registry.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.mixin.registry.tag.RegistryTagContainerAccessor;

import net.minecraft.class_5318;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public final class FabricTagManager implements IdentifiableResourceReloadListener {
	private final List<Registry<?>> syncedRegistries;
	private final List<Registry<?>> unsyncedRegistries;

	FabricTagManager() {
		this.syncedRegistries = Collections.unmodifiableList(getSyncedRegistries());
		this.unsyncedRegistries = Collections.unmodifiableList(getUnsyncedRegistries());
	}

	public static List<Registry<?>> getSyncedRegistries() {
		final List<Registry<?>> registries = new ArrayList<>();
		// Supported already synced registries for tags v1.0
		registries.add(Registry.BIOME); // Synced ID
		registries.add(Registry.PAINTING_MOTIVE); // Synced ID
		registries.add(Registry.PARTICLE_TYPE); // Synced ID
		registries.add(Registry.SOUND_EVENT); // Synced ID
		registries.add(Registry.STATUS_EFFECT); // Synced ID
		registries.add(Registry.VILLAGER_PROFESSION); // Synced ID
		registries.add(Registry.VILLAGER_TYPE); // Synced ID
		registries.add(Registry.ENCHANTMENT); // Synced ID

		return registries;
	}

	public static List<Registry<?>> getUnsyncedRegistries() {
		// Supported but unsynced registries for v1.0
		final List<Registry<?>> registries = new ArrayList<>();

		// Special cases
		registries.add(Registry.POTION); // Not synced by ID, register to sync ids
		registries.add(Registry.BLOCK_ENTITY_TYPE); // Not synced by ID, register to sync ids
		registries.add(Registry.POINT_OF_INTEREST_TYPE); // TODO: Not synced by ID, not even sent to clients

		return registries;
	}

	@Override
	public Identifier getFabricId() {
		return ExtraTagInternals.PACKET_ID;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return null;
	}

	/**
	 * Packet description (v1.0):
	 *
	 * <p><ul>
	 * <li>Integer / Minor version of the packet
	 * <li>Boolean / Whether another registry entry exists
	 * <ul>
	 *     <li>Identifier / The Identifier of the registry
	 *     <li>RegistryTagContainer / as described by {@link net.minecraft.tag.RegistryTagContainer#toPacket(PacketByteBuf)}
	 * </ul>
	 * <li>Boolean / false - end of section
	 * <li>Boolean / Whether this packet contains dimension type tag data
	 *     <ul><li> RegistryTagContainer / as described by {@link net.minecraft.tag.RegistryTagContainer#toPacket(PacketByteBuf)} </ul>
	 * <li>Boolean / false - End of Packet
	 * </ul>
	 */
	public void toPacket(PacketByteBuf buf) {
		buf.writeInt(ExtraTagInternals.MINOR_VERSION);

		for (Registry<?> registry : this.syncedRegistries) {
			final Identifier id = ((Registry) Registry.REGISTRIES).getId(registry);

			if (id == null) {
				continue; // Do not count registries that are not registered.
			}

			// Say we have an entry
			buf.writeBoolean(true);
			buf.writeIdentifier(id);

			final RegistryTagContainer<?> registryTagContainer = ExtraTagInternals.getRegistryTagContainer(registry);
			// And write the tags
			registryTagContainer.toPacket(buf);
		}

		buf.writeBoolean(false);

		final RegistryTagContainer<DimensionType> dimensionsTagContainer = ExtraTagInternals.getDimensionTypeContainer();
		final RegistryTagContainerAccessor<DimensionType> accessor = (RegistryTagContainerAccessor<DimensionType>) ExtraTagInternals.getDimensionTypeContainer();

		// If the registry is empty, i.e. Not Initialized, do not include the dimension info
		if (!(accessor.getRegistry() instanceof EmptyRegistry)) {
			// Say we have dimensions
			buf.writeBoolean(true);
			// And write the tags
			dimensionsTagContainer.toPacket(buf);
		}

		// End of Packet
		buf.writeBoolean(false);
	}

	public static FabricTagManager fromPacket(class_5318 dimensionTracker, PacketByteBuf buf) {
		final int minorVersion = buf.readInt();

		final FabricTagManager fabricTagManager = new FabricTagManager();

		return fabricTagManager;
	}

	private class Holder {

	}
}
