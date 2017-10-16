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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class DuckyAIFollowOwner extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwner(final EntityDucky ducky, final float minDistance, final float maxDistance) {
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
        distanceToTarget = getDucky().getDistanceSqToEntity(owner);
        if (distanceToTarget > maxDistance * maxDistance) {
            if (needToFly(owner)) {
                if (!getDucky().isCanFly()) {
                    setTargetToFollow(owner);
                    return true;
                }
            } else {
                setTargetToFollow(owner);
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
        if (!getDucky().canMove() || getDucky().isAttacking() || getDucky().getNavigator().noPath()) {
            getDucky().getNavigator().clearPathEntity();
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance) {
            if (needToFly(getTargetToFollow())) {
                if (!getDucky().isCanFly()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        getDucky().getNavigator().clearPathEntity();
        return false;
    }

    @Override
    public void updateTask() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        final BlockPos location = getPositionBelowTarget();
        getDucky().getNavigator().tryMoveToXYZ(location.getX(), location.getY(), location.getZ(), speedModifier);

        if ((getDucky().getNavigator().getPath() != null && distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE) || (distanceToTarget > minDistance * minDistance && isDuckyStuck())) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigator().clearPathEntity();
            }
        }
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
    }

}
