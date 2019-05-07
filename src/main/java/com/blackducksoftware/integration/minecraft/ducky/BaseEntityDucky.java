/**
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import java.util.UUID;

import javax.annotation.Nullable;

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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public abstract class BaseEntityDucky extends EntityTameable {
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

    protected static final DataParameter<Byte> IS_FIRE_PROOF = EntityDataManager.<Byte>createKey(BaseEntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> CAN_FLY = EntityDataManager.<Byte>createKey(BaseEntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> STRENGTH = EntityDataManager.<Byte>createKey(BaseEntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> SPEED = EntityDataManager.<Byte>createKey(BaseEntityDucky.class, DataSerializers.BYTE);

    private boolean isFlying;
    private boolean isAttacking;

    protected final DuckyAIFlyTowardsTargetAndAttack duckyAIFlyTowardsTargetAndAttack;
    protected final DuckyAIFollowOwnerFlying duckyAIFollowOwnerFlying;

    protected final PathNavigateGround groundNavigator;
    protected final DuckyPathNavigateFlying flyingNavigator;

    protected final EntityMoveHelper groundMoveHelper;
    protected final DuckyFlyHelper flyingMoveHelper;

    protected BaseEntityDucky(EntityType<?> type, final World worldIn) {
        super(type, worldIn);
        this.setSize(0.4F, 0.7F);
        this.setScale(1.0F);
        duckyAIFlyTowardsTargetAndAttack = new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32);
        duckyAIFollowOwnerFlying = new DuckyAIFollowOwnerFlying(this, 3.0F, 12.0F);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        groundNavigator = new PathNavigateGround(this, worldIn);
        groundNavigator.setCanSwim(true);
        groundNavigator.setEnterDoors(true);
        this.navigator = groundNavigator;
        flyingNavigator = new DuckyPathNavigateFlying(this, worldIn);
        flyingNavigator.setCanSwim(true);
        flyingNavigator.setCanEnterDoors(true);
        groundMoveHelper = new EntityMoveHelper(this);
        this.moveHelper = groundMoveHelper;
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
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new DuckyAIPanic(this, 1.4D));
        this.tasks.addTask(2, new DuckyAIWander(this, 1.0D, 100));
        this.tasks.addTask(3, new DuckyAILookIdle(this));
        // this.tasks.addTask(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.tasks.addTask(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntitySlime.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityShulker.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityGhast.class, true, false));
        this.targetTasks.addTask(6, new EntityAIHurtByTarget(this, true, EntityMob.class, EntitySlime.class, EntityShulker.class, EntityGhast.class));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BASE_HEALTH);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(BASE_SPEED);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    @Override
    public boolean canAttackClass(final Class<? extends EntityLivingBase> cls) {
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
        this.destPos = (float) (this.destPos + (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }
        this.wingRotDelta = (float) (this.wingRotDelta * 0.9D);
        this.wingRotation += this.wingRotDelta * 2.0F;
        if (!this.onGround && this.motionY < 0.0D && !isFlying() && isTooHigh()) {
            this.motionY *= 0.6D;
        }
    }

    public boolean isTooHigh() {
        BlockPos loc = null;
        final double y = this.getPosition().getY();
        boolean isTooHigh = true;
        for (double i = y; i >= y - 3; i--) {
            loc = new BlockPos(this.getPosition().getX(), i, this.getPosition().getZ());
            final IBlockState blockstate = this.world.getBlockState(loc);
            if (blockstate.getMaterial() != Material.AIR || blockstate.isFullCube()) {
                isTooHigh = false;
            }
        }
        return isTooHigh;
    }

    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
        if (canAttackFrom) {
            this.applyEnchantments(this, entityIn);
        }
        return canAttackFrom;
    }

    /**
     * Return true if you want to skip processing the other hand
     */
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (isBreedingItem(itemstack)) {
            if (!player.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                entityTamedDucky.setOwnerId(player.getUniqueID());
                entityTamedDucky.setTamed(true);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.playTameEffect(true);
            }
            return true;
        }
        return false;
    }

    public void setAttributesFromOriginal(final BaseEntityDucky originalDucky, final UUID ownerId) {
        this.setSitting(originalDucky.isSitting());
        this.moveToBlockPosAndAngles(originalDucky.getPosition(), 0.0F, 0.0F);
        this.navigator.clearPath();
        this.setAttackTarget(null);
        if (ownerId != null) {
            this.setOwnerId(ownerId);
            this.setTamed(true);
        }
    }

    protected void spawnTamedDucky(final EntityPlayer player, final EntityTamedDucky entityTamedDucky) {
        if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(entityTamedDucky, player)) {
            entityTamedDucky.setAttributesFromOriginal(this, player.getUniqueID());
            entityTamedDucky.onInitialSpawn(entityTamedDucky.world.getDifficultyForLocation(this.getPosition()), (IEntityLivingData) null, null);
            this.remove();
            entityTamedDucky.world.spawnEntity(entityTamedDucky);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (this.aiSit != null) {
                setSitting(false);
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean canMove() {
        return !isSitting() && !getLeashed();
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() == Items.BREAD;
    }

    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return DuckyModSounds.duckQuack;
    }

    @Override
    protected SoundEvent getHurtSound(final DamageSource source) {
        return DuckyModSounds.duckHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DuckyModSounds.duckDeath;
    }

    @Override
    protected void playStepSound(BlockPos pos, IBlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public boolean isSitting() {
        final byte tamedByte = this.dataManager.get(TAMED).byteValue();
        final boolean sitting = (tamedByte & 1) != 0;
        return sitting;
    }

    @Override
    public void setSitting(final boolean sitting) {
        if (this.aiSit != null) {
            this.aiSit.setSitting(sitting);
            final byte tamedByte = this.dataManager.get(TAMED).byteValue();
            if (sitting) {
                this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte | 1)));
            } else {
                this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte & -2)));
            }
        }
    }

    private boolean getManagedByteBoolean(final DataParameter<Byte> dataParameter) {
        final byte managedByte = this.dataManager.get(dataParameter).byteValue();
        final boolean value = (managedByte & 1) != 0;
        return value;
    }

    private void setManagedByteBoolean(final DataParameter<Byte> dataParameter, final boolean value) {
        final byte managedByte = this.dataManager.get(dataParameter).byteValue();
        if (value) {
            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte | 1)));
        } else {
            this.dataManager.set(dataParameter, Byte.valueOf((byte) (managedByte & -2)));
        }
    }

    public boolean isFireProof() {
        return getManagedByteBoolean(IS_FIRE_PROOF);
    }

    public void setFireProof(final boolean fireProof) {
        this.isImmuneToFire = fireProof;
        setManagedByteBoolean(IS_FIRE_PROOF, fireProof);
    }

    public boolean isCanFly() {
        return getManagedByteBoolean(CAN_FLY);
    }

    public void setCanFly(final boolean canFly) {
        setManagedByteBoolean(CAN_FLY, canFly);
        if (canFly) {
            this.tasks.addTask(4, duckyAIFlyTowardsTargetAndAttack);
            this.tasks.addTask(7, duckyAIFollowOwnerFlying);
        } else {
            this.tasks.removeTask(duckyAIFlyTowardsTargetAndAttack);
            this.tasks.removeTask(duckyAIFollowOwnerFlying);
        }
    }

    public boolean isStrong() {
        return getManagedByteBoolean(STRENGTH);
    }

    public void setStrong(final boolean strong) {
        setManagedByteBoolean(STRENGTH, strong);
        if (strong) {
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(INCREASED_DAMAGE);
        } else {
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
        }
    }

    public boolean isFast() {
        return getManagedByteBoolean(SPEED);
    }

    public void setFast(final boolean fast) {
        setManagedByteBoolean(SPEED, fast);
        if (fast) {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(FAST_SPEED);
            this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(FAST_SPEED);
        } else {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
            this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(BASE_SPEED);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(final NBTTagCompound compound) {
        super.writeAdditional(compound);
        compound.setBoolean("FireProof", this.isFireProof());
        compound.setBoolean("CanFly", this.isCanFly());
        compound.setBoolean("Strong", this.isStrong());
        compound.setBoolean("Fast", this.isFast());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(final NBTTagCompound compound) {
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
    protected int getExperiencePoints(final EntityPlayer player) {
        return 10;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean canSpawn(IWorld worldIn, boolean fromSpawner) {
        return false;
    }

    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        return null;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    @Override
    public void setScaleForAge(final boolean child) {
        this.setScale(1.0F);
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(final boolean isFlying) {
        this.isFlying = isFlying;
        if (isFlying) {
            this.navigator = flyingNavigator;
            this.moveHelper = flyingMoveHelper;
        } else {
            this.navigator = groundNavigator;
            this.moveHelper = groundMoveHelper;
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(final boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public PathNavigate getGroundNavigator() {
        return groundNavigator;
    }

    public PathNavigate getFlyingNavigator() {
        return flyingNavigator;
    }
}
