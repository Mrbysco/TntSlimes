package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SlimeRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TNTSlimes.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TNTSlimes.MOD_ID);

	public static final DeferredHolder<EntityType<?>, EntityType<TNTSlime>> TNT_SLIME = ENTITY_TYPES.register("tnt_slime",
			() -> EntityType.Builder.<TNTSlime>of(TNTSlime::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10).build("tnt_slime"));

	public static final DeferredItem<DeferredSpawnEggItem> TNT_SLIME_SPAWN_EGG = ITEMS.register("tnt_slime_spawn_egg", () ->
			new DeferredSpawnEggItem(TNT_SLIME::get, 0xb11527, 0xdb2f1a, (new Item.Properties())));
}
