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

import com.blackducksoftware.integration.minecraft.ducky.BaseEntityDucky;

import net.minecraft.entity.Entity;

public class DuckyAIFlyTowardsTargetAndAttack extends AbstractDuckyMoveAttack {
    /**
     * If the distance to the target entity is further than this, this AI task will not run.
     */
    private final float maxTargetDistance;
    private final long memoryLength;
    private long targetLastSeen = 0;

    public DuckyAIFlyTowardsTargetAndAttack(final BaseEntityDucky creature, final float targetMaxDistance, final long memoryLength) {
        super(creature);
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(3);
        this.memoryLength = memoryLength;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        final Entity target = getDucky().getAttackTarget();
        if (target == null || !getDucky().canMove()) {
            return false;
        }
        setTargetToFollow(target);
        distanceToTarget = getDucky().getDistanceSq(target);
        attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distanceToTarget);
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance && needToFly(target)) {
            getDucky().setFlying(true);
            getDucky().setAttacking(true);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (!getTargetToFollow().isAlive() || !getDucky().canMove()) {
            getDucky().setFlying(false);
            getDucky().setAttacking(false);
            return false;
        }
        if (getDucky().getDistanceSq(getTargetToFollow()) < this.maxTargetDistance * this.maxTargetDistance && needToFly(getTargetToFollow())) {
            distanceToTarget = getDucky().getDistanceSq(getTargetToFollow());
            checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
            if (targetLastSeen < memoryLength) {
                return true;
            }
        }
        getDucky().setFlying(false);
        getDucky().setAttacking(false);
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
        if (!getDucky().canEntityBeSeen(getTargetToFollow())) {
            targetLastSeen++;
        } else {
            targetLastSeen = 0;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        getDucky().getNavigator().tryMoveToEntityLiving(getTargetToFollow(), speedModifier);
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());

        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }
}
