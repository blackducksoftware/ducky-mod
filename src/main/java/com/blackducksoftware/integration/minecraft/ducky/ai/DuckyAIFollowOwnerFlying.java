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

public class DuckyAIFollowOwnerFlying extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwnerFlying(EntityDucky ducky, float minDistance, float maxDistance) {
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
        LivingEntity owner = getDucky().getOwner();
        if (owner == null || !getDucky().canMove() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof PlayerEntity && owner.isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(owner);
        if (distanceToTarget > maxDistance * maxDistance && needToFly(owner)) {
            setTargetToFollow(owner);
            getDucky().setFlying(true);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (!getDucky().canMove() || getDucky().isAttacking() || getDucky().getDistanceSq(getTargetToFollow()) < minDistance * minDistance || !needToFly(getTargetToFollow())) {
            getDucky().setFlying(false);
            return false;
        }
        distanceToTarget = getDucky().getDistanceSq(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance && needToFly(getTargetToFollow()) && getDucky().getPosY() < getTargetToFollow().getPosY()) {
            return true;
        }
        getDucky().setFlying(false);
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
        double speedModifier = getSpeedModifier(distanceToTarget);
        getDucky().getNavigator().tryMoveToEntityLiving(getTargetToFollow(), speedModifier);

        boolean isStuck = isDuckyStuck();
        if (distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE || (distanceToTarget > minDistance * minDistance && isStuck)) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigator().clearPath();
            }
        }
        getDucky().faceEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
        getDucky().getLookController().setLookPositionWithEntity(getTargetToFollow(), getDucky().getHorizontalFaceSpeed(), getDucky().getVerticalFaceSpeed());
    }

}
