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
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDucky extends EntityMob {

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
        final Predicate<EntityLiving> predicate = new Predicate<EntityLiving>() {
            @Override
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                if (p_apply_1_ != null) {
                    final boolean shouldTarget = !(p_apply_1_ instanceof EntityAgeable || p_apply_1_ instanceof EntityWaterMob || p_apply_1_ instanceof EntityDucky);
                    return shouldTarget;
                }
                return false;
            }
        };
        this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.tasks.addTask(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.tasks.addTask(4, new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, false, predicate));
        this.tasks.addTask(6, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.60D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
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
}
