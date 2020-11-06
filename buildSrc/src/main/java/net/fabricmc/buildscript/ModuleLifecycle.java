package net.fabricmc.buildscript;

/**
 * Represents the lifecycle of module that is in Fabric API.
 */
public enum ModuleLifecycle {
	ACTIVE,
	/**
	 * An experimental module per <a href="https://github.com/FabricMC/fabric/issues/499">RFC</a>
	 */
	EXPERIMENTAL,
	/**
	 * A deprecated module.
	 * Deprecated modules are subject to removal and have been replaced by a newer module.
	 */
	DEPRECATED
}
