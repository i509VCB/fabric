package net.fabricmc.fabric.test.provider.share;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.google.common.collect.Iterables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.provider.v1.ApiLookupMap;
import net.fabricmc.fabric.api.provider.v1.ApiProviderMap;

/**
 * A testmod for validating custom api provider registries work properly.
 * This is an automated test which will throw an assertion error if the test fails.
 *
 * <p>This test adds a new storage volume called {@code the store}.
 * The store is per server world and may contain one instance of an api.
 */
public final class TheShareTest implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ApiLookupMap<LookupImpl<?, ?>> PROVIDERS = ApiLookupMap.create(LookupImpl::new);
	// Some apis to test
	private static final ShareApiLookup<BooleanSupplier, Void> SIMPLE_BOOL = getLookup(
			new Identifier("fabric-provider-api-v1-testmod", "bool_supplier"),
			BooleanSupplier.class,
			Void.class
	);
	private static final ShareApiLookup<DoubleSupplier, Void> SIMPLE_DOUBLE = getLookup(
			new Identifier("fabric-provider-api-v1-testmod", "double_supplier"),
			DoubleSupplier.class,
			Void.class
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting the share test for provider api");
		// Impl cast is fine since this is a test
		final int lookupCount = Iterables.size(PROVIDERS);

		if (lookupCount != 2) {
			throw new AssertionError(String.format("Test failed: Exactly 2 test lookups were not in the share lookup providers! Found %s", lookupCount));
		}

		// The providers are registered: get the show on the road
		SIMPLE_BOOL.setProvider((world, context) -> () -> {
			return world.getRegistryKey() == World.END;
		});
		SIMPLE_DOUBLE.setProvider((world, context) -> () -> {
			return world.getRegistryKey() == World.OVERWORLD ? 0.509D : -0.509D;
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// This test is run on the overworld and the end.
			// Fail if the worlds don't exist
			failIfFalse(server.getWorld(World.OVERWORLD) == null, "Test failed: Overworld was not found");
			failIfFalse(server.getWorld(World.END) == null, "Test failed: The end was not found");

			// Fail if the share has not be bound to persistent state manager
			final Share overworldShare = server.getWorld(World.OVERWORLD).getPersistentStateManager().get(() -> {
				throw new AssertionError("Overworld: This should not be thrown!");
			}, "fabric-provider-api-v1:theshare");
			failIfFalse(overworldShare != null, "Test failed: Could not get share from overworld");

			final Share endShare = server.getWorld(World.END).getPersistentStateManager().<Share>get(() -> {
				throw new AssertionError("The End: This should not be thrown!");
			}, "fabric-provider-api-v1:theshare");
			failIfFalse(endShare != null, "Test failed: Could not get share from the end");

			// Shares are present: Let's get the show on the road
		});

		// Bind the share to a persistent state
		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD || world.getRegistryKey() == World.END) {
				world.getPersistentStateManager().getOrCreate(() -> {
					return new Share(world);
				}, "fabric-provider-api-v1:theshare");
			}
		});
	}

	private static <T, C> ShareApiLookup<T, C> getLookup(Identifier lookupId, Class<T> apiClass, Class<C> contextClass) {
		Objects.requireNonNull(apiClass, "Id of API cannot be null");
		Objects.requireNonNull(contextClass, "Context key cannot be null");

		//noinspection unchecked
		return (ShareApiLookup<T, C>) PROVIDERS.getLookup(lookupId, apiClass, contextClass);
	}

	private static void failIfFalse(boolean value, String message) {
		if (!value) throw new AssertionError(message);
	}

	private static final class LookupImpl<T, C> implements ShareApiLookup<T, C> {
		private ShareApiProvider<T, C> provider;

		@Override
		public synchronized void setProvider(ShareApiProvider<T, C> provider) {
			this.provider = provider;
		}
	}
}
