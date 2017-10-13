/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import java.util.UUID;

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.DuckyModSounds;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFlyTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwnerFlying;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAILookIdle;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIMoveTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIWander;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDucky extends EntityTameable {
    public static final double GIANT_HEALTH = 128.0D;
    public static final double TAMED_HEALTH = 64.0D;
    public static final double TAMED_DAMAGE = 15.0D;

    public static final double BASE_HEALTH = 15.0D;
    public static final double BASE_DAMAGE = 6.0D;
    public static final double BASE_SPEED = 0.35D;

    public static final double INCREASED_DAMAGE = 40.0D;
    public static final double FAST_SPEED = 0.65D;

    public static final String DUCKY_NAME = "bd_ducky";

    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    protected static final DataParameter<Byte> IS_FIRE_PROOF = EntityDataManager.<Byte> createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> CAN_FLY = EntityDataManager.<Byte> createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> STRENGTH = EntityDataManager.<Byte> createKey(EntityDucky.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> SPEED = EntityDataManager.<Byte> createKey(EntityDucky.class, DataSerializers.BYTE);

    private boolean isFlying;
    private boolean isAttacking;

    protected final DuckyAIFlyTowardsTargetAndAttack duckyAIFlyTowardsTargetAndAttack;
    protected final DuckyAIFollowOwnerFlying duckyAIFollowOwnerFlying;

    public EntityDucky(final World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.setScale(1.0F);
        duckyAIFlyTowardsTargetAndAttack = new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32);
        duckyAIFollowOwnerFlying = new DuckyAIFollowOwnerFlying(this, 3.0F, 8.0F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_FIRE_PROOF, Byte.valueOf((byte) 0));
        this.dataManager.register(CAN_FLY, Byte.valueOf((byte) 0));
        this.dataManager.register(STRENGTH, Byte.valueOf((byte) 0));
        this.dataManager.register(SPEED, Byte.valueOf((byte) 0));
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new DuckyAIWander(this, 1.0D, 100));
        this.tasks.addTask(2, new DuckyAILookIdle(this));
        // this.tasks.addTask(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.tasks.addTask(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityShulker.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityGhast.class, true, false));
        this.targetTasks.addTask(6, new EntityAIHurtByTarget(this, true, EntityMob.class, EntityShulker.class, EntityGhast.class));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BASE_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
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
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) (this.destPos + (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
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
            final IBlockState blockstate = this.worldObj.getBlockState(loc);
            if (blockstate.getMaterial() != Material.AIR || blockstate.isFullCube()) {
                isTooHigh = false;
            }
        }
        return isTooHigh;
    }

    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
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
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.worldObj.isRemote) {
                final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.worldObj);
                entityTamedDucky.setOwnerId(player.getUniqueID());
                entityTamedDucky.setTamed(true);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.playTameEffect(true);
            }
            return true;
        }
        return false;
    }

    public void setAttributesFromOriginal(final EntityDucky originalDucky, final UUID ownerId) {
        this.setSitting(originalDucky.isSitting());
        this.moveToBlockPosAndAngles(originalDucky.getPosition(), 0.0F, 0.0F);
        this.navigator.clearPathEntity();
        this.setAttackTarget((EntityLivingBase) null);
        if (ownerId != null) {
            this.setOwnerId(ownerId);
            this.setTamed(true);
        }
    }

    protected void spawnTamedDucky(final EntityPlayer player, final EntityTamedDucky entityTamedDucky) {
        if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(entityTamedDucky, player)) {
            entityTamedDucky.setAttributesFromOriginal(this, player.getUniqueID());
            entityTamedDucky.onInitialSpawn(entityTamedDucky.worldObj.getDifficultyForLocation(this.getPosition()), (IEntityLivingData) null);
            this.setDead();
            entityTamedDucky.worldObj.spawnEntityInWorld(entityTamedDucky);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
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
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
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
        this.aiSit.setSitting(sitting);
        final byte tamedByte = this.dataManager.get(TAMED).byteValue();
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte | 1)));
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte & -2)));
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
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(INCREASED_DAMAGE);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
        }
    }

    public boolean isFast() {
        return getManagedByteBoolean(SPEED);
    }

    public void setFast(final boolean fast) {
        setManagedByteBoolean(SPEED, fast);
        if (fast) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(FAST_SPEED);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("FireProof", this.isFireProof());
        compound.setBoolean("CanFly", this.isCanFly());
        compound.setBoolean("Strong", this.isStrong());
        compound.setBoolean("Fast", this.isFast());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
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
        return 20;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
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
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(final boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}
