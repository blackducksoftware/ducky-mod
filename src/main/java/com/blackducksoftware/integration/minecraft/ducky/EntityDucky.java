/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import java.util.UUID;

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.DuckyModEntities;
import com.blackducksoftware.integration.minecraft.DuckyModSounds;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFlyTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwnerFlying;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAILookIdle;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIMoveTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIPanic;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIWander;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIWatchTarget;
import com.blackducksoftware.integration.minecraft.ducky.pathfinding.DuckyFlyHelper;
import com.blackducksoftware.integration.minecraft.ducky.pathfinding.DuckyPathNavigateFlying;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityDucky extends TamableAnimal {
    public static final String DUCKY_NAME = "bd_ducky";

    public static final double GIANT_HEALTH = 128.0D;
    public static final double TAMED_HEALTH = 64.0D;
    public static final double TAMED_DAMAGE = 15.0D;

    public static final double BASE_HEALTH = 15.0D;
    public static final double BASE_DAMAGE = 6.0D;
    public static final double BASE_SPEED = 0.35D;

    public static final double INCREASED_DAMAGE = 40.0D;
    public static final double FAST_SPEED = 0.65D;

    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    protected static final EntityDataAccessor<Boolean> IS_FIRE_PROOF = SynchedEntityData.defineId(EntityDucky.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> CAN_FLY = SynchedEntityData.defineId(EntityDucky.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_STRONG = SynchedEntityData.defineId(EntityDucky.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_FAST = SynchedEntityData.defineId(EntityDucky.class, EntityDataSerializers.BOOLEAN);

    private boolean isFlying;
    private boolean isAttacking;
    private boolean isFireProof;

    protected final DuckyAIFlyTowardsTargetAndAttack duckyAIFlyTowardsTargetAndAttack = new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32);
    protected final DuckyAIFollowOwnerFlying duckyAIFollowOwnerFlying = new DuckyAIFollowOwnerFlying(this, 1.0F, 12.0F);

    protected final GroundPathNavigation groundNavigator;
    protected final DuckyPathNavigateFlying flyingNavigator;

    protected final MoveControl groundMoveHelper;
    protected final DuckyFlyHelper flyingMoveHelper;

    public EntityDucky(Level level) {
        this(DuckyModEntities.DUCKY, level);
    }

    public EntityDucky(EntityType<? extends EntityDucky> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);

        groundNavigator = new GroundPathNavigation(this, level);
        groundNavigator.setCanFloat(true);
        this.navigation = groundNavigator;

        flyingNavigator = new DuckyPathNavigateFlying(this, level);
        flyingNavigator.setCanFloat(true);
        flyingNavigator.setCanOpenDoors(true);
        flyingNavigator.setCanPassDoors(true);

        groundMoveHelper = new MoveControl(this);
        this.moveControl = groundMoveHelper;

        flyingMoveHelper = new DuckyFlyHelper(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FIRE_PROOF, false);
        this.entityData.define(CAN_FLY, false);
        this.entityData.define(IS_STRONG, false);
        this.entityData.define(IS_FAST, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DuckyAIPanic(this, 1.4D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(10, new DuckyAIWander(this, 1.0D, 100));
        this.goalSelector.addGoal(10, new DuckyAILookIdle(this));
        //          this.goalSelector.addGoal(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.goalSelector.addGoal(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Monster.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Phantom.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Slime.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Shulker.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Ghast.class, true, false));
        this.targetSelector.addGoal(6, new OwnerHurtByTargetGoal(this));
    }

    // registerAttributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                   .add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
                   .add(Attributes.FLYING_SPEED, BASE_SPEED)
                   .add(Attributes.FOLLOW_RANGE, 64.0D)
                   .add(Attributes.MAX_HEALTH, BASE_HEALTH)
                   .add(Attributes.ATTACK_DAMAGE, BASE_DAMAGE);
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    @Override
    public boolean canAttack(LivingEntity entity) {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void tick() {
        super.tick();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = Mth.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }
        this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);
        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround && vec3d.y() < 0.0D) {
            this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }
        this.wingRotation += this.wingRotDelta * 2.0F;
    }

    //    @Override
    //    public boolean attackEntityAsMob(Entity entityIn) {
    //        // ATTACK_DAMAGE Attributes.field_233823_f_
    //        boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.field_233823_f_).getValue()));
    //        if (canAttackFrom) {
    //            this.applyEnchantments(this, entityIn);
    //        }
    //        return canAttackFrom;
    //    }

    //    /**
    //     * Return true if you want to skip processing the other hand
    //     */
    //    @Override
    //    public boolean processInteract(PlayerEntity player, Hand hand) {
    //        ItemStack itemstack = player.getHeldItem(hand);
    //        if (isBreedingItem(itemstack)) {
    //            if (!player.abilities.isCreativeMode) {
    //                itemstack.shrink(1);
    //            }
    //            if (!this.world.isRemote) {
    //                EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
    //                entityTamedDucky.setOwnerId(player.getUniqueID());
    //                entityTamedDucky.setTamed(true);
    //                spawnTamedDucky(player, entityTamedDucky);
    //                entityTamedDucky.playTameEffect(true);
    //            }
    //            return true;
    //        }
    //        return false;
    //    }

    //processInteract
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isFood(itemstack)) {
            if (this.level.isClientSide()) {
                EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.level);
                spawnTamedDucky(player, entityTamedDucky);
            }
            return InteractionResult.sidedSuccess(!this.level.isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    public boolean canMove() {
        return !isInSittingPose() && !isLeashed();
    }

    public void setAttributesFromOriginal(EntityDucky originalDucky, UUID ownerId) {
        this.setOrderedToSit(originalDucky.isOrderedToSit());
        this.setInSittingPose(originalDucky.isInSittingPose());
        BlockPos position = new BlockPos(originalDucky.getOnPos());
        Vec2 pitchAndYaw = originalDucky.getRotationVector();
        float pitch = pitchAndYaw.x;
        float yaw = pitchAndYaw.y;
        this.moveTo(position, yaw, pitch);
        this.getNavigation().stop();
        this.setTarget(null);
        if (ownerId != null) {
            this.setOwnerUUID(ownerId);
            this.setTame(true);
        }
    }

    protected void spawnTamedDucky(Player player, EntityTamedDucky entityTamedDucky) {
        if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(entityTamedDucky, player)) {
            entityTamedDucky.setAttributesFromOriginal(this, player.getUUID());
            this.remove(RemovalReason.DISCARDED);
            entityTamedDucky.level.addFreshEntity(entityTamedDucky);
            //entityTamedDucky.finalizeSpawn(entityTamedDucky.getServer().getLevel()),
            // entityTamedDucky.level.getCurrentDifficultyAt(entityTamedDucky.blockPosition()), spawnType, (SpawnGroupData) null, (CompoundTag) null);
            //this.world.setEntityState(entityTamedDucky, (byte) 7);
            //entityTamedDucky.playTameEffect(true);
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (MobSpawnType.SPAWN_EGG.equals(spawnType)) {
            return super.finalizeSpawn(serverLevelAccessor, difficulty, spawnType, spawnGroupData, compoundTag);
        }
        return null;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    //    /**
    //     * Checks if the entity's current position is a valid location to spawn this entity.
    //     */
    //    @Override
    //    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReason) {
    //        return false;
    //    }
    //
    //    @Override
    //    public AgeableEntity createChild(AgeableEntity ageable) {
    //        return null;
    //    }

    //    /**
    //     * Called when the entity is attacked.
    //     */
    //    @Override
    //    public boolean attackEntityFrom(DamageSource source, float amount) {
    //        if (this.isInvulnerableTo(source)) {
    //            return false;
    //        }
    //        setSitting(false);
    //        return super.attackEntityFrom(source, amount);
    //
    //    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.BREAD;
    }

    //    @Override
    //    public boolean canMateWith(AnimalEntity otherAnimal) {
    //        return false;
    //    }

    @Override
    protected SoundEvent getAmbientSound() {
        return DuckyModSounds.duckQuack;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return DuckyModSounds.duckHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DuckyModSounds.duckDeath;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    //    @Override
    //    @Nullable
    //    public ResourceLocation getLootTable() {
    //        return null;
    //    }

    //    //    @Override
    //    public boolean isSitting() {
    //        byte tamedByte = this.dataManager.get(TAMED).byteValue();
    //        boolean sitting = (tamedByte & 1) != 0;
    //        return sitting;
    //    }
    //
    //    //    @Override
    //    public void setSitting(boolean sitting) {
    //        //        if (this.getAISit() != null) {
    //        //            this.getAISit().setSitting(sitting);
    //        byte tamedByte = this.dataManager.get(TAMED).byteValue();
    //        if (sitting) {
    //            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte | 1)));
    //        } else {
    //            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte & -2)));
    //        }
    //        //        }
    //    }

    //    private boolean getManagedByteBoolean(DataParameter<Byte> dataParameter) {
    //        byte managedByte = this.dataManager.get(dataParameter).byteValue();
    //        boolean value = (managedByte & 1) != 0;
    //        return value;
    //    }
    //
    //    private void setManagedByteBoolean(DataParameter<Byte> dataParameter, boolean value) {
    //        byte managedByte = this.dataManager.get(dataParameter).byteValue();
    //        if (value) {
    //            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte | 1)));
    //        } else {
    //            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte & -2)));
    //        }
    //    }

    //    /**
    //     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
    //     */
    //    @Override
    //    protected void dealFireDamage(int amount) {
    //        if (!this.isFireProof()) {
    //            this.attackEntityFrom(DamageSource.IN_FIRE, (float) amount);
    //        }
    //    }

    /**
     * Returns whether this Entity is invulnerable to the given DamageSource.
     */
    @Override
    public boolean isInvulnerableTo(DamageSource damageSrc) {
        if (isCanFly() && damageSrc.isFall()) {
            return true;
        } else if (isFireProof() && damageSrc.isFire()) {
            return true;
        }
        return super.isInvulnerableTo(damageSrc);
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    @Override
    public void setRemainingFireTicks(int ticks) {
        if (this.isFireProof()) {
            super.setRemainingFireTicks(0);
        } else {
            super.setRemainingFireTicks(ticks);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    @Override
    public void setSecondsOnFire(int seconds) {
        if (this.isFireProof()) {
            super.setSecondsOnFire(0);
        } else {
            super.setSecondsOnFire(seconds);
        }
    }

    public boolean isFireProof() {
        if (!isFireProof) {
            isFireProof = this.entityData.get(IS_FIRE_PROOF);
        }
        return isFireProof;
    }

    public void setFireProof(boolean fireProof) {
        this.entityData.set(IS_FIRE_PROOF, fireProof);
        isFireProof = fireProof;
    }

    public boolean isCanFly() {
        return this.entityData.get(CAN_FLY);
    }

    public void setCanFly(boolean canFly) {
        this.entityData.set(CAN_FLY, canFly);
        if (canFly) {
            this.goalSelector.addGoal(4, duckyAIFlyTowardsTargetAndAttack);
            this.goalSelector.addGoal(7, duckyAIFollowOwnerFlying);
        } else {
            this.goalSelector.removeGoal(duckyAIFlyTowardsTargetAndAttack);
            this.goalSelector.removeGoal(duckyAIFollowOwnerFlying);
        }
    }

    public boolean isStrong() {
        return this.entityData.get(IS_STRONG);
    }

    public void setStrong(boolean strong) {
        this.entityData.set(IS_STRONG, strong);
        if (strong) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(INCREASED_DAMAGE);
        } else {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
        }
    }

    public boolean isFast() {
        return this.entityData.get(IS_FAST);
    }

    public void setFast(boolean fast) {
        this.entityData.set(IS_FAST, fast);
        if (fast) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(FAST_SPEED);
            this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(FAST_SPEED);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
            this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(BASE_SPEED);
        }
    }

    //    /**
    //     * (abstract) Protected helper method to write subclass entity data to NBT.
    //     */
    //    @Override
    //    public void writeAdditional(CompoundNBT compound) {
    //        super.writeAdditional(compound);
    //        compound.putBoolean("FireProof", this.isFireProof());
    //        compound.putBoolean("CanFly", this.isCanFly());
    //        compound.putBoolean("Strong", this.isStrong());
    //        compound.putBoolean("Fast", this.isFast());
    //    }
    //
    //    /**
    //     * (abstract) Protected helper method to read subclass entity data from NBT.
    //     */
    //    @Override
    //    public void readAdditional(CompoundNBT compound) {
    //        super.readAdditional(compound);
    //        this.setFireProof(compound.getBoolean("FireProof"));
    //        this.setCanFly(compound.getBoolean("CanFly"));
    //        this.setStrong(compound.getBoolean("Strong"));
    //        this.setFast(compound.getBoolean("Fast"));
    //    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperienceReward(Player player) {
        return 10;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
        if (isFlying) {
            this.navigation = flyingNavigator;
            this.moveControl = flyingMoveHelper;
        } else {
            this.navigation = groundNavigator;
            this.moveControl = groundMoveHelper;
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public GroundPathNavigation getGroundNavigator() {
        return groundNavigator;
    }

    public DuckyPathNavigateFlying getFlyingNavigator() {
        return flyingNavigator;
    }
}
