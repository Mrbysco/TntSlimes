package com.mrbysco.tntslimes.entity;

import com.mrbysco.tntslimes.config.SlimeConfig;
import com.mrbysco.tntslimes.entity.goal.TNTSlimeSwellGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.Random;

public class TNTSlime extends Slime {
	private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(TNTSlime.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(TNTSlime.class, EntityDataSerializers.BOOLEAN);

	private int oldSwell;
	private int swell;
	private int maxSwell = 30;
	private int explosionRadius = 2;

	public TNTSlime(EntityType<? extends Slime> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new TNTSlimeSwellGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_SWELL_DIR, -1);
		this.entityData.define(DATA_IS_IGNITED, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);

		tag.putShort("Fuse", (short) this.maxSwell);
		tag.putByte("ExplosionRadius", (byte) this.explosionRadius);
		tag.putBoolean("ignited", this.isIgnited());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("Fuse", 99)) {
			this.maxSwell = tag.getShort("Fuse");
		}

		if (tag.contains("ExplosionRadius", 99)) {
			this.explosionRadius = tag.getByte("ExplosionRadius");
		}

		if (tag.getBoolean("ignited")) {
			this.ignite();
		}
	}

	public float getSwelling(float partialTick) {
		return Mth.lerp(partialTick, (float) this.oldSwell, (float) this.swell) / (float) (this.maxSwell - 2);
	}

	public int getSwellDir() {
		return this.entityData.get(DATA_SWELL_DIR);
	}

	public void setSwellDir(int dir) {
		this.entityData.set(DATA_SWELL_DIR, dir);
	}

	public boolean isIgnited() {
		return this.entityData.get(DATA_IS_IGNITED);
	}

	public void ignite() {
		this.entityData.set(DATA_IS_IGNITED, true);
	}

	@Override
	public void tick() {
		if (this.isAlive()) {
			this.oldSwell = this.swell;
			if (this.isIgnited()) {
				this.setSwellDir(1);
			}

			int i = this.getSwellDir();
			if (i > 0 && this.swell == 0) {
				this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
				this.gameEvent(GameEvent.PRIME_FUSE);
			}

			this.swell += i;
			if (this.swell < 0) {
				this.swell = 0;
			}

			if (this.swell >= this.maxSwell) {
				this.swell = this.maxSwell;
				this.explodeSlime();
			}
		}

		super.tick();
	}

	protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		ItemStack itemstack = player.getItemInHand(interactionHand);
		if (itemstack.is(Items.FLINT_AND_STEEL)) {
			this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
			if (!this.level.isClientSide) {
				this.ignite();
				itemstack.hurtAndBreak(1, player, (p_32290_) -> {
					p_32290_.broadcastBreakEvent(interactionHand);
				});
			}

			return InteractionResult.sidedSuccess(this.level.isClientSide);
		} else {
			return super.mobInteract(player, interactionHand);
		}
	}

	private void explodeSlime() {
		if (!this.level.isClientSide) {
			Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
			float f = 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, explosion$blockinteraction);
			this.discard();
		}
	}

	@Override
	protected boolean isDealsDamage() {
		return false;
	}

	@Override
	protected void setSize(int size, boolean resetHealth) {
		int i = Mth.clamp(size, 1, 127);
		this.explosionRadius = Mth.clamp(size, 1, 16);
		this.entityData.set(ID_SIZE, i);
		this.reapplyPosition();
		this.refreshDimensions();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) (size));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.2F + 0.1F * i));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(((double) i));
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.xpReward = i;
	}

	public static boolean checkTNTSlimeSpawnRules(EntityType<TNTSlime> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random random) {
		if (level.getDifficulty() != Difficulty.PEACEFUL) {
			if (level.getBiome(pos).is(Biomes.SWAMP) && pos.getY() > 50 && pos.getY() < 70 && random.nextFloat() < 0.5F &&
					random.nextFloat() < level.getMoonBrightness() && level.getMaxLocalRawBrightness(pos) <= random.nextInt(8)) {
				return checkMobSpawnRules(entityType, level, spawnType, pos, random);
			}

			if (!(level instanceof WorldGenLevel)) {
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(pos);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel) level).getSeed(), 987234911L).nextInt(10) == 0;
			if (random.nextInt(10) == 0 && flag && pos.getY() < SlimeConfig.COMMON.minY.get()) {
				return checkMobSpawnRules(entityType, level, spawnType, pos, random);
			}
		}

		return false;
	}
}
