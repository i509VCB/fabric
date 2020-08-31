package net.fabricmc.fabric.api.resource.v1;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;

/**
 * Interface for "identifiable" resource reload listeners.
 *
 * <p>"Identifiable" listeners have an unique identifier, which can be depended on,
 * and can provide dependencies that they would like to see executed before
 * themselves.
 *
 * @see ResourceReloadListenerKeys
 */
public interface IdentifiableResourceReloadListener extends ResourceReloadListener {
	/**
	 * @return The unique identifier of this listener.
	 */
	Identifier getFabricId();

	/**
	 * @return The identifiers of listeners this listener expects to have been
	 * executed before itself. Please keep in mind that this only takes effect
	 * during the application stage!
	 */
	default Collection<Identifier> getFabricDependencies() {
		return Collections.emptyList();
	}
}
