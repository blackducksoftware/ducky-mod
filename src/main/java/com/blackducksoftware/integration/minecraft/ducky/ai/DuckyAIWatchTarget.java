/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

public class DuckyAIWatchTarget extends TargetGoal {
    protected MobEntity theWatcher;
    /**
     * The closest entity which is being watched by this one.
     */
    protected Entity closestEntity;
    /**
     * This is the Maximum distance that the AI will look for the Entity
     */
    protected float maxDistance;
    private int lookTime;
    private final float chance;
    protected Predicate<LivingEntity> watchedClassSelector;

    public DuckyAIWatchTarget(MobEntity theWatcher, @Nullable Predicate<LivingEntity> watchedClassSelector, float maxDistance, int lookTime) {
        this(theWatcher, watchedClassSelector, maxDistance, lookTime, 0.02F);
    }

    public DuckyAIWatchTarget(MobEntity theWatcher, @Nullable Predicate<LivingEntity> watchedClassSelector, float maxDistance, int lookTime, float chanceIn) {
        super(theWatcher, false);
        this.theWatcher = theWatcher;
        this.watchedClassSelector = watchedClassSelector;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
        this.lookTime = lookTime;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (this.theWatcher.getRNG().nextFloat() >= this.chance) {
            return false;
        } else {
            if (this.theWatcher.getAttackTarget() != null) {
                this.closestEntity = this.theWatcher.getAttackTarget();
            }

            List<LivingEntity> list = theWatcher.world.getEntitiesWithinAABB(LivingEntity.class, this.getTargetableArea(maxDistance), watchedClassSelector);

            if (list.isEmpty()) {
                return false;
            } else {
                this.closestEntity = list.get(0);
                return true;
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.theWatcher.getBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !this.closestEntity.isAlive() ? false : (this.theWatcher.getDistanceSq(this.closestEntity) > this.maxDistance * this.maxDistance ? false : this.lookTime > 0);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        if (lookTime == 0) {
            this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
        }
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.closestEntity = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        this.theWatcher.getLookController()
            .setLookPosition(this.closestEntity.getPosX(), this.closestEntity.getPosY() + this.closestEntity.getEyeHeight(), this.closestEntity.getPosZ(), this.theWatcher.getHorizontalFaceSpeed(),
                this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }

}
