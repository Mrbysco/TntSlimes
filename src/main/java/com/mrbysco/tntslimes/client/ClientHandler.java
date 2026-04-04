package com.mrbysco.tntslimes.client;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.client.renderer.TNTSlimeRenderer;
import com.mrbysco.tntslimes.registry.SlimeRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	public static final ModelLayerLocation TNT_SLIME = new ModelLayerLocation(TNTSlimes.modLoc("tnt_slime"), "main");
	public static final ModelLayerLocation TNT_SLIME_OUTER = new ModelLayerLocation(TNTSlimes.modLoc("tnt_slime"), "outer");

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SlimeRegistry.TNT_SLIME.get(), TNTSlimeRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TNT_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TNT_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
	}
}
