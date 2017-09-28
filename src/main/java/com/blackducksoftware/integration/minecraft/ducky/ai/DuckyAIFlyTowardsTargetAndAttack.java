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

public class DuckyAIFlyTowardsTargetAndAttack extends AbstractDuckyMoveAttack {
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;
    private final long memoryLength;
    private long targetLastSeen = 0;

    public DuckyAIFlyTowardsTargetAndAttack(final EntityDucky creature, final float targetMaxDistance, final long memoryLength) {
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
        if (target == null || getDucky().isSitting()) {
            return false;
        }
        setTargetToFollow(target);
        distanceToTarget = getDucky().getDistanceSqToEntity(target);
        attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distanceToTarget);
        if (!needToFly(target) || distanceToTarget > this.maxTargetDistance * this.maxTargetDistance) {
            return false;
        }
        getDucky().setFlying(true);
        getDucky().setAttacking(true);
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (!getTargetToFollow().isEntityAlive() || !needToFly(getTargetToFollow()) || getDucky().isSitting()) {
            getDucky().setFlying(false);
            getDucky().setAttacking(false);
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
        if (targetLastSeen < memoryLength && distanceToTarget < this.maxTargetDistance * this.maxTargetDistance) {
            return true;
        }
        getDucky().setFlying(false);
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
        if (!getDucky().canEntityBeSeen(getTargetToFollow())) {
            targetLastSeen++;
        } else {
            targetLastSeen = 0;
        }
        final double xDifference = getTargetToFollow().posX - getDucky().posX;
        final double yDifference = getTargetToFollow().posY + 0.2D - getDucky().posY;
        final double zDifference = getTargetToFollow().posZ - getDucky().posZ;
        final double xMove = (Math.signum(xDifference) * 0.5D - getDucky().motionX) * 0.10000000149011612D;
        final double yMove = (yDifference / 10 * 0.8D);
        final double zMove = (Math.signum(zDifference) * 0.5D - getDucky().motionZ) * 0.10000000149011612D;

        getDucky().motionX += xMove;
        getDucky().motionY = yMove;
        getDucky().motionZ += zMove;
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }
}
