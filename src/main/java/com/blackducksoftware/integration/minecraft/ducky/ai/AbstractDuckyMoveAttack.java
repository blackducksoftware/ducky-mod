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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractDuckyMoveAttack extends Goal {
    protected final static double TELEPORT_RANGE = 24.0D;

    private final EntityDucky ducky;
    private Entity targetToFollow;
    protected int attackTick;
    protected float oldWaterCost;
    protected double distanceToTarget;
    protected double attackReach;
    protected int stuckTick;
    protected Vec3d lastPostion = null;

    public AbstractDuckyMoveAttack(EntityDucky ducky) {
        this.ducky = ducky;
    }

    public EntityDucky getDucky() {
        return ducky;
    }

    public Entity getTargetToFollow() {
        return targetToFollow;
    }

    public void setTargetToFollow(Entity targetToFollow) {
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

    protected boolean updateCalc(double distanceToTarget) {
        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.stuckTick++;
        return getTargetToFollow().isAlive();
    }

    protected double getSpeedModifier(double distanceToTarget) {
        double speedModifier = 1.0D;
        if (attackReach > 0 && distanceToTarget < attackReach * 4) {
            speedModifier = 0.75D;
        }
        return speedModifier;
    }

    public boolean isEmptyBlock(BlockPos pos) {
        BlockState blockstate = getDucky().world.getBlockState(pos);
        return blockstate.getMaterial() == Material.AIR ? true : !blockstate.isSolid();
    }

    public boolean needToFly(Entity target) {
        if (target == null) {
            return false;
        }
        GroundPathNavigator navigator = ducky.getGroundNavigator();
        Path path = navigator.getPath();

        boolean shouldFly = false;
        if (path == null) {
            path = navigator.getPathToEntityLiving(target, 0);
        }
        if (path != null) {
            PathPoint pathpoint = path.getFinalPathPoint();
            if (pathpoint == null) {
                shouldFly = true;
            } else {
                int i = MathHelper.floor(target.getPosition().getY()) - pathpoint.y;
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

    protected double getAttackReachSqr(Entity target) {

        return 1 + ducky.getWidth() * 2.0F * ducky.getWidth() * 2.0F + (target.getWidth() / 4);
    }

    protected void checkAndPerformAttack(Entity target, double distance) {
        if (target == null) {
            return;
        }
        double attackReach = this.getAttackReachSqr(target);
        if (distance <= attackReach && this.attackTick <= 0) {
            this.attackTick = 20;
            ducky.swingArm(Hand.MAIN_HAND);
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
        int startingX = MathHelper.floor(getTargetToFollow().getPosition().getX()) - 2;
        int startingZ = MathHelper.floor(getTargetToFollow().getPosition().getZ()) - 2;
        int startingY = MathHelper.floor(getTargetToFollow().getBoundingBox().minY);

        // Search a 4x4 area around the target (at least 2 blocks away from the target) for a 2 high empty spot on a solid block to put Ducky
        for (int xAdjustment = 0; xAdjustment <= 4; ++xAdjustment) {
            for (int zAdjustment = 0; zAdjustment <= 4; ++zAdjustment) {
                if (xAdjustment < 1 || zAdjustment < 1 || xAdjustment > 3 || zAdjustment > 3) {
                    BlockPos pos = new BlockPos(startingX + xAdjustment, startingY - 1, startingZ + zAdjustment);
                    boolean isBlockBelowSolid = getDucky().world.getBlockState(pos).func_215682_a(getDucky().world, pos, getDucky());
                    boolean isBlockEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY, startingZ + zAdjustment));
                    boolean isBlockAboveEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY + 1, startingZ + zAdjustment));
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
            location = new BlockPos(getTargetToFollow().getPosition().getX(), getTargetToFollow().getPosition().getY(), getTargetToFollow().getPosition().getZ());
        } else {
            for (double i = getTargetToFollow().getPosition().getY(); i > 0.0D; i = i - 1.0D) {
                BlockPos currentLocation = new BlockPos(getTargetToFollow().getPosition().getX(), i, getTargetToFollow().getPosition().getZ());
                BlockState blockstate = getDucky().world.getBlockState(currentLocation);
                if (blockstate.getMaterial() != Material.AIR || blockstate.func_215682_a(getDucky().world, currentLocation, getDucky())) {
                    location = currentLocation;
                    break;
                }
            }
        }
        return location;
    }
}
