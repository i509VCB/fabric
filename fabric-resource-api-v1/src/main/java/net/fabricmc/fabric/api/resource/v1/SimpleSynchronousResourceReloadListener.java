package net.fabricmc.fabric.api.resource.v1;

import net.minecraft.resource.SynchronousResourceReloadListener;

/**
 * A simplified version of the "resource reload listener" interface, hiding the peculiarities of the API and ensuring all data is loaded on the main thread.
 *
 * <p>For a fully asynchronous alternative, consider using{@link SimpleResourceReloadListener}.
 */
public interface SimpleSynchronousResourceReloadListener extends IdentifiableResourceReloadListener, SynchronousResourceReloadListener {
}
