package com.mrbysco.tntslimes.registry;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlimeRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TNTSlimes.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TNTSlimes.MOD_ID);

	public static final RegistryObject<EntityType<TNTSlime>> TNT_SLIME = ENTITIES.register("tnt_slime",
			() -> register("inquisitor", EntityType.Builder.<TNTSlime>of(TNTSlime::new, MobCategory.MONSTER)
					.sized(2.04F, 2.04F).clientTrackingRange(10)));

	public static final RegistryObject<Item> TNT_SLIME_SPAWN_EGG = ITEMS.register("tnt_slime_spawn_egg", () ->
			new ForgeSpawnEggItem(TNT_SLIME::get, 0xb11527, 0xdb2f1a, itemBuilder()));

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}

	private static Item.Properties itemBuilder() {
		return new Item.Properties().tab(CreativeModeTab.TAB_MISC);
	}
}
