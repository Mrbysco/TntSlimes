package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.config.SlimeConfig;
import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TNTSlimes.MOD_ID)
public class SlimeSetup {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addSpawn(BiomeLoadingEvent event) {
		if (event.getName() != null) {
			ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
			if (BiomeDictionary.hasType(biomeKey, Type.OVERWORLD)) {
				int tntSlimeWeight = SlimeConfig.COMMON.weight.get();
				if (tntSlimeWeight > 0) {
					event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(SlimeRegistry.TNT_SLIME.get(), SlimeConfig.COMMON.weight.get(),
							SlimeConfig.COMMON.minGroup.get(), SlimeConfig.COMMON.maxGroup.get()));
				}
			}
		}
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(SlimeRegistry.TNT_SLIME.get(), Monster.createMonsterAttributes().build());
	}

	public static void registerSpawnPlacement() {
		SpawnPlacements.register(SlimeRegistry.TNT_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TNTSlime::checkTNTSlimeSpawnRules);
	}
}
