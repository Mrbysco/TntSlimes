package com.mrbysco.tntslimes.client;

import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.client.renderer.TNTSlimeRenderer;
import com.mrbysco.tntslimes.registry.SlimeRegistry;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static final ModelLayerLocation TNT_SLIME = new ModelLayerLocation(new ResourceLocation(TNTSlimes.MOD_ID, "tnt_slime"), "main");
	public static final ModelLayerLocation TNT_SLIME_OUTER = new ModelLayerLocation(new ResourceLocation(TNTSlimes.MOD_ID, "tnt_slime"), "outer");

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SlimeRegistry.TNT_SLIME.get(), TNTSlimeRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TNT_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TNT_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
	}
}
