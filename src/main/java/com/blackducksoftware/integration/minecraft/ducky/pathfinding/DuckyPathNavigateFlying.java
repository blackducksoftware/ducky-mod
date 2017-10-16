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
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DuckyPathNavigateFlying extends PathNavigate {
    public DuckyPathNavigateFlying(final EntityDucky ducky, final World world) {
        super(ducky, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new FlyingNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
    protected boolean canNavigate() {
        return this.getCanSwim() && this.isInLiquid() || !entity.isRiding();
    }

    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(entity.posX, entity.posY, entity.posZ);
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    @Override
    public Path getPathToEntityLiving(final Entity entityIn) {
        return this.getPathToPos(new BlockPos(entityIn));
    }

    @Override
    public void onUpdateNavigation() {
        ++this.totalTicks;

        if (this.tryUpdatePath) {
            this.updatePath();
        }

        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3d vec3d = this.currentPath.getVectorFromIndex(entity, this.currentPath.getCurrentPathIndex());

                if (MathHelper.floor(entity.posX) == MathHelper.floor(vec3d.x) && MathHelper.floor(entity.posY) == MathHelper.floor(vec3d.y) && MathHelper.floor(entity.posZ) == MathHelper.floor(vec3d.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.debugPathFinding();

            if (!this.noPath()) {
                final Vec3d vec3d1 = this.currentPath.getPosition(entity);
                entity.getMoveHelper().setMoveTo(vec3d1.x, vec3d1.y, vec3d1.z, this.speed);
            }
        }
    }

    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d currentPosition, final Vec3d targetPosition, final int sizeX, final int sizeY, final int sizeZ) {
        final double xDifference = targetPosition.x - currentPosition.x;
        final double yDifference = targetPosition.y - currentPosition.y;
        final double zDifference = targetPosition.z - currentPosition.z;
        final double lengthToTarget = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;

        if (lengthToTarget < 1.0E-8D) {
            return false;
        } else {
            final Vec3d vectorToTarget = targetPosition.subtract(currentPosition);
            final double increment = 1 / lengthToTarget;
            for (double f = 0.0D; f < 1; f = f + increment) {
                final Vec3d vectorToAdd = new Vec3d(vectorToTarget.x * f, vectorToTarget.y * f, vectorToTarget.z * f);
                final Vec3d currentAdjusted = currentPosition.add(vectorToAdd);
                final BlockPos position = new BlockPos(currentAdjusted.x, currentAdjusted.y, currentAdjusted.z);

                final IBlockState iblockstate = entity.world.getBlockState(position);
                if (iblockstate.getMaterial() != Material.AIR || iblockstate.isFullCube()) {
                    return false;
                }
            }
            return true;
        }
    }

    public void setCanEnterDoors(final boolean canEnterDoors) {
        this.nodeProcessor.setCanEnterDoors(canEnterDoors);
    }

    public void setCanSwim(final boolean canSwim) {
        this.nodeProcessor.setCanSwim(canSwim);
    }

    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }

    @Override
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.world.getBlockState(pos).isSideSolid(this.world, pos, EnumFacing.UP);
    }
}
