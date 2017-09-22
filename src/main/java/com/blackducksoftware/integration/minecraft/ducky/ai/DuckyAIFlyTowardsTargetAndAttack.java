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
import net.minecraft.util.math.MathHelper;

public class DuckyAIFlyTowardsTargetAndAttack extends AbstractDuckyMoveTowardsTarget {
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;
    private final long memoryLength;
    private long targetLastSeen = 0;

    private EntityLivingBase target;

    public DuckyAIFlyTowardsTargetAndAttack(final EntityDucky creature, final float targetMaxDistance, final long memoryLength) {
        super(creature);
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
        this.memoryLength = memoryLength;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        target = getDucky().getAttackTarget();
        if (target == null) {
            return false;
        }
        final double distance = getDucky().getDistanceSqToEntity(target);
        final double attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distance);
        if (!needToFly(target) || distance < attackReach || distance > this.maxTargetDistance * this.maxTargetDistance) {
            return false;
        }
        getDucky().setFlying(true);
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (!target.isEntityAlive() || !needToFly(target)) {
            getDucky().setFlying(false);
            return false;
        }
        final double distance = getDucky().getDistanceSqToEntity(target);
        final double attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distance);
        if (distance > attackReach && targetLastSeen < memoryLength && distance < this.maxTargetDistance * this.maxTargetDistance) {
            return true;
        }
        getDucky().setFlying(false);
        return false;
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        getDucky().getNavigator().clearPathEntity();
        target = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        super.updateTask();
        if (!target.isEntityAlive()) {
            return;
        }
        if (!target.canEntityBeSeen(getDucky())) {
            targetLastSeen++;
        } else {
            targetLastSeen = 0;
        }
        final double xDifference = target.posX - getDucky().posX;
        final double yDifference = target.posY + 0.2D - getDucky().posY;
        final double zDifference = target.posZ - getDucky().posZ;
        final double xMove = (Math.signum(xDifference) * 0.5D - getDucky().motionX) * 0.10000000149011612D;
        final double yMove = (yDifference / 10 * 0.8D);
        final double zMove = (Math.signum(zDifference) * 0.5D - getDucky().motionZ) * 0.10000000149011612D;

        final float f = (float) (MathHelper.atan2(getDucky().motionZ, getDucky().motionX) * (180D / Math.PI)) - 90.0F;
        final float f1 = MathHelper.wrapDegrees(f - getDucky().rotationYaw);
        getDucky().rotationYaw += f1;

        getDucky().getLookHelper().setLookPosition(target.posX, target.posY + target.getEyeHeight(), target.posZ, getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().motionX += xMove;
        getDucky().motionY = yMove;
        getDucky().motionZ += zMove;
    }
}
