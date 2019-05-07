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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.BaseEntityDucky;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class DuckyAIFollowOwnerFlying extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwnerFlying(final BaseEntityDucky ducky, final float minDistance, final float maxDistance) {
        super(ducky);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase owner = getDucky().getOwner();
        if (owner == null || !getDucky().canMove() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof EntityPlayer && ((EntityPlayer) owner).isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(owner);
        if (distanceToTarget > maxDistance * maxDistance && needToFly(owner)) {
            setTargetToFollow(owner);
            getDucky().setFlying(true);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (!getDucky().canMove() || getDucky().isAttacking() || getDucky().getDistanceSq(getTargetToFollow()) < minDistance * minDistance || !needToFly(getTargetToFollow())) {
            getDucky().setFlying(false);
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance && needToFly(getTargetToFollow()) && getDucky().posY < getTargetToFollow().posY) {
            return true;
        }
        getDucky().setFlying(false);
        return false;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        getDucky().getNavigator().tryMoveToEntityLiving(getTargetToFollow(), speedModifier);

        final boolean isStuck = isDuckyStuck();
        if (distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE || (distanceToTarget > minDistance * minDistance && isStuck)) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigator().clearPath();
            }
        }
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
    }

}
