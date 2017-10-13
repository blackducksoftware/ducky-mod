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
        return this.getCanSwim() && this.isInLiquid() || !this.theEntity.isRiding();
    }

    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
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
                final Vec3d vec3d = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());

                if (MathHelper.floor_double(this.theEntity.posX) == MathHelper.floor_double(vec3d.xCoord) && MathHelper.floor_double(this.theEntity.posY) == MathHelper.floor_double(vec3d.yCoord)
                        && MathHelper.floor_double(this.theEntity.posZ) == MathHelper.floor_double(vec3d.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.func_192876_m();

            if (!this.noPath()) {
                final Vec3d vec3d1 = this.currentPath.getPosition(this.theEntity);
                this.theEntity.getMoveHelper().setMoveTo(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, this.speed);
            }
        }
    }

    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d currentPosition, final Vec3d targetPosition, final int sizeX, final int sizeY, final int sizeZ) {
        final double xDifference = targetPosition.xCoord - currentPosition.xCoord;
        final double yDifference = targetPosition.yCoord - currentPosition.yCoord;
        final double zDifference = targetPosition.zCoord - currentPosition.zCoord;
        final double lengthToTarget = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;

        if (lengthToTarget < 1.0E-8D) {
            return false;
        } else {
            final Vec3d vectorToTarget = targetPosition.subtract(currentPosition);
            final double increment = 1 / lengthToTarget;
            for (double f = 0.0D; f < 1; f = f + increment) {
                final Vec3d vectorToAdd = new Vec3d(vectorToTarget.xCoord * f, vectorToTarget.yCoord * f, vectorToTarget.zCoord * f);
                final Vec3d currentAdjusted = currentPosition.add(vectorToAdd);
                final BlockPos position = new BlockPos(currentAdjusted.xCoord, currentAdjusted.yCoord, currentAdjusted.zCoord);

                final IBlockState iblockstate = this.theEntity.worldObj.getBlockState(position);
                if (iblockstate.getMaterial() != Material.AIR || iblockstate.isFullCube()) {
                    return false;
                }
            }
            return true;
        }
    }

    public void setCanBreakDoors(final boolean canBreakDoors) {
        this.nodeProcessor.setCanBreakDoors(canBreakDoors);
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
        return this.worldObj.getBlockState(pos).isSideSolid(this.worldObj, pos, EnumFacing.UP);
    }
}
