package net.fabricmc.fabric.mixin.registry.tag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.registry.Registry;

@Mixin(RegistryTagContainer.class)
public interface RegistryTagContainerAccessor<T> {
	@Accessor
	Registry<T> getRegistry();
}
