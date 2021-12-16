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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;

public class DuckyAIFollowOwner extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwner(EntityDucky ducky, float minDistance, float maxDistance) {
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

    private boolean ableToFollow() {
        PathNavigation navigation = getDucky().getNavigation();
        boolean hasRemainingPath = navigation.isDone() || navigation.isStuck() || navigation.getPath() == null;
        return getDucky().canMove() && hasRemainingPath;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (!ableToFollow() || getDucky().isAttacking()) {
            getDucky().getNavigation().stop();
            return false;
        }
        distanceToTarget = getDucky().distanceToSqr(getTargetToFollow());
        if (distanceToTarget > minDistance * minDistance) {
            if (needToFly(getTargetToFollow())) {
                if (!getDucky().isCanFly()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        getDucky().getNavigation().stop();
        return false;
    }

    @Override
    public void tick() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        double speedModifier = getSpeedModifier(distanceToTarget);
        BlockPos location = getPositionBelowTarget();
        getDucky().getNavigation().moveTo(location.getX(), location.getY(), location.getZ(), speedModifier);

        if ((getDucky().getNavigation().getPath() != null && distanceToTarget >= TELEPORT_RANGE * TELEPORT_RANGE) || (distanceToTarget > minDistance * minDistance && isDuckyStuck())) {
            if (relocateDuckyNearTarget()) {
                getDucky().getNavigation().stop();
            }
        }
        //        getDucky().faceEntity(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
        getDucky().getLookControl().setLookAt(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
    }

}
