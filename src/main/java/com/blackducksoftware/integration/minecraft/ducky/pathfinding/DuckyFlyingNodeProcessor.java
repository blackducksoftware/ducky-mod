/**
 * ducky-mod
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.pathfinding.PathPoint;

public class DuckyFlyingNodeProcessor extends FlyingNodeProcessor {

    @Override
    public int func_222859_a(PathPoint[] pathOptions, PathPoint providedTargetPoint) {
        Map<PathPoint, Double> closePathDistances = new HashMap<>();

        int additionalVertical = 0;
        if (this.entity.getPosY() < providedTargetPoint.y) {
            additionalVertical = 1;
        }
        PathPoint targetPoint = providedTargetPoint.cloneMove(providedTargetPoint.x, providedTargetPoint.y + additionalVertical, providedTargetPoint.z);

        PathPoint pathpoint = this.openPoint(targetPoint.x, targetPoint.y, targetPoint.z + 1);
        PathPoint pathpoint1 = this.openPoint(targetPoint.x - 1, targetPoint.y, targetPoint.z);
        PathPoint pathpoint2 = this.openPoint(targetPoint.x + 1, targetPoint.y, targetPoint.z);
        PathPoint pathpoint3 = this.openPoint(targetPoint.x, targetPoint.y, targetPoint.z - 1);
        PathPoint pathpoint4 = this.openPoint(targetPoint.x, targetPoint.y + 1, targetPoint.z);
        PathPoint pathpoint5 = this.openPoint(targetPoint.x, targetPoint.y - 1, targetPoint.z);

        addPathPoint(pathpoint, targetPoint, closePathDistances);
        addPathPoint(pathpoint1, targetPoint, closePathDistances);
        addPathPoint(pathpoint2, targetPoint, closePathDistances);
        addPathPoint(pathpoint3, targetPoint, closePathDistances);
        addPathPoint(pathpoint4, targetPoint, closePathDistances);
        addPathPoint(pathpoint5, targetPoint, closePathDistances);

        Double shortestDistance = closePathDistances.values().stream().min(Double::compareTo).orElse(Double.MAX_VALUE);
        for (PathPoint pathPoint : closePathDistances.keySet()) {
            Double distance = closePathDistances.get(pathPoint);
            if (distance > shortestDistance) {
                pathPoint.costMalus++;
            }
        }

        boolean flag = pathpoint3 == null || pathpoint3.costMalus != 0.0F;
        boolean flag1 = pathpoint == null || pathpoint.costMalus != 0.0F;
        boolean flag2 = pathpoint2 == null || pathpoint2.costMalus != 0.0F;
        boolean flag3 = pathpoint1 == null || pathpoint1.costMalus != 0.0F;
        boolean flag4 = pathpoint4 == null || pathpoint4.costMalus != 0.0F;
        boolean flag5 = pathpoint5 == null || pathpoint5.costMalus != 0.0F;
        Map<PathPoint, Double> nextPathDistances = new HashMap<>();
        if (flag && flag3) {
            PathPoint pathpoint6 = this.openPoint(targetPoint.x - 1, targetPoint.y, targetPoint.z - 1);
            addPathPoint(pathpoint6, targetPoint, nextPathDistances);
        }

        if (flag && flag2) {
            PathPoint pathpoint7 = this.openPoint(targetPoint.x + 1, targetPoint.y, targetPoint.z - 1);
            addPathPoint(pathpoint7, targetPoint, nextPathDistances);
        }

        if (flag1 && flag3) {
            PathPoint pathpoint8 = this.openPoint(targetPoint.x - 1, targetPoint.y, targetPoint.z + 1);
            addPathPoint(pathpoint8, targetPoint, nextPathDistances);
        }

        if (flag1 && flag2) {
            PathPoint pathpoint9 = this.openPoint(targetPoint.x + 1, targetPoint.y, targetPoint.z + 1);
            addPathPoint(pathpoint9, targetPoint, nextPathDistances);
        }

        if (flag && flag4) {
            PathPoint pathpoint10 = this.openPoint(targetPoint.x, targetPoint.y + 1, targetPoint.z - 1);
            addPathPoint(pathpoint10, targetPoint, nextPathDistances);
        }

        if (flag1 && flag4) {
            PathPoint pathpoint11 = this.openPoint(targetPoint.x, targetPoint.y + 1, targetPoint.z + 1);
            addPathPoint(pathpoint11, targetPoint, nextPathDistances);
        }

        if (flag2 && flag4) {
            PathPoint pathpoint12 = this.openPoint(targetPoint.x + 1, targetPoint.y + 1, targetPoint.z);
            addPathPoint(pathpoint12, targetPoint, nextPathDistances);
        }

        if (flag3 && flag4) {
            PathPoint pathpoint13 = this.openPoint(targetPoint.x - 1, targetPoint.y + 1, targetPoint.z);
            addPathPoint(pathpoint13, targetPoint, nextPathDistances);
        }

        if (flag && flag5) {
            PathPoint pathpoint14 = this.openPoint(targetPoint.x, targetPoint.y - 1, targetPoint.z - 1);
            addPathPoint(pathpoint14, targetPoint, nextPathDistances);
        }

        if (flag1 && flag5) {
            PathPoint pathpoint15 = this.openPoint(targetPoint.x, targetPoint.y - 1, targetPoint.z + 1);
            addPathPoint(pathpoint15, targetPoint, nextPathDistances);
        }

        if (flag2 && flag5) {
            PathPoint pathpoint16 = this.openPoint(targetPoint.x + 1, targetPoint.y - 1, targetPoint.z);
            addPathPoint(pathpoint16, targetPoint, nextPathDistances);
        }

        if (flag3 && flag5) {
            PathPoint pathpoint17 = this.openPoint(targetPoint.x - 1, targetPoint.y - 1, targetPoint.z);
            addPathPoint(pathpoint17, targetPoint, nextPathDistances);
        }

        Double nextShortestDistance = nextPathDistances.values().stream().min(Double::compareTo).orElse(Double.MAX_VALUE);
        for (PathPoint pathPoint : nextPathDistances.keySet()) {
            Double distance = nextPathDistances.get(pathPoint);
            if (distance > nextShortestDistance) {
                pathPoint.costMalus++;
            }
        }

        int i = 0;
        for (PathPoint pathPoint : closePathDistances.keySet()) {
            pathOptions[i++] = pathPoint;
        }
        for (PathPoint pathPoint : nextPathDistances.keySet()) {
            pathOptions[i++] = pathPoint;
        }

        return i;
    }

    private void addPathPoint(PathPoint pathPoint, PathPoint targetPoint, Map<PathPoint, Double> pathDistances) {
        if (pathPoint != null && !pathPoint.visited) {
            pathDistances.put(pathPoint, distanceToTargetSquared(pathPoint, targetPoint));
        }
    }

    private double distanceToTargetSquared(PathPoint pathPoint, PathPoint targetPoint) {
        return distanceToTargetSquared(pathPoint.x, pathPoint.y, pathPoint.z, targetPoint.x, targetPoint.y, targetPoint.z);
    }

    private double distanceToTargetSquared(double x, double y, double z, double targetX, double targetY, double targetZ) {
        double xDifference = targetX - x;
        double yDifference = targetY - y;
        double zDifference = targetZ - z;
        double distanceToTargetSquared = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;
        return distanceToTargetSquared;
    }

}
