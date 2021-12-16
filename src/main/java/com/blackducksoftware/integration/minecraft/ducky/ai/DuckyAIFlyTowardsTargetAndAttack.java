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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class DuckyAIFlyTowardsTargetAndAttack extends AbstractDuckyMoveAttack {
    /**
     * If the distance to the target entity is further than this, this AI task will not run.
     */
    private final float maxTargetDistance;
    private final long memoryLength;
    private long targetLastSeen = 0;
    protected TargetingConditions targetConditions;

    public DuckyAIFlyTowardsTargetAndAttack(EntityDucky creature, float targetMaxDistance, long memoryLength) {
        super(creature);
        this.maxTargetDistance = targetMaxDistance;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
        this.memoryLength = memoryLength;
        this.targetConditions = TargetingConditions.forCombat().range(maxTargetDistance);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        LivingEntity target = getDucky().getTarget();
        if (target == null || !getDucky().canMove()) {
            return false;
        }
        setTargetToFollow(target);

        distanceToTarget = getDucky().distanceToSqr(target);
        attackReach = getAttackReachSqr(target);
        checkAndPerformAttack(target, distanceToTarget);
        if (distanceToTarget < this.maxTargetDistance * this.maxTargetDistance && needToFly(target)) {
            getDucky().setFlying(true);
            getDucky().setAttacking(true);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (!getTargetToFollow().isAlive() || !getDucky().canMove()) {
            getDucky().setFlying(false);
            getDucky().setAttacking(false);
            return false;
        }
        if (getDucky().distanceToSqr(getTargetToFollow()) < this.maxTargetDistance * this.maxTargetDistance && needToFly(getTargetToFollow())) {
            distanceToTarget = getDucky().distanceToSqr(getTargetToFollow());
            checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
            if (targetLastSeen < memoryLength) {
                return true;
            }
        }
        getDucky().setFlying(false);
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
        if (!getDucky().getSensing().hasLineOfSight(getTargetToFollow())) {
            targetLastSeen++;
        } else {
            targetLastSeen = 0;
        }
        double speedModifier = getSpeedModifier(distanceToTarget);
        double attackReachSqr = getAttackReachSqr(getTargetToFollow());
        Path path = getDucky().getNavigation().createPath(getTargetToFollow(), (int) attackReachSqr);
        getDucky().getNavigation().moveTo(path, speedModifier);

        //getDucky().faceEntity(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
        getDucky().getLookControl().setLookAt(getTargetToFollow(), getDucky().getMaxHeadXRot(), getDucky().getMaxHeadYRot());
        checkAndPerformAttack(getTargetToFollow(), distanceToTarget);
    }
}
