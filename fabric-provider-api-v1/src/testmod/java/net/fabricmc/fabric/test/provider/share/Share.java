package net.fabricmc.fabric.test.provider.share;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public final class Share extends PersistentState {
	Share(ServerWorld world) {
		super("share-" + world.getRegistryKey().getValue().toString());
	}

	@Override
	public void fromTag(CompoundTag tag) {
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return tag;
	}
}
