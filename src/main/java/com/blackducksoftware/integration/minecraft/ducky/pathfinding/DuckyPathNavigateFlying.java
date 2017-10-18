/**
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.Entity;
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
        this.nodeProcessor.initProcessor(world, ducky);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new DuckyFlyingNodeProcessor();
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
        final DuckyFlyingNodeProcessor nodeProcessor = (DuckyFlyingNodeProcessor) this.nodeProcessor;
        return nodeProcessor.isDirectPathBetweenPoints(entity, currentPosition.x, currentPosition.y, currentPosition.z, targetPosition.x, targetPosition.y, targetPosition.z);
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
