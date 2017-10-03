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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.Entity;

public class DuckyAIMoveTowardsTargetAndAttack extends AbstractDuckyMoveAttack {
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;

    public DuckyAIMoveTowardsTargetAndAttack(final EntityDucky creature, final float targetMaxDistance) {
        super(creature);
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
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
        distanceToTarget = getDucky().getDistanceSqToEntity(target);
        attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distanceToTarget);
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance && !needToFly(target)) {
            getDucky().setAttacking(true);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (!getTargetToFollow().isEntityAlive() || !getDucky().canMove() || isDuckyStuck()) {
            getDucky().setAttacking(false);
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance && !needToFly(getTargetToFollow())) {
            return true;
        }
        getDucky().setAttacking(false);
        return false;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        getDucky().getNavigator().tryMoveToEntityLiving(getTargetToFollow(), speedModifier);
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }

}
