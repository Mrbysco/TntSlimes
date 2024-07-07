package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class SlimeSetup {
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(SlimeRegistry.TNT_SLIME.get(), Monster.createMonsterAttributes().build());
	}

	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(SlimeRegistry.TNT_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				TNTSlime::checkTNTSlimeSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
	}
}
