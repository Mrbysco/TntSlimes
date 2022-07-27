package com.mrbysco.tntslimes.config;

import com.mrbysco.tntslimes.TNTSlimes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class SlimeConfig {
	public static class Common {
		public final ForgeConfigSpec.IntValue minY;

		Common(ForgeConfigSpec.Builder builder) {
			builder.push("Spawn settings")
					.comment("Settings for the slime spawn rates");

			minY = builder
					.comment("Defines from which Y level and below  a TNT slime can naturally spawn underground [Default: 40]")
					.defineInRange("minY", 40, Integer.MIN_VALUE, Integer.MAX_VALUE);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec spawnSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		spawnSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		TNTSlimes.LOGGER.debug("Loaded TNT Slime's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		TNTSlimes.LOGGER.warn("TNT Slime's config just got changed on the file system!");
	}
}
