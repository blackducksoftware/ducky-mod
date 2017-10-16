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
import net.minecraft.util.math.Vec3d;

public abstract class AbstractDuckyMoveAttack extends EntityAIBase {
    private final EntityDucky ducky;
    private Entity targetToFollow;
    protected int attackTick;
    protected final int attackInterval = 20;
    protected float oldWaterCost;
    protected double distanceToTarget;
    protected double attackReach;
    protected int stuckTick;
    protected Vec3d lastPostion = null;

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
        this.stuckTick++;
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
        final IBlockState iblockstate = getDucky().world.getBlockState(pos);
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
            path = getDucky().getNavigator().getPathToEntityLiving(target);
        }
        if (path != null) {
            final PathPoint pathpoint = path.getFinalPathPoint();
            if (pathpoint == null) {
                shouldFly = true;
            } else {
                final int i = MathHelper.floor(target.posY) - pathpoint.yCoord;
                shouldFly = !target.onGround || (i > 1.25D);
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

    protected boolean isDuckyStuck() {
        if (lastPostion == null) {
            lastPostion = getDucky().getPositionVector();
        } else if (stuckTick > 20) {
            stuckTick = 0;
            if (lastPostion.squareDistanceTo(getDucky().getPositionVector()) < 2.25D) {
                return true;
            }
            lastPostion = getDucky().getPositionVector();
        }
        return false;
    }

    protected void relocateDuckyNearTarget() {
        final int startingX = MathHelper.floor(getTargetToFollow().posX) - 2;
        final int startingZ = MathHelper.floor(getTargetToFollow().posZ) - 2;
        final int startingY = MathHelper.floor(getTargetToFollow().getEntityBoundingBox().minY);

        // Search a 4x4 area around the target (at least 2 blocks away from the target) for a 2 high empty spot on a solid block to put Ducky
        for (int xAdjustment = 0; xAdjustment <= 4; ++xAdjustment) {
            for (int zAdjustment = 0; zAdjustment <= 4; ++zAdjustment) {
                if (xAdjustment < 1 || zAdjustment < 1 || xAdjustment > 3 || zAdjustment > 3) {
                    final boolean isBlockBelowSolid = getDucky().world.getBlockState(new BlockPos(startingX + xAdjustment, startingY - 1, startingZ + zAdjustment)).isOpaqueCube();
                    final boolean isBlockEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY, startingZ + zAdjustment));
                    final boolean isBlockAboveEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY + 1, startingZ + zAdjustment));
                    if (isBlockBelowSolid && isBlockEmpty && isBlockAboveEmpty) {
                        getDucky().setLocationAndAngles(startingX + xAdjustment + 0.5F, startingY, startingZ + zAdjustment + 0.5F, getDucky().rotationYaw, getDucky().rotationPitch);
                        return;
                    }
                }
            }
        }
    }
}
