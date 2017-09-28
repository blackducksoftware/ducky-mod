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
        if (owner == null || getDucky().isSitting() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof EntityPlayer && ((EntityPlayer) owner).isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(owner);
        if (!needToFly(owner)) {
            return false;
        }
        if (distanceToTarget > maxDistance * maxDistance) {
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
        if (getDucky().isSitting() || getDucky().isAttacking() || !needToFly(getTargetToFollow())) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(getTargetToFollow());
        if (distanceToTarget < minDistance * minDistance) {
            return false;
        }
        return true;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if (!updateCalc(distanceToTarget)) {
            return;
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
    }

}
