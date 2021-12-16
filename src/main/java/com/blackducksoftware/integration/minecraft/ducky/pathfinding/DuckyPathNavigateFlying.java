/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.PathFinder;

public class DuckyPathNavigateFlying extends FlyingPathNavigation {
    public DuckyPathNavigateFlying(EntityDucky ducky, Level world) {
        super(ducky, world);
    }

    @Override
    protected PathFinder createPathFinder(int var) {
        this.nodeEvaluator = new DuckyFlyingNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);

        BlockPos currentPosition = this.mob.getOnPos();
        BlockPos blockPosCorner = new BlockPos(currentPosition).offset(-32, -32, -32);
        BlockPos blockPosOppositeCorner = new BlockPos(currentPosition).offset(32, 32, 32);
        PathNavigationRegion region = new PathNavigationRegion(this.level, blockPosCorner, blockPosOppositeCorner);

        this.nodeEvaluator.prepare(region, this.mob);
        return new PathFinder(this.nodeEvaluator, var);
    }

}
