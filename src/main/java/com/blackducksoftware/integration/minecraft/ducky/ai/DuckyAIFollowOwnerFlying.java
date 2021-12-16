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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DuckyAIFollowOwnerFlying extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwnerFlying(EntityDucky ducky, float minDistance, float maxDistance) {
        super(ducky);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        LivingEntity owner = getDucky().getOwner();
        if (owner == null || !getDucky().canMove() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof Player && owner.isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().distanceToSqr(owner);
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
    public boolean canContinueToUse() {
        if (!getDucky().canMove() || getDucky().isAttacking() || getDucky().distanceToSqr(getTargetToFollow()) < minDistance * minDistance || !needToFly(getTargetToFollow())) {
            getDucky().setFlying(false);
            return false;
        }
        distanceToTarget = getDucky().distanceToSqr(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance && needToFly(getTargetToFollow()) && getDucky().getY() < getTargetToFollow().getY()) {
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
        getDucky().getNavigation().moveTo(getTargetToFollow(), speedModifier);

        boolean isStuck = isDuckyStuck();
        if (distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE || (distanceToTarget > minDistance * minDistance && isStuck)) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigation().stop();
            }
        }
        // getDucky().faceEntity(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
        getDucky().getLookControl().setLookAt(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
    }

}
