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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.Vec3d;

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
        if (target == null || !getDucky().canMove()) {
            return false;
        }
        setTargetToFollow(target);
        distanceToTarget = getDucky().getDistanceSqToEntity(target);
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
        if (!getTargetToFollow().isEntityAlive() || !getDucky().canMove()) {
            getDucky().setFlying(false);
            getDucky().setAttacking(false);
            return false;
        }
        if (getDucky().getDistanceSqToEntity(getTargetToFollow()) < this.maxTargetDistance * this.maxTargetDistance && needToFly(getTargetToFollow())) {
            distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
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
    public void updateTask() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        if (!getDucky().canEntityBeSeen(getTargetToFollow())) {
            targetLastSeen++;
        } else {
            targetLastSeen = 0;
        }
        Vec3d vector = getTargetToFollow().getPositionVector().subtract(getDucky().getPositionVector());
        vector = vector.normalize().scale(getDucky().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        getDucky().motionX = vector.x;
        float yAdjustment = 0.1F;
        if (isDuckyStuck()) {
            yAdjustment = 0.5F;
        }
        getDucky().motionY = vector.y + yAdjustment;
        getDucky().motionZ = vector.z;

        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }
}
