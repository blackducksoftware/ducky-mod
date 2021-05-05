/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Region;
import net.minecraft.world.World;

public class DuckyPathNavigateFlying extends FlyingPathNavigator {
    public DuckyPathNavigateFlying(EntityDucky ducky, World world) {
        super(ducky, world);
    }

    @Override
    protected PathFinder getPathFinder(int var) {
        this.nodeProcessor = new DuckyFlyingNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);

        BlockPos blockPosCorner = new BlockPos(this.entity.getPositionVec()).add(-32, -32, -32);
        BlockPos blockPosOppositeCorner = new BlockPos(this.entity.getPositionVec().add(32, 32, 32));
        Region region = new Region(this.world, blockPosCorner, blockPosOppositeCorner);

        //func_225578_a_ == init
        this.nodeProcessor.func_225578_a_(region, this.entity);
        return new PathFinder(this.nodeProcessor, var);
    }

}
