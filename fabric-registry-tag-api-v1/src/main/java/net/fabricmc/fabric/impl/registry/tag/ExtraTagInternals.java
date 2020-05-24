package net.fabricmc.fabric.impl.registry.tag;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.class_5318;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

public final class ExtraTagInternals {
	/**
	 * Identifier used by packets to send additional tags.
	 */
	public static final Identifier PACKET_ID = new Identifier("fabric", "extra_tags/v1");
	/**
	 * Minor version of the packet.
	 */
	public static final int MINOR_VERSION = 0;

	static RegistryTagContainer<Biome> BIOME_TAG_CONTAINER = new RegistryTagContainer<>(Registry.BIOME, "tags/biomes", "biomes");
	static RegistryTagContainer<BlockEntityType<?>> BLOCK_ENTITY_TAG_CONTAINER = new RegistryTagContainer<>(Registry.BLOCK_ENTITY_TYPE, "tags/blockentities", "blockentities");
	// Dimensions require special handling
	static RegistryTagContainer<DimensionType> DIMENSION_TYPE_TAG_CONTAINER = createEmptyDimensionContainer();
	static RegistryTagContainer<Enchantment> ENCHANTMENT_TAG_CONTAINER = new RegistryTagContainer<>(Registry.ENCHANTMENT, "tags/enchantments", "enchantments");
	static RegistryTagContainer<PaintingMotive> PAINTING_MOTIVE_TAG_CONTAINER = new RegistryTagContainer<>(Registry.PAINTING_MOTIVE, "tags/painting_motives", "painting_motives");
	static RegistryTagContainer<ParticleType<?>> PARTICLE_TYPE_TAG_CONTAINER = new RegistryTagContainer<>(Registry.PARTICLE_TYPE, "tags/particle_types", "particle_types");
	static RegistryTagContainer<PointOfInterestType> POINT_OF_INTEREST_TYPE_TAG_CONTAINER = new RegistryTagContainer<>(Registry.POINT_OF_INTEREST_TYPE, "tags/points_of_interest", "points_of_interest");
	static RegistryTagContainer<Potion> POTION_TAG_CONTAINER = new RegistryTagContainer<>(Registry.POTION, "tags/potions", "potions");
	static RegistryTagContainer<SoundEvent> SOUND_EVENTS_CONTAINER = new RegistryTagContainer<>(Registry.SOUND_EVENT, "tags/sounds", "sounds");
	static RegistryTagContainer<StatusEffect> STATUS_EFFECTS_CONTAINER = new RegistryTagContainer<>(Registry.STATUS_EFFECT, "tags/status_effects", "status_effects");
	static RegistryTagContainer<VillagerProfession> VILLAGER_PROFESSION_CONTAINER = new RegistryTagContainer<>(Registry.VILLAGER_PROFESSION, "tags/villager_professions", "villager_professions");
	static RegistryTagContainer<VillagerType> VILLAGER_TYPE_CONTAINER = new RegistryTagContainer<>(Registry.VILLAGER_TYPE, "tags/villager_types", "villager_types");

	public static RegistryTagContainer<Biome> getBiomeContainer() {
		return BIOME_TAG_CONTAINER;
	}

	public static RegistryTagContainer<BlockEntityType<?>> getBlockEntityContainer() {
		return BLOCK_ENTITY_TAG_CONTAINER;
	}

	public static RegistryTagContainer<DimensionType> getDimensionTypeContainer() {
		return DIMENSION_TYPE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<Enchantment> getEnchantmentContainer() {
		return ENCHANTMENT_TAG_CONTAINER;
	}

	public static RegistryTagContainer<PaintingMotive> getPaintingContainer() {
		return PAINTING_MOTIVE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<ParticleType<?>> getParticleTypeContainer() {
		return PARTICLE_TYPE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<PointOfInterestType> getPointOfInterestTypeContainer() {
		return POINT_OF_INTEREST_TYPE_TAG_CONTAINER;
	}

	public static RegistryTagContainer<Potion> getPotionContainer() {
		return POTION_TAG_CONTAINER;
	}

	public static RegistryTagContainer<SoundEvent> getSoundEventContainer() {
		return SOUND_EVENTS_CONTAINER;
	}

	public static RegistryTagContainer<StatusEffect> getStatusEffectContainer() {
		return STATUS_EFFECTS_CONTAINER;
	}

	public static RegistryTagContainer<VillagerProfession> getVillagerProfessionContainer() {
		return VILLAGER_PROFESSION_CONTAINER;
	}

	public static RegistryTagContainer<VillagerType> getVillagerTypeContainer() {
		return VILLAGER_TYPE_CONTAINER;
	}

	public static void createAndSetDimensionContainer(class_5318 dimensionTracker) {
		DIMENSION_TYPE_TAG_CONTAINER = new RegistryTagContainer<>(dimensionTracker.method_29116(), "tags/dimensions", "dimensions");
	}

	private static RegistryTagContainer<DimensionType> createEmptyDimensionContainer() {
		return new RegistryTagContainer<>(new EmptyRegistry<>(), "tags/dimensions", "dimensions");
	}

	// TODO: Mark all these registries as `SYNCED` since registry tag containers use raw ids.
	private static List<Registry<?>> getSyncedRegistries() {
		final List<Registry<?>> registries = new ArrayList<>();
		// Supported registries for tags v1.0
		registries.add(Registry.BIOME);
		registries.add(Registry.ENCHANTMENT);
		registries.add(Registry.PAINTING_MOTIVE);
		registries.add(Registry.PARTICLE_TYPE);
		registries.add(Registry.POINT_OF_INTEREST_TYPE);
		registries.add(Registry.POTION);
		registries.add(Registry.SOUND_EVENT);
		registries.add(Registry.STATUS_EFFECT);
		registries.add(Registry.VILLAGER_PROFESSION);
		registries.add(Registry.VILLAGER_TYPE);

		return registries;
	}
}
