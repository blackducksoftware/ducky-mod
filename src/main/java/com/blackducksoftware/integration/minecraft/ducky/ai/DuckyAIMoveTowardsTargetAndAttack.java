/**
 * 0.6.1-release
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

import java.util.EnumSet;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class DuckyAIMoveTowardsTargetAndAttack extends AbstractDuckyMoveAttack {
    /**
     * If the distance to the target entity is further than this, this AI task will not run.
     */
    private final float maxTargetDistance;

    public DuckyAIMoveTowardsTargetAndAttack(final EntityDucky creature, final float targetMaxDistance) {
        super(creature);
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
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
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance && !needToFly(target)) {
            if (needToFly(target)) {
                if (!getDucky().isCanFly()) {
                    getDucky().setAttacking(true);
                    return true;
                }
            } else {
                getDucky().setAttacking(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (!getTargetToFollow().isAlive() || !getDucky().canMove() || isDuckyStuck()) {
            getDucky().setAttacking(false);
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(getTargetToFollow());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance) {
            if (needToFly(getTargetToFollow())) {
                if (!getDucky().isCanFly()) {
                    return true;
                }
            } else {
                return true;
            }
        }
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
        final double speedModifier = getSpeedModifier(distanceToTarget);
        final BlockPos location = getPositionBelowTarget();
        getDucky().getNavigator().tryMoveToXYZ(location.getX(), location.getY(), location.getZ(), speedModifier);

        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookController().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }

}
