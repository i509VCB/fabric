package net.fabricmc.indigo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class IndigoMixinConfigPlugin implements IMixinConfigPlugin {
	private static final String JSON_ELEMENT = "fabric-renderer-api-v1:contains_renderer";
	private static Boolean indigoApplicable;

	static boolean shouldApplyIndigo() {
		if (indigoApplicable != null) return indigoApplicable;

		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			if (container.getMetadata().containsCustomElement(JSON_ELEMENT)) {
				indigoApplicable = false;
				return false;
			}
		}

		indigoApplicable = true;
		return true;
	}

	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return shouldApplyIndigo();
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
