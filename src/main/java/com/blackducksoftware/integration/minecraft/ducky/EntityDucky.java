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
import com.blackducksoftware.integration.minecraft.ducky.pathfinding.DuckyFlyHelper;
import com.blackducksoftware.integration.minecraft.ducky.pathfinding.DuckyPathNavigateFlying;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class EntityDucky extends TameableEntity {
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

    protected static final DataParameter<Byte> IS_FIRE_PROOF = EntityDataManager.<Byte>createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> CAN_FLY = EntityDataManager.<Byte>createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> STRENGTH = EntityDataManager.<Byte>createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> SPEED = EntityDataManager.<Byte>createKey(EntityDucky.class, DataSerializers.BYTE);

    private boolean isFlying;
    private boolean isAttacking;
    private boolean isFireProof;

    protected final DuckyAIFlyTowardsTargetAndAttack duckyAIFlyTowardsTargetAndAttack = new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32);
    protected final DuckyAIFollowOwnerFlying duckyAIFollowOwnerFlying = new DuckyAIFollowOwnerFlying(this, 1.0F, 12.0F);

    protected final GroundPathNavigator groundNavigator;
    protected final DuckyPathNavigateFlying flyingNavigator;

    protected final MovementController groundMoveHelper;
    protected final DuckyFlyHelper flyingMoveHelper;

    public EntityDucky(World worldIn) {
        this(DuckyModEntities.DUCKY, worldIn);
    }

    public EntityDucky(EntityType<? extends EntityDucky> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);

        groundNavigator = new GroundPathNavigator(this, worldIn);
        groundNavigator.setCanSwim(true);
        this.navigator = groundNavigator;

        flyingNavigator = new DuckyPathNavigateFlying(this, worldIn);
        flyingNavigator.setCanSwim(true);
        flyingNavigator.setCanEnterDoors(true);

        groundMoveHelper = new MovementController(this);
        this.moveController = groundMoveHelper;

        flyingMoveHelper = new DuckyFlyHelper(this);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_FIRE_PROOF, Byte.valueOf((byte) 0));
        this.dataManager.register(CAN_FLY, Byte.valueOf((byte) 0));
        this.dataManager.register(STRENGTH, Byte.valueOf((byte) 0));
        this.dataManager.register(SPEED, Byte.valueOf((byte) 0));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new DuckyAIPanic(this, 1.4D));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(10, new DuckyAIWander(this, 1.0D, 100));
        this.goalSelector.addGoal(10, new DuckyAILookIdle(this));
        //  this.goalSelector.addGoal(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.goalSelector.addGoal(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PhantomEntity.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SlimeEntity.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ShulkerEntity.class, true, false));
        this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, GhastEntity.class, true, false));
        this.targetSelector.addGoal(6, new HurtByTargetGoal(this));
    }

    // registerAttributes
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                   .func_233815_a_(Attributes.field_233818_a_, BASE_HEALTH)
                   .func_233815_a_(Attributes.field_233821_d_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233822_e_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233819_b_, 64.0D)
                   .func_233815_a_(Attributes.field_233823_f_, BASE_DAMAGE);
    }

    //    @Override
    //    protected void registerAttributes() {
    //        super.registerAttributes();
    //        // MAX_HEALTH Attributes.field_233818_a_
    //        // MOVEMENT_SPEED Attributes.field_233821_d_
    //        // FLYING_SPEED Attributes.field_233822_e_
    //        // FOLLOW_RANGE Attributes.field_233819_b_
    //        // ATTACK_DAMAGE Attributes.field_233823_f_
    //
    //        this.getAttribute(Attributes.field_233818_a_).setBaseValue(BASE_HEALTH);
    //        this.getAttribute(Attributes.field_233821_d_).setBaseValue(BASE_SPEED);
    //        this.getAttributes().registerAttribute(Attributes.field_233822_e_).setBaseValue(BASE_SPEED);
    //        this.getAttribute(Attributes.field_233819_b_).setBaseValue(64.0D);
    //        this.getAttributes().registerAttribute(Attributes.field_233823_f_).setBaseValue(BASE_DAMAGE);
    //    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    @Override
    public boolean canAttack(EntityType cls) {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick() {
        super.livingTick();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }
        this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);
        Vector3d vec3d = this.getMotion();
        if (!this.onGround && vec3d.getY() < 0.0D) {
            this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        }
        this.wingRotation += this.wingRotDelta * 2.0F;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        // ATTACK_DAMAGE Attributes.field_233823_f_
        boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.field_233823_f_).getValue()));
        if (canAttackFrom) {
            this.applyEnchantments(this, entityIn);
        }
        return canAttackFrom;
    }

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
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (this.isBreedingItem(itemstack)) {
            if (!this.world.isRemote) {
                EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                spawnTamedDucky(player, entityTamedDucky);
            }
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }

        return super.func_230254_b_(player, hand);
    }

    public void setAttributesFromOriginal(EntityDucky originalDucky, UUID ownerId) {
        this.setSitting(originalDucky.isSitting());
        BlockPos position = new BlockPos(originalDucky.getPositionVec());
        Vector2f pitchAndYaw = originalDucky.getPitchYaw();
        float pitch = pitchAndYaw.x;
        float yaw = pitchAndYaw.y;
        this.moveToBlockPosAndAngles(position, yaw, pitch);
        this.getNavigator().clearPath();
        this.setAttackTarget(null);
        if (ownerId != null) {
            this.setOwnerId(ownerId);
            this.setTamed(true);
        }
    }

    protected void spawnTamedDucky(PlayerEntity player, EntityTamedDucky entityTamedDucky) {
        if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(entityTamedDucky, player)) {
            entityTamedDucky.setAttributesFromOriginal(this, player.getUniqueID());
            this.remove();
            entityTamedDucky.onInitialSpawn(entityTamedDucky.world, entityTamedDucky.world.getDifficultyForLocation(this.getPositionUnderneath()), SpawnReason.SPAWN_EGG, (ILivingEntityData) null, null);
            entityTamedDucky.world.addEntity(entityTamedDucky);
            entityTamedDucky.setTamedBy(player);
            this.world.setEntityState(entityTamedDucky, (byte) 7);
            //entityTamedDucky.playTameEffect(true);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        setSitting(false);
        return super.attackEntityFrom(source, amount);

    }

    public boolean canMove() {
        return !isSitting() && !getLeashed();
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.BREAD;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

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
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    //    @Override
    public boolean isSitting() {
        byte tamedByte = this.dataManager.get(TAMED).byteValue();
        boolean sitting = (tamedByte & 1) != 0;
        return sitting;
    }

    //    @Override
    public void setSitting(boolean sitting) {
        //        if (this.getAISit() != null) {
        //            this.getAISit().setSitting(sitting);
        byte tamedByte = this.dataManager.get(TAMED).byteValue();
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte | 1)));
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte & -2)));
        }
        //        }
    }

    private boolean getManagedByteBoolean(DataParameter<Byte> dataParameter) {
        byte managedByte = this.dataManager.get(dataParameter).byteValue();
        boolean value = (managedByte & 1) != 0;
        return value;
    }

    private void setManagedByteBoolean(DataParameter<Byte> dataParameter, boolean value) {
        byte managedByte = this.dataManager.get(dataParameter).byteValue();
        if (value) {
            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte | 1)));
        } else {
            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte & -2)));
        }
    }

    public boolean isFireProof() {
        if (!isFireProof) {
            isFireProof = getManagedByteBoolean(IS_FIRE_PROOF);
        }
        return isFireProof;
    }

    public void setFireProof(boolean fireProof) {
        setManagedByteBoolean(IS_FIRE_PROOF, fireProof);
        isFireProof = fireProof;
    }

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
        if (isCanFly() && DamageSource.FALL.getDamageType().equals(damageSrc.getDamageType())) {
            return true;
        } else if (isFireProof() && damageSrc.isFireDamage()) {
            return true;
        }
        return super.isInvulnerableTo(damageSrc);
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    @Override
    protected void setOnFireFromLava() {
        if (!this.isFireProof()) {
            super.setOnFireFromLava();
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    @Override
    public void setFire(int seconds) {
        if (!this.isFireProof()) {
            super.setFire(seconds);
        }
    }

    public boolean isCanFly() {
        return getManagedByteBoolean(CAN_FLY);
    }

    public void setCanFly(boolean canFly) {
        setManagedByteBoolean(CAN_FLY, canFly);
        if (canFly) {
            this.goalSelector.addGoal(4, duckyAIFlyTowardsTargetAndAttack);
            this.goalSelector.addGoal(7, duckyAIFollowOwnerFlying);
        } else {
            this.goalSelector.removeGoal(duckyAIFlyTowardsTargetAndAttack);
            this.goalSelector.removeGoal(duckyAIFollowOwnerFlying);
        }
    }

    public boolean isStrong() {
        return getManagedByteBoolean(STRENGTH);
    }

    public void setStrong(boolean strong) {
        setManagedByteBoolean(STRENGTH, strong);
        // ATTACK_DAMAGE Attributes.field_233823_f_
        if (strong) {
            this.getAttribute(Attributes.field_233823_f_).setBaseValue(INCREASED_DAMAGE);
        } else {
            this.getAttribute(Attributes.field_233823_f_).setBaseValue(BASE_DAMAGE);
        }
    }

    public boolean isFast() {
        return getManagedByteBoolean(SPEED);
    }

    public void setFast(boolean fast) {
        setManagedByteBoolean(SPEED, fast);
        // MOVEMENT_SPEED Attributes.field_233821_d_
        // FLYING_SPEED Attributes.field_233822_e_
        if (fast) {
            this.getAttribute(Attributes.field_233821_d_).setBaseValue(FAST_SPEED);
            this.getAttribute(Attributes.field_233822_e_).setBaseValue(FAST_SPEED);
        } else {
            this.getAttribute(Attributes.field_233821_d_).setBaseValue(BASE_SPEED);
            this.getAttribute(Attributes.field_233822_e_).setBaseValue(BASE_SPEED);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("FireProof", this.isFireProof());
        compound.putBoolean("CanFly", this.isCanFly());
        compound.putBoolean("Strong", this.isStrong());
        compound.putBoolean("Fast", this.isFast());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setFireProof(compound.getBoolean("FireProof"));
        this.setCanFly(compound.getBoolean("CanFly"));
        this.setStrong(compound.getBoolean("Strong"));
        this.setFast(compound.getBoolean("Fast"));
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 10;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReason) {
        return false;
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
        if (isFlying) {
            this.navigator = flyingNavigator;
            this.moveController = flyingMoveHelper;
        } else {
            this.navigator = groundNavigator;
            this.moveController = groundMoveHelper;
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public GroundPathNavigator getGroundNavigator() {
        return groundNavigator;
    }

    public DuckyPathNavigateFlying getFlyingNavigator() {
        return flyingNavigator;
    }
}
