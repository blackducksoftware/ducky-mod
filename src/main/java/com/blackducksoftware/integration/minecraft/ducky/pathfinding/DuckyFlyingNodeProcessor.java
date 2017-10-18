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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyingNodeProcessor extends FlyingNodeProcessor {
    public boolean isDirectPathBetweenPoints(final Vec3d currentPosition, final Vec3d targetPosition, final int sizeX, final int sizeY, final int sizeZ) {
        final double xDifference = targetPosition.x - currentPosition.x;
        final double yDifference = targetPosition.y - currentPosition.y;
        final double zDifference = targetPosition.z - currentPosition.z;
        final double distanceToTargetSquared = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;

        if (distanceToTargetSquared < 1.0E-8D) {
            return false;
        } else {
            final Vec3d vectorToTarget = targetPosition.subtract(currentPosition);
            final double increment = 1 / distanceToTargetSquared;
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
}