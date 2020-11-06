package net.fabricmc.buildscript;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;

// Cannot be final
public class FabricApiModule {
	@Inject
	public FabricApiModule(ObjectFactory objects) {
		this.lifecycle = objects.property(ModuleLifecycle.class);
		this.lifecycle.set(ModuleLifecycle.ACTIVE);
	}

	private final Property<ModuleLifecycle> lifecycle;

	@Input
	public ModuleLifecycle getLifecycle() {
		return this.lifecycle.get();
	}

	public void setLifecycle(ModuleLifecycle lifecycle) {
		this.lifecycle.set(lifecycle);
	}
}
