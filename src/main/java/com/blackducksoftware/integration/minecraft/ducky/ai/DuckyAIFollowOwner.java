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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class DuckyAIFollowOwner extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwner(final EntityDucky ducky, final float minDistance, final float maxDistance) {
        super(ducky);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setMutexFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        final LivingEntity owner = getDucky().getOwner();
        if (owner == null || !getDucky().canMove() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof PlayerEntity && owner.isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(owner);
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
            getDucky().getNavigator().clearPath();
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance) {
            if (needToFly(getTargetToFollow())) {
                if (!getDucky().isCanFly()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        getDucky().getNavigator().clearPath();
        return false;
    }

    @Override
    public void tick() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        final BlockPos location = getPositionBelowTarget();
        getDucky().getNavigator().tryMoveToXYZ(location.getX(), location.getY(), location.getZ(), speedModifier);

        if ((getDucky().getNavigator().getPath() != null && distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE) || (distanceToTarget > minDistance * minDistance && isDuckyStuck())) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigator().clearPath();
            }
        }
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookController().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
    }

}
