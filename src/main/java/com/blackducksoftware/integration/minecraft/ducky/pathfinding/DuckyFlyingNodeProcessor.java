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
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyingNodeProcessor extends FlyingNodeProcessor {
    public boolean isDirectPathBetweenPoints(final LivingEntity entity, final double currentPositionX, final double currentPositionY, final double currentPositionZ, final double targetPositionX, final double targetPositionY,
        final double targetPositionZ) {
        final Vec3d currentPosition = new Vec3d(currentPositionX, currentPositionY, currentPositionZ);
        final Vec3d targetPosition = new Vec3d(targetPositionX, targetPositionY, targetPositionZ);
        return isDirectPathBetweenPoints(entity, currentPosition, targetPosition);
    }

    public boolean isDirectPathBetweenPoints(final LivingEntity entity, final Vec3d currentPosition, final Vec3d targetPosition) {
        final double xDifference = targetPosition.x - currentPosition.x;
        final double yDifference = targetPosition.y - currentPosition.y;
        final double zDifference = targetPosition.z - currentPosition.z;
        final double distanceToTargetSquared = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;

        if (distanceToTargetSquared < 1) {
            return true;
        } else {
            final Vec3d vectorToTarget = targetPosition.subtract(currentPosition);
            final double increment = 1 / distanceToTargetSquared;
            for (double f = 0.0D; f < 1; f = f + increment) {
                final Vec3d vectorToAdd = new Vec3d(vectorToTarget.x * f, vectorToTarget.y * f, vectorToTarget.z * f);
                final Vec3d currentAdjusted = currentPosition.add(vectorToAdd);
                final BlockPos position = new BlockPos(currentAdjusted.x, currentAdjusted.y, currentAdjusted.z);

                final BlockState iblockstate = entity.world.getBlockState(position);
                if (iblockstate.getMaterial() != Material.AIR || iblockstate.isSolid()) {
                    return false;
                }
            }
            return true;
        }
    }

    //    @Override
    //    public int func_222859_a(final PathPoint[] pathOptions, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
    //        if (isDirectPathBetweenPoints(entity, currentPoint.x, currentPoint.y, currentPoint.z, targetPoint.x, targetPoint.y, targetPoint.z)) {
    //            int i = 0;
    //            final Vec3d currentPosition = new Vec3d(currentPoint.x, currentPoint.y, currentPoint.z);
    //            final Vec3d targetPosition = new Vec3d(targetPoint.x, targetPoint.y, targetPoint.z);
    //            Vec3d vector = targetPosition.subtract(currentPosition);
    //            vector = vector.normalize().scale(1.5);
    //            final Vec3d pathPointVector = currentPosition.add(vector);
    //            final PathPoint pathpoint = this.openPoint((int) pathPointVector.x, (int) pathPointVector.y, (int) pathPointVector.z);
    //            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
    //                pathOptions[i++] = pathpoint;
    //            }
    //            return i;
    //        } else {
    //            return super.findPathOptions(pathOptions, currentPoint, targetPoint, maxDistance);
    //        }
    //    }

}
