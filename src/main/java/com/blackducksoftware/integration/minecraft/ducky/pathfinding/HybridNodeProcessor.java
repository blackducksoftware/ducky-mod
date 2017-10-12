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

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.world.IBlockAccess;

public class HybridNodeProcessor extends NodeProcessor {

    @Override
    public PathPoint getStart() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PathPoint getPathPointToCoords(final double x, final double y, final double z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z, final EntityLiving entitylivingIn, final int xSize, final int ySize, final int zSize, final boolean canBreakDoorsIn,
            final boolean canEnterDoorsIn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z) {
        // TODO Auto-generated method stub
        return null;
    }

}
