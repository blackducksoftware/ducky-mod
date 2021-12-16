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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

public class DuckyAIWatchTarget extends TargetGoal {
    protected Mob theWatcher;
    /**
     * The closest entity which is being watched by this one.
     */
    protected LivingEntity closestEntity;
    /**
     * This is the Maximum distance that the AI will look for the Entity
     */
    protected float maxDistance;
    private int lookTime;
    private final float chance;
    private TargetingConditions targetingConditions;

    public DuckyAIWatchTarget(Mob theWatcher, @Nullable Predicate<LivingEntity> watchedClassSelector, float maxDistance, int lookTime) {
        this(theWatcher, watchedClassSelector, maxDistance, lookTime, 0.02F);
    }

    public DuckyAIWatchTarget(Mob theWatcher, @Nullable Predicate<LivingEntity> watchedClassSelector, float maxDistance, int lookTime, float chanceIn) {
        super(theWatcher, false);
        this.theWatcher = theWatcher;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.lookTime = lookTime;
        targetingConditions = TargetingConditions.forNonCombat().range(maxDistance).selector(watchedClassSelector);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        if (this.theWatcher.getRandom().nextFloat() >= this.chance) {
            return false;
        } else {
            if (this.theWatcher.getTarget() != null) {
                this.closestEntity = this.theWatcher.getTarget();
            }

            List<LivingEntity> list = theWatcher.level.getNearbyEntities(LivingEntity.class, targetingConditions, this.theWatcher, this.getTargetableArea(maxDistance));

            if (list.isEmpty()) {
                return false;
            } else {
                this.closestEntity = list.get(0);
                return true;
            }
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.theWatcher.getBoundingBox().expandTowards(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return !this.closestEntity.isAlive() ? false : (this.theWatcher.distanceToSqr(this.closestEntity) > this.maxDistance * this.maxDistance ? false : this.lookTime > 0);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        if (lookTime == 0) {
            this.lookTime = 40 + this.theWatcher.getRandom().nextInt(40);
        }
        super.start();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        this.closestEntity = null;
        super.stop();
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        this.theWatcher.getLookControl()
            .setLookAt(this.closestEntity.getX(), this.closestEntity.getY() + this.closestEntity.getEyeHeight(),
                this.closestEntity.getZ(), this.theWatcher.getMaxHeadXRot(), this.theWatcher.getMaxHeadYRot());
        --this.lookTime;
    }

}
