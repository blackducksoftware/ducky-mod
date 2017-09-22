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

public class DuckyAIMoveTowardsTargetAndAttack extends AbstractDuckyMoveTowardsTarget {
    private final double speed;
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;

    private EntityLivingBase target;

    public DuckyAIMoveTowardsTargetAndAttack(final EntityDucky creature, final double speedIn, final float targetMaxDistance) {
        super(creature);
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
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
        final double distance = target.getDistanceSqToEntity(getDucky());
        final double attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distance);
        if (needToFly(target)) {
            return false;
        } else if (distance < this.maxTargetDistance * this.maxTargetDistance && distance > attackReach) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (!target.isEntityAlive() || needToFly(target) || getDucky().getNavigator().noPath()) {
            return false;
        }
        final double distance = getDucky().getDistanceSqToEntity(target);
        final double attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distance);
        if (distance < this.maxTargetDistance * this.maxTargetDistance && distance > attackReach) {
            return true;
        }
        return false;
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
        getDucky().getNavigator().tryMoveToEntityLiving(target, this.speed);

        final float f = (float) (MathHelper.atan2(getDucky().motionZ, getDucky().motionX) * (180D / Math.PI)) - 90.0F;
        final float f1 = MathHelper.wrapDegrees(f - getDucky().rotationYaw);
        getDucky().rotationYaw += f1;

        getDucky().getLookHelper().setLookPosition(target.posX, target.posY + target.getEyeHeight(), target.posZ, getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        target = null;
        getDucky().getNavigator().clearPathEntity();
    }

}
