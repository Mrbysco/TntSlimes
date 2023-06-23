package com.mrbysco.tntslimes;

import com.mojang.logging.LogUtils;
import com.mrbysco.tntslimes.client.ClientHandler;
import com.mrbysco.tntslimes.config.SlimeConfig;
import com.mrbysco.tntslimes.registry.SlimeRegistry;
import com.mrbysco.tntslimes.registry.SlimeSetup;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TNTSlimes.MOD_ID)
public class TNTSlimes {
	public static final String MOD_ID = "tntslimes";
	public static final Logger LOGGER = LogUtils.getLogger();

	public TNTSlimes() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, SlimeConfig.spawnSpec);
		eventBus.register(SlimeConfig.class);

		SlimeRegistry.ITEMS.register(eventBus);
		SlimeRegistry.ENTITY_TYPES.register(eventBus);

		eventBus.addListener(this::addTabContents);

		eventBus.addListener(SlimeSetup::registerSpawnPlacements);
		eventBus.addListener(SlimeSetup::registerEntityAttributes);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		});
	}

	private void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(SlimeRegistry.TNT_SLIME_SPAWN_EGG);
		}
	}
}
