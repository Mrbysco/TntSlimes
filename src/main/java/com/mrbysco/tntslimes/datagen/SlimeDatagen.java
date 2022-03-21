package com.mrbysco.tntslimes.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.registry.SlimeRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlimeDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(new Language(generator));
			generator.addProvider(new ItemModels(generator, helper));
		}
	}

	private static class Loots extends LootTableProvider {
		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(
					Pair.of(SlimeLootTables::new, LootContextParamSets.ENTITY)
			);
		}

		public static class SlimeLootTables extends EntityLoot {

			@Override
			protected void addTables() {
				this.add(SlimeRegistry.TNT_SLIME.get(), LootTable.lootTable());
			}

			@Override
			protected Iterable<EntityType<?>> getKnownEntities() {
				Stream<EntityType<?>> entityTypeStream = SlimeRegistry.ENTITIES.getEntries().stream().map(RegistryObject::get);
				return entityTypeStream::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
			map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
		}
	}

	private static class Language extends LanguageProvider {
		public Language(DataGenerator gen) {
			super(gen, TNTSlimes.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			addEntityType(SlimeRegistry.TNT_SLIME, "TNT Slime");

			addItem(SlimeRegistry.TNT_SLIME_SPAWN_EGG, "TNT Slime Spawn Egg");
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, TNTSlimes.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			for (RegistryObject<Item> item : SlimeRegistry.ITEMS.getEntries()) {
				if (item.get() instanceof SpawnEggItem) {
					withExistingParent(item.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
				}
			}
		}
	}
}
