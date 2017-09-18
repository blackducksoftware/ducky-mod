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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIWatchTarget extends EntityAIBase {

    protected EntityLiving theWatcher;
    /** The closest entity which is being watched by this one. */
    protected Entity closestEntity;
    /** This is the Maximum distance that the AI will look for the Entity */
    protected float maxDistance;
    private int lookTime;
    private final float chance;
    protected Predicate<? super Entity> watchedClassSelector;

    protected EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;

    public EntityAIWatchTarget(final EntityLiving theWatcher, @Nullable final Predicate<? super Entity> watchedClassSelector, final float maxDistance, final int lookTime) {
        this(theWatcher, watchedClassSelector, maxDistance, lookTime, 0.02F);
    }

    public EntityAIWatchTarget(final EntityLiving theWatcher, @Nullable final Predicate<? super Entity> watchedClassSelector, final float maxDistance, final int lookTime, final float chanceIn) {
        this.theWatcher = theWatcher;
        this.watchedClassSelector = watchedClassSelector;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(2);
        this.lookTime = lookTime;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(theWatcher);
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

            final List<Entity> list = theWatcher.worldObj.<Entity> getEntitiesWithinAABB(EntityLiving.class, this.getTargetableArea(maxDistance), watchedClassSelector);

            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.theNearestAttackableTargetSorter);
                this.closestEntity = list.get(0);
                return true;
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(final double targetDistance) {
        return this.theWatcher.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        return !this.closestEntity.isEntityAlive() ? false : (this.theWatcher.getDistanceSqToEntity(this.closestEntity) > this.maxDistance * this.maxDistance ? false : this.lookTime > 0);
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
    public void updateTask() {
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, this.theWatcher.getHorizontalFaceSpeed(),
                this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }

}
