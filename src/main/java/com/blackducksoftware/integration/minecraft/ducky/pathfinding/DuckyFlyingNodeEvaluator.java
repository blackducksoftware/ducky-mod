/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;

public class DuckyFlyingNodeEvaluator extends FlyNodeEvaluator {

    @Override
    public int getNeighbors(Node[] pathOptions, Node providedTargetPoint) {
        Map<Node, Double> closePathDistances = new HashMap<>();

        int additionalVertical = 0;
        if (this.entityHeight < providedTargetPoint.y) {
            additionalVertical = 1;
        }
        Node targetPoint = providedTargetPoint.cloneAndMove(providedTargetPoint.x, providedTargetPoint.y + additionalVertical, providedTargetPoint.z);

        Node node = this.getNode(targetPoint.x, targetPoint.y, targetPoint.z + 1);
        Node node1 = this.getNode(targetPoint.x - 1, targetPoint.y, targetPoint.z);
        Node node2 = this.getNode(targetPoint.x + 1, targetPoint.y, targetPoint.z);
        Node node3 = this.getNode(targetPoint.x, targetPoint.y, targetPoint.z - 1);
        Node node4 = this.getNode(targetPoint.x, targetPoint.y + 1, targetPoint.z);
        Node node5 = this.getNode(targetPoint.x, targetPoint.y - 1, targetPoint.z);

        addPathNode(node, targetPoint, closePathDistances);
        addPathNode(node1, targetPoint, closePathDistances);
        addPathNode(node2, targetPoint, closePathDistances);
        addPathNode(node3, targetPoint, closePathDistances);
        addPathNode(node4, targetPoint, closePathDistances);
        addPathNode(node5, targetPoint, closePathDistances);

        Double shortestDistance = closePathDistances.values().stream().min(Double::compareTo).orElse(Double.MAX_VALUE);
        for (Node currentNode : closePathDistances.keySet()) {
            Double distance = closePathDistances.get(node);
            if (distance > shortestDistance) {
                currentNode.costMalus++;
            }
        }

        boolean flag = node3 == null || node3.costMalus != 0.0F;
        boolean flag1 = node == null || node.costMalus != 0.0F;
        boolean flag2 = node2 == null || node2.costMalus != 0.0F;
        boolean flag3 = node1 == null || node1.costMalus != 0.0F;
        boolean flag4 = node4 == null || node4.costMalus != 0.0F;
        boolean flag5 = node5 == null || node5.costMalus != 0.0F;
        Map<Node, Double> nextPathDistances = new HashMap<>();
        if (flag && flag3) {
            Node node6 = this.getNode(targetPoint.x - 1, targetPoint.y, targetPoint.z - 1);
            addPathNode(node6, targetPoint, nextPathDistances);
        }

        if (flag && flag2) {
            Node node7 = this.getNode(targetPoint.x + 1, targetPoint.y, targetPoint.z - 1);
            addPathNode(node7, targetPoint, nextPathDistances);
        }

        if (flag1 && flag3) {
            Node node8 = this.getNode(targetPoint.x - 1, targetPoint.y, targetPoint.z + 1);
            addPathNode(node8, targetPoint, nextPathDistances);
        }

        if (flag1 && flag2) {
            Node node9 = this.getNode(targetPoint.x + 1, targetPoint.y, targetPoint.z + 1);
            addPathNode(node9, targetPoint, nextPathDistances);
        }

        if (flag && flag4) {
            Node node10 = this.getNode(targetPoint.x, targetPoint.y + 1, targetPoint.z - 1);
            addPathNode(node10, targetPoint, nextPathDistances);
        }

        if (flag1 && flag4) {
            Node node11 = this.getNode(targetPoint.x, targetPoint.y + 1, targetPoint.z + 1);
            addPathNode(node11, targetPoint, nextPathDistances);
        }

        if (flag2 && flag4) {
            Node node12 = this.getNode(targetPoint.x + 1, targetPoint.y + 1, targetPoint.z);
            addPathNode(node12, targetPoint, nextPathDistances);
        }

        if (flag3 && flag4) {
            Node node13 = this.getNode(targetPoint.x - 1, targetPoint.y + 1, targetPoint.z);
            addPathNode(node13, targetPoint, nextPathDistances);
        }

        if (flag && flag5) {
            Node node14 = this.getNode(targetPoint.x, targetPoint.y - 1, targetPoint.z - 1);
            addPathNode(node14, targetPoint, nextPathDistances);
        }

        if (flag1 && flag5) {
            Node node15 = this.getNode(targetPoint.x, targetPoint.y - 1, targetPoint.z + 1);
            addPathNode(node15, targetPoint, nextPathDistances);
        }

        if (flag2 && flag5) {
            Node node16 = this.getNode(targetPoint.x + 1, targetPoint.y - 1, targetPoint.z);
            addPathNode(node16, targetPoint, nextPathDistances);
        }

        if (flag3 && flag5) {
            Node node17 = this.getNode(targetPoint.x - 1, targetPoint.y - 1, targetPoint.z);
            addPathNode(node17, targetPoint, nextPathDistances);
        }

        Double nextShortestDistance = nextPathDistances.values().stream().min(Double::compareTo).orElse(Double.MAX_VALUE);
        for (Node currentNode : nextPathDistances.keySet()) {
            Double distance = nextPathDistances.get(node);
            if (distance > nextShortestDistance) {
                currentNode.costMalus++;
            }
        }

        int i = 0;
        for (Node currentNode : closePathDistances.keySet()) {
            pathOptions[i++] = currentNode;
        }
        for (Node currentNode : nextPathDistances.keySet()) {
            pathOptions[i++] = currentNode;
        }

        return i;
    }

    private void addPathNode(Node node, Node targetNode, Map<Node, Double> pathDistances) {
        if (node != null && !node.closed) {
            pathDistances.put(node, distanceToTargetSquared(node, targetNode));
        }
    }

    private double distanceToTargetSquared(Node node, Node targetNode) {
        return distanceToTargetSquared(node.x, node.y, node.z, targetNode.x, targetNode.y, targetNode.z);
    }

    private double distanceToTargetSquared(double x, double y, double z, double targetX, double targetY, double targetZ) {
        double xDifference = targetX - x;
        double yDifference = targetY - y;
        double zDifference = targetZ - z;
        double distanceToTargetSquared = xDifference * xDifference + yDifference * yDifference + zDifference * zDifference;
        return distanceToTargetSquared;
    }

}
