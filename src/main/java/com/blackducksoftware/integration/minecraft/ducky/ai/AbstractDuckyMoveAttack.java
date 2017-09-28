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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractDuckyMoveAttack extends EntityAIBase {
    private final EntityDucky ducky;
    private Entity targetToFollow;
    protected int attackTick;
    protected final int attackInterval = 20;
    protected float oldWaterCost;
    protected double distanceToTarget;
    protected double attackReach;

    public AbstractDuckyMoveAttack(final EntityDucky ducky) {
        this.ducky = ducky;
    }

    public EntityDucky getDucky() {
        return ducky;
    }

    public Entity getTargetToFollow() {
        return targetToFollow;
    }

    public void setTargetToFollow(final Entity targetToFollow) {
        this.targetToFollow = targetToFollow;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.oldWaterCost = getDucky().getPathPriority(PathNodeType.WATER);
        getDucky().setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.targetToFollow = null;
        getDucky().setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    protected boolean updateCalc(final double distanceToTarget) {
        this.attackTick = Math.max(this.attackTick - 1, 0);
        return getTargetToFollow().isEntityAlive();
    }

    protected double getSpeedModifier(final double distanceToTarget) {
        double speedModifier = 1.0D;
        if (attackReach > 0 && distanceToTarget < attackReach * 4) {
            speedModifier = 0.75D;
        }
        return speedModifier;
    }

    public boolean isEmptyBlock(final BlockPos pos) {
        final IBlockState iblockstate = getDucky().worldObj.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }

    public boolean needToFly(final Entity target) {
        if (target == null) {
            return false;
        }
        final PathNavigate navigator = ducky.getNavigator();
        Path path = navigator.getPath();

        boolean shouldFly = false;
        if (path == null) {
            final double speedModifier = getSpeedModifier(distanceToTarget);
            getDucky().getNavigator().tryMoveToEntityLiving(target, speedModifier);
            path = navigator.getPath();
        }
        if (path != null) {
            if (getDucky().getNavigator().noPath()) {
                shouldFly = true;
            } else {
                final PathPoint pathpoint = path.getFinalPathPoint();
                if (pathpoint == null) {
                    shouldFly = true;
                } else {
                    final int i = MathHelper.floor_double(target.posY) - pathpoint.yCoord;
                    shouldFly = !target.onGround || (i > 1.25D);
                }
            }
        } else {
            shouldFly = true;
        }
        if (shouldFly) {
            navigator.clearPathEntity();
        }
        return shouldFly;
    }

    protected double getAttackReachSqr(final Entity target) {

        return 1 + ducky.width * 2.0F * ducky.width * 2.0F + (target.width / 4);
    }

    protected void checkAndPerformAttack(final Entity target, final double distance) {
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

    public float rotationYawToTarget() {
        double dirx = getDucky().posX - getTargetToFollow().posX;
        final double diry = getDucky().posY - getTargetToFollow().posY;
        double dirz = getDucky().posZ - getTargetToFollow().posZ;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        dirz /= len;
        double yaw = Math.atan2(dirz, dirx);
        // to degree
        yaw = yaw * 180.0 / Math.PI;
        yaw += 90f;
        return (float) yaw;
    }
}
