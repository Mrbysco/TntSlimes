package com.mrbysco.tntslimes;

import com.mojang.logging.LogUtils;
import com.mrbysco.tntslimes.client.ClientHandler;
import com.mrbysco.tntslimes.config.SlimeConfig;
import com.mrbysco.tntslimes.registry.SlimeRegistry;
import com.mrbysco.tntslimes.registry.SlimeSetup;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(TNTSlimes.MOD_ID)
public class TNTSlimes {
	public static final String MOD_ID = "tntslimes";
	public static final Logger LOGGER = LogUtils.getLogger();

	public TNTSlimes(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SlimeConfig.spawnSpec);
		eventBus.register(SlimeConfig.class);

		SlimeRegistry.ITEMS.register(eventBus);
		SlimeRegistry.ENTITY_TYPES.register(eventBus);

		eventBus.addListener(this::addTabContents);

		eventBus.addListener(SlimeSetup::registerSpawnPlacements);
		eventBus.addListener(SlimeSetup::registerEntityAttributes);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		}
	}

	private void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(SlimeRegistry.TNT_SLIME_SPAWN_EGG);
		}
	}
}
