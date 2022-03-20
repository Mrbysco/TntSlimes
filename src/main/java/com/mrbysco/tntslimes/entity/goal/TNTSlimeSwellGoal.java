package com.mrbysco.tntslimes.entity.goal;

import com.mrbysco.tntslimes.entity.TNTSlime;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TNTSlimeSwellGoal extends Goal {
	private final TNTSlime tntSlime;
	@Nullable
	private LivingEntity target;

	public TNTSlimeSwellGoal(TNTSlime tntSlime) {
		this.tntSlime = tntSlime;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public boolean canUse() {
		LivingEntity livingentity = this.tntSlime.getTarget();
		System.out.println(livingentity != null ? this.tntSlime.distanceToSqr(livingentity) : "");
		return this.tntSlime.getSwellDir() > 0 || livingentity != null && this.tntSlime.distanceToSqr(livingentity) < 9.0D;
	}

	public void start() {
		this.tntSlime.getNavigation().stop();
		this.target = this.tntSlime.getTarget();
	}

	public void stop() {
		this.target = null;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (this.target == null) {
			this.tntSlime.setSwellDir(-1);
		} else if (this.tntSlime.distanceToSqr(this.target) > 49.0D) {
			this.tntSlime.setSwellDir(-1);
		} else if (!this.tntSlime.getSensing().hasLineOfSight(this.target)) {
			this.tntSlime.setSwellDir(-1);
		} else {
			this.tntSlime.setSwellDir(1);
		}
	}
}
