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

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractDuckyMoveTowardsTarget extends EntityAIBase {
    private final EntityCreature ducky;
    protected int attackTick;
    protected final int attackInterval = 20;

    public AbstractDuckyMoveTowardsTarget(final EntityCreature ducky) {
        this.ducky = ducky;
    }

    public EntityCreature getDucky() {
        return ducky;
    }

    public boolean needToFly(final EntityLivingBase target) {
        if (target == null) {
            return false;
        }
        final PathNavigate navigator = ducky.getNavigator();
        final Path path = navigator.getPath();
        if (path != null) {
            final PathPoint pathpoint = path.getFinalPathPoint();
            if (pathpoint == null) {
                return true;
            } else {
                final int i = pathpoint.yCoord - MathHelper.floor_double(target.posY);
                return !target.onGround || (i > 1.25D);
            }
        }
        return true;
    }

    protected double getAttackReachSqr(final EntityLivingBase target) {
        return ducky.width * 2.0F * ducky.width * 2.0F + target.width;
    }

    protected void checkAndPerformAttack(final EntityLivingBase target, final double distance) {
        if (target == null) {
            return;
        }
        final double attackReach = this.getAttackReachSqr(target);
        if (distance <= attackReach && this.attackTick <= 0) {
            this.attackTick = 20;
            ducky.swingArm(EnumHand.MAIN_HAND);
            ducky.attackEntityAsMob(target);
        }
    }

    @Override
    public void updateTask() {
        this.attackTick = Math.max(this.attackTick - 1, 0);
    }
}
