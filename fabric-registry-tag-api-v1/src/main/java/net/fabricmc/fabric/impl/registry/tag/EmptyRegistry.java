package net.fabricmc.fabric.impl.registry.tag;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.mojang.serialization.Lifecycle;

import net.minecraft.class_5321;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * An empty registry. This is immutable.
 * @param <T> The type this registry should hold.
 */
final class EmptyRegistry<T> extends Registry<T> {
	public EmptyRegistry() {
		super(null, Lifecycle.experimental());
	}

	@Override
	public Identifier getId(T entry) {
		return null;
	}

	@Override
	public class_5321<T> method_29113(T object) {
		throw new IllegalStateException("Unregistered registry element: " + object + " in " + this);
	}

	@Override
	public int getRawId(T entry) {
		return 0;
	}

	@Override
	public T method_29107(class_5321<T> arg) {
		return null;
	}

	@Override
	public T get(Identifier id) {
		return null;
	}

	@Override
	public Optional<T> getOrEmpty(Identifier id) {
		return Optional.empty();
	}

	@Override
	public Set<Identifier> getIds() {
		return Collections.emptySet();
	}

	@Override
	public boolean containsId(Identifier id) {
		return false;
	}

	@Override
	public boolean method_29112(class_5321<T> arg) {
		return false;
	}

	@Override
	public boolean method_29111(int i) {
		return false;
	}

	@Override
	public T get(int index) {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return Collections.emptyIterator();
	}

	public boolean isEmpty() {
		return true;
	}
}
