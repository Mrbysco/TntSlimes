package com.mrbysco.tntslimes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.tntslimes.TNTSlimes;
import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

public class TNTSlimeRenderer extends SlimeRenderer {
	private static final ResourceLocation TNT_SLIME_TEXTURE = ResourceLocation.fromNamespaceAndPath(TNTSlimes.MOD_ID, "textures/entity/tnt_slime/tnt_slime.png");

	public TNTSlimeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public SlimeRenderState createRenderState() {
		return new TNTSlimeRenderState();
	}

	@Override
	public void extractRenderState(Slime slime, SlimeRenderState renderState, float partialTick) {
		super.extractRenderState(slime, renderState, partialTick);
		if (slime instanceof TNTSlime tntSlime && renderState instanceof TNTSlimeRenderState tntState) {
			tntState.swelling = tntSlime.getSwelling(partialTick);
		}
	}

	@Override
	protected void scale(SlimeRenderState renderState, PoseStack poseStack) {
		super.scale(renderState, poseStack);
		if (renderState instanceof TNTSlimeRenderState tntState) {
			float f = tntState.swelling;
			float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f2 = (1.0F + f * 0.4F) * f1;
			float f3 = (1.0F + f * 0.1F) / f1;
			poseStack.scale(f2, f3, f2);
		}
	}

	@Override
	protected float getWhiteOverlayProgress(SlimeRenderState renderState) {
		if (renderState instanceof TNTSlimeRenderState tntState) {
			float f = tntState.swelling;
			return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
		}
		return super.getWhiteOverlayProgress(renderState);
	}

	@Override
	public ResourceLocation getTextureLocation(SlimeRenderState renderState) {
		return TNT_SLIME_TEXTURE;
	}
}
