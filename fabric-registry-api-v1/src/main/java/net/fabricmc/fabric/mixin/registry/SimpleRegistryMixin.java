package net.fabricmc.fabric.mixin.registry;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

@Mixin(SimpleRegistry.class)
abstract class SimpleRegistryMixin<T> extends RegistryMixin<T> {
	@Shadow
	@Final
	private Object2IntMap<T> field_26683;
	@Shadow
	@Final
	private BiMap<Identifier, T> entriesById;

	// Stub variable used to track whether the current entry being added is new.
	// Minecraft's Registries are inherently not thread safe, so making this thread local is overkill
	@Unique
	private boolean fabric_isAddedObjectNew = false;

	@Inject(method = "set", at = @At("HEAD"))
	private <V extends T> void handleAdditionToRegistry(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle, CallbackInfoReturnable<V> cir) {
		// This injection exists primarily to install some safety mechanisms into registries for mods and registry sync.
		// Mojang does check for these changes but will not fail; Mojang has opted to just log the changes
		//
		// The following scenarios should fail without any change to the contents of the registry:
		// - Registering an object twice in the Registry; registry entries should not share the same object instance
		// - Registering two different objects under the same Identifier; for obvious reason of replacing an entry

		// First let's validate the entry being registered is already not present in the registry
		final int indexedEntriesRawId = this.field_26683.getInt(entry);

		// The default return value of `field_26683` is `-1`.
		// If the value is 0 or greater, the object is already present in the registry
		if (indexedEntriesRawId >= 0) {
			// TODO: Is RuntimeException correct?
			throw new RuntimeException("Attempted to register object " + entry + " twice! (at raw IDs " + indexedEntriesRawId + " and " + rawId + " )");
		}

		final Identifier registryId = key.getValue();

		if (!this.entriesById.containsKey(registryId)) {
			// We are not replacing an existing entry
			this.fabric_isAddedObjectNew = true;
		} else {
			// Check for possible registry replacement
			final T oldObject = this.entriesById.get(registryId);

			// Verify the tracked entry is not a null value; don't know why you would.
			// Also verify the replacement object is not the same as the old instance.

			// FIXME: The second condition should always be true?
			// This is since we check that we fail earlier if we register an object twice?
			if (oldObject != null && oldObject != entry) {
				final int oldObjectRawId = this.field_26683.getInt(oldObject);

				// When replacing a registry entry, both the Identifier and rawId must be the same.
				// If the raw ids dont match, likely due to just calling `register` naively to replace an entry, we need to fail.
				if (oldObjectRawId != rawId) {
					throw new RuntimeException("Attempted to register ID " + registryId + " at different raw IDs (" + oldObjectRawId + ", " + rawId + ")! If you're trying to override a registry entry, use .set(), not .register()!");
				}

				// Fire the remove event
				this.getEntryRemovedEvent().invoker().onEntryRemoved(oldObjectRawId, registryId, key, oldObject);
				this.fabric_isAddedObjectNew = true;
			} else {
				// We are not replacing any registry entry
				this.fabric_isAddedObjectNew = false;
			}
		}
	}

	@Inject(method = "set", at = @At("TAIL"))
	private <V extends T> void callEntryAdded(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle, CallbackInfoReturnable<V> cir) {
		// Called after the object has been placed in the correct maps in the registry
		if (this.fabric_isAddedObjectNew) {
			this.getEntryAddedEvent().invoker().onEntryAdded(rawId, key.getValue(), key, entry);
		}
	}
}
