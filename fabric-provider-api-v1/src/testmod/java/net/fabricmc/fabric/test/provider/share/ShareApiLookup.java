package net.fabricmc.fabric.test.provider.share;

public interface ShareApiLookup<T, C> {
	void setProvider(ShareApiProvider<T, C> provider);
}
