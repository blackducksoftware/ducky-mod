/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
