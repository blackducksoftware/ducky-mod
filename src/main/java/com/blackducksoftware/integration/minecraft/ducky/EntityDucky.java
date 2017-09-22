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

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFlyTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIMoveTowardsTargetAndAttack;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDucky extends EntityTameable {
    public static final double TAMED_HEALTH = 64.0D;
    public static final double TAMED_DAMAGE = 30.0D;
    public static final double BASE_HEALTH = 15.0D;
    public static final double BASE_DAMAGE = 6.0D;

    public static final String DUCKY_NAME = "bd_ducky";

    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    private boolean isFlying;

    public EntityDucky(final World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(final boolean isFlying) {
        this.isFlying = isFlying;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.tasks.addTask(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.tasks.addTask(4, new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityShulker.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityGhast.class, true, false));
        this.tasks.addTask(6, new EntityAIHurtByTarget(this, true));
        this.tasks.addTask(7, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BASE_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.60D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    @Override
    public boolean canAttackClass(final Class<? extends EntityLivingBase> cls) {
        return cls != EntityPlayer.class && cls != EntityDucky.class && !cls.isAssignableFrom(EntityAgeable.class);
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
        if (!this.onGround && this.motionY < 0.0D && !isFlying()) { // && !this.isInWater() && !this.isInLava()) {
            this.motionY *= 0.6D;
        }
    }

    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
        if (canAttackFrom) {
            this.applyEnchantments(this, entityIn);
        }
        return canAttackFrom;
    }

    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BASE_HEALTH);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
            this.setHealth((float) BASE_HEALTH);
        }
    }

    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (isBreedingItem(itemstack)) {
            return false;
        }
        if (this.isTamed()) {
            if (!itemstack.func_190926_b()) {
                if (itemstack.getItem() instanceof ItemFood) {
                    final ItemFood itemfood = (ItemFood) itemstack.getItem();
                    if (itemfood.isWolfsFavoriteMeat() && (this.getHealth() < TAMED_HEALTH)) {
                        if (!player.capabilities.isCreativeMode) {
                            itemstack.func_190918_g(1);
                        }
                        System.out.println("Healing from : " + this.getHealth());
                        this.heal(itemfood.getHealAmount(itemstack));
                        System.out.println("Healedto : " + this.getHealth());
                        return true;
                    }
                }
            }
            if (this.isOwner(player) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack)) {
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget((EntityLivingBase) null);
            }
        } else {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.worldObj.isRemote) {
                if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget((EntityLivingBase) null);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() == Items.FISH;
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
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
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

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return 1000;
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
}
