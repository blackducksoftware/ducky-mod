/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractDuckyMoveAttack extends Goal {
    protected final static double TELEPORT_RANGE = 24.0D;

    private final EntityDucky ducky;
    private LivingEntity targetToFollow;
    protected int attackTick;
    protected float oldWaterCost;
    protected double distanceToTarget;
    protected double attackReach;
    protected int stuckTick;
    protected Vec3 lastPostion = null;

    public AbstractDuckyMoveAttack(EntityDucky ducky) {
        this.ducky = ducky;
    }

    public EntityDucky getDucky() {
        return ducky;
    }

    public LivingEntity getTargetToFollow() {
        return targetToFollow;
    }

    public void setTargetToFollow(LivingEntity targetToFollow) {
        this.targetToFollow = targetToFollow;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.oldWaterCost = getDucky().getPathfindingMalus(BlockPathTypes.WATER);
        getDucky().setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        this.targetToFollow = null;
        getDucky().setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
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
        BlockState blockstate = getDucky().level.getBlockState(pos);
        FluidState fluidstate = getDucky().level.getFluidState(pos);
        return blockstate.isAir() ? true : fluidstate.isEmpty();
    }

    public boolean needToFly(Entity target) {
        if (target == null) {
            return false;
        }
        GroundPathNavigation navigator = ducky.getGroundNavigator();
        Path path = navigator.getPath();

        boolean shouldFly;
        if (path == null) {
            path = navigator.createPath(target, 0);
        }
        if (path != null) {
            Node node = path.getEndNode();
            if (node == null) {
                shouldFly = true;
            } else {
                double i = Math.floor(target.getY()) - node.y;
                shouldFly = !target.isOnGround() || (i > 1.5D);
            }
        } else {
            shouldFly = true;
        }
        if (shouldFly) {
            navigator.stop();
        }
        return shouldFly;
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return 1 + ducky.getBbWidth() * 2.0F * ducky.getBbWidth() * 2.0F + (target.getBbWidth() / 4);
    }

    protected void checkAndPerformAttack(LivingEntity target, double distance) {
        if (target == null) {
            return;
        }
        double attackReach = this.getAttackReachSqr(target);
        if (distance <= attackReach && this.attackTick <= 0 && ducky.canAttack(target, TargetingConditions.forCombat())) {
            this.attackTick = 20;
            ducky.swing(InteractionHand.MAIN_HAND);
            //            ducky.attackEntityAsMob(target);
            ducky.setTarget(target);
            ducky.setAttacking(true);
        }
    }

    protected boolean isDuckyStuck() {
        if (lastPostion == null) {
            lastPostion = getDucky().getPosition(0);
        } else if (stuckTick > 20) {
            stuckTick = 0;
            if (lastPostion.distanceToSqr(getDucky().getPosition(0)) < 2.25D) {
                return true;
            }
            lastPostion = getDucky().getPosition(0);
        }
        return false;
    }

    protected boolean relocateDuckyNearTarget() {
        double startingX = Math.floor(getTargetToFollow().getX()) - 2;
        double startingZ = Math.floor(getTargetToFollow().getZ()) - 2;
        double startingY = Math.floor(getTargetToFollow().getBoundingBox().minY);

        // Search a 4x4 area around the target (at least 2 blocks away from the target) for a 2 high empty spot on a solid block to put Ducky
        for (int xAdjustment = 0; xAdjustment <= 4; ++xAdjustment) {
            for (int zAdjustment = 0; zAdjustment <= 4; ++zAdjustment) {
                if (xAdjustment < 1 || zAdjustment < 1 || xAdjustment > 3 || zAdjustment > 3) {
                    BlockPos pos = new BlockPos(startingX + xAdjustment, startingY - 1, startingZ + zAdjustment);
                    boolean isBlockBelowSolid = getDucky().level.getBlockState(pos).entityCanStandOn(getDucky().level, pos, getDucky());
                    boolean isBlockEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY, startingZ + zAdjustment));
                    boolean isBlockAboveEmpty = this.isEmptyBlock(new BlockPos(startingX + xAdjustment, startingY + 1, startingZ + zAdjustment));
                    if (isBlockBelowSolid && isBlockEmpty && isBlockAboveEmpty) {
                        BlockPos position = new BlockPos(startingX + xAdjustment + 0.5F, startingY, startingZ + zAdjustment + 0.5F);
                        Vec2 pitchAndYaw = getDucky().getRotationVector();
                        float pitch = pitchAndYaw.x;
                        float yaw = pitchAndYaw.y;
                        getDucky().moveTo(position, yaw, pitch);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockPos getPositionBelowTarget() {
        BlockPos location = null;
        if (getTargetToFollow().isOnGround()) {
            location = new BlockPos(getTargetToFollow().getX(), getTargetToFollow().getY(), getTargetToFollow().getZ());
        } else {
            for (double i = getTargetToFollow().getY(); i > 0.0D; i = i - 1.0D) {
                BlockPos currentLocation = new BlockPos(getTargetToFollow().getX(), i, getTargetToFollow().getZ());
                BlockState blockstate = getDucky().level.getBlockState(currentLocation);
                if (blockstate.getMaterial() != Material.AIR || getDucky().level.getBlockState(currentLocation).entityCanStandOn(getDucky().level, currentLocation, getDucky())) {
                    location = currentLocation;
                    break;
                }
            }
        }
        return location;
    }
}
