/**
 * Copyright (C) 2019 Synopsys, Inc.
 * https://www.synopsys.com/
 *
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.BaseEntityDucky;

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
    protected final static double TELEPORT_RANGE = 24.0D;

    private final BaseEntityDucky ducky;
    private Entity targetToFollow;
    protected int attackTick;
    protected final int attackInterval = 20;
    protected float oldWaterCost;
    protected double distanceToTarget;
    protected double attackReach;
    protected int stuckTick;
    protected Vec3d lastPostion = null;

    public AbstractDuckyMoveAttack(final BaseEntityDucky ducky) {
        this.ducky = ducky;
    }

    public BaseEntityDucky getDucky() {
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
        return getTargetToFollow().isAlive();
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
        final PathNavigate navigator = ducky.getGroundNavigator();
        Path path = navigator.getPath();

        boolean shouldFly = false;
        if (path == null) {
            path = navigator.getPathToEntityLiving(target);
        }
        if (path != null) {
            final PathPoint pathpoint = path.getFinalPathPoint();
            if (pathpoint == null) {
                shouldFly = true;
            } else {
                final int i = MathHelper.floor(target.posY) - pathpoint.y;
                shouldFly = !target.onGround || (i > 1.5D);
            }
        } else {
            shouldFly = true;
        }
        if (shouldFly) {
            navigator.clearPath();
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

    protected boolean relocateDuckyNearTarget() {
        final int startingX = MathHelper.floor(getTargetToFollow().posX) - 2;
        final int startingZ = MathHelper.floor(getTargetToFollow().posZ) - 2;
        final int startingY = MathHelper.floor(getTargetToFollow().getBoundingBox().minY);

        // Search a 4x4 area around the target (at least 2 blocks away from the target) for a 2 high empty spot on a solid block to put Ducky
        for (int xAdjustment = 0; xAdjustment <= 4; ++xAdjustment) {
            for (int zAdjustment = 0; zAdjustment <= 4; ++zAdjustment) {
                if (xAdjustment < 1 || zAdjustment < 1 || xAdjustment > 3 || zAdjustment > 3) {
                    final BlockPos pos = new BlockPos(startingX + xAdjustment, startingY - 1, startingZ + zAdjustment);
                    final boolean isBlockBelowSolid = getDucky().world.getBlockState(pos).isTopSolid();
                    final boolean isBlockEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY, startingZ + zAdjustment));
                    final boolean isBlockAboveEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY + 1, startingZ + zAdjustment));
                    if (isBlockBelowSolid && isBlockEmpty && isBlockAboveEmpty) {
                        getDucky().setLocationAndAngles(startingX + xAdjustment + 0.5F, startingY, startingZ + zAdjustment + 0.5F, getDucky().rotationYaw, getDucky().rotationPitch);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockPos getPositionBelowTarget() {
        BlockPos location = null;
        if (getTargetToFollow().onGround) {
            location = new BlockPos(getTargetToFollow().posX, getTargetToFollow().posY, getTargetToFollow().posZ);
        } else {
            for (double i = getTargetToFollow().posY; i > 0.0D; i = i - 1.0D) {
                final BlockPos currentLocation = new BlockPos(getTargetToFollow().posX, i, getTargetToFollow().posZ);
                final IBlockState blockstate = getDucky().world.getBlockState(currentLocation);
                if (blockstate.getMaterial() != Material.AIR || blockstate.isFullCube()) {
                    location = currentLocation;
                    break;
                }
            }
        }
        return location;
    }
}
