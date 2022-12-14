package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

public class SlimeSetup {
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(SlimeRegistry.TNT_SLIME.get(), Monster.createMonsterAttributes().build());
	}

	public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		event.register(SlimeRegistry.TNT_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				TNTSlime::checkTNTSlimeSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
	}
}
