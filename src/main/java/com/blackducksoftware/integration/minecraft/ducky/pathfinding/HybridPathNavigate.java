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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HybridPathNavigate extends PathNavigate {

    public HybridPathNavigate(final EntityLiving entitylivingIn, final World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder getPathFinder() {
        // TODO
        return null;
    }

    @Override
    protected boolean canNavigate() {
        return getCanSwim() && this.isInLiquid() || !this.theEntity.isRiding();
    }

    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
    }

    @Override
    public Path getPathToEntityLiving(final Entity entityIn) {
        return this.getPathToPos(new BlockPos(entityIn));
    }

    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d posVec31, final Vec3d posVec32, final int sizeX, final int sizeY, final int sizeZ) {
        // TODO
        return false;
    }

    @Override
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.worldObj.getBlockState(pos).isSideSolid(this.worldObj, pos, EnumFacing.UP);
    }

    @Override
    public void onUpdateNavigation() {
        // TODO

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

    public void setAvoidSun(final boolean avoidSun) {
        // TODO
    }

}
