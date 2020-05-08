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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyHelper extends FlyingMovementController {
    public DuckyFlyHelper(EntityDucky ducky) {
        super(ducky, 20, true);
    }

    @Override
    public void tick() {
        float moveSpeed;
        if (this.mob.onGround) {
            moveSpeed = (float) (this.mob.getAttributes().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * speed);
        } else {
            moveSpeed = (float) (this.mob.getAttributes().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).getValue() * speed);
        }
        this.mob.setAIMoveSpeed(moveSpeed);
        if (this.action == FlyingMovementController.Action.MOVE_TO) {
            this.action = FlyingMovementController.Action.WAIT;
            Vec3d targetPosition = new Vec3d(posX, posY, posZ);
            Vec3d vector = targetPosition.subtract(this.mob.getPositionVector());
            vector = vector.normalize().scale(moveSpeed);
            this.mob.setMotion(vector);
        }
    }
}
