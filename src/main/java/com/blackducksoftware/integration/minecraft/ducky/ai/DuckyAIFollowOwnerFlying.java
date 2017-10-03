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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class DuckyAIFollowOwnerFlying extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwnerFlying(final EntityDucky ducky, final float minDistance, final float maxDistance) {
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
        if (distanceToTarget > maxDistance * maxDistance && needToFly(owner)) {
            setTargetToFollow(owner);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (!getDucky().canMove() || getDucky().isAttacking() || getDucky().getDistanceSqToEntity(getTargetToFollow()) < minDistance * minDistance || !needToFly(getTargetToFollow())) {
            return false;
        }
        if (getDucky().getDistanceSqToEntity(getTargetToFollow()) > minDistance * minDistance && needToFly(getTargetToFollow()) && getDucky().posY < getTargetToFollow().posY) {
            distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
            return true;
        }
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
        if (distanceToTarget >= 256.0D || isDuckyStuck()) {
            relocateDuckyNearTarget();
        } else {
            Vec3d vector = getTargetToFollow().getPositionVector().subtract(getDucky().getPositionVector());
            vector = vector.normalize().scale(getDucky().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            getDucky().motionX = vector.xCoord;
            getDucky().motionY = vector.yCoord + 0.05F;
            getDucky().motionZ = vector.zCoord;

            getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
            getDucky().getLookHelper().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        }
    }

}
