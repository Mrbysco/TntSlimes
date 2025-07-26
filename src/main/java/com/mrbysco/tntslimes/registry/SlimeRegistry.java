package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SlimeRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TNTSlimes.MOD_ID);
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(TNTSlimes.MOD_ID);

	public static final DeferredHolder<EntityType<?>, EntityType<TNTSlime>> TNT_SLIME = ENTITY_TYPES
			.registerEntityType("tnt_slime", TNTSlime::new, MobCategory.MONSTER,
					builder -> builder.sized(0.52F, 0.52F).eyeHeight(0.325F)
							.clientTrackingRange(10).spawnDimensionsScale(4.0F));

	public static final DeferredItem<SpawnEggItem> TNT_SLIME_SPAWN_EGG = ITEMS.registerItem("tnt_slime_spawn_egg",
			(properties) -> new SpawnEggItem(SlimeRegistry.TNT_SLIME.get(), properties));
}
