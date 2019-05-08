/**
 * Copyright (C) 2019 Synopsys, Inc.
 * https://www.synopsys.com/
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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.AxisAlignedBB;

public class DuckyAIWatchTarget extends EntityAIBase {
    protected EntityLiving theWatcher;
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
    protected Predicate<EntityLivingBase> watchedClassSelector;

    protected EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;

    public DuckyAIWatchTarget(final EntityLiving theWatcher, @Nullable final Predicate<EntityLivingBase> watchedClassSelector, final float maxDistance, final int lookTime) {
        this(theWatcher, watchedClassSelector, maxDistance, lookTime, 0.02F);
    }

    public DuckyAIWatchTarget(final EntityLiving theWatcher, @Nullable final Predicate<EntityLivingBase> watchedClassSelector, final float maxDistance, final int lookTime, final float chanceIn) {
        this.theWatcher = theWatcher;
        this.watchedClassSelector = watchedClassSelector;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(5);
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

            final List<EntityLiving> list = theWatcher.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, this.getTargetableArea(maxDistance), watchedClassSelector);

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
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, this.theWatcher.getHorizontalFaceSpeed(),
            this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }

}
