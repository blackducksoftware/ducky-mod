/**
 * 0.6.1-release
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

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.util.math.vector.Vector3d;

public class DuckyFlyHelper extends FlyingMovementController {
    public DuckyFlyHelper(EntityDucky ducky) {
        super(ducky, 20, true);
    }

    @Override
    public void tick() {
        float moveSpeed;
        // MOVEMENT_SPEED Attributes.field_233821_d_
        // FLYING_SPEED Attributes.field_233822_e_
        // onGround() == func_233570_aj_()
        if (this.mob.func_233570_aj_()) {
            moveSpeed = (float) (this.mob.getAttribute(Attributes.field_233821_d_).getValue() * speed);
        } else {
            moveSpeed = (float) (this.mob.getAttribute(Attributes.field_233822_e_).getValue() * speed);
        }
        this.mob.setAIMoveSpeed(moveSpeed);
        if (this.action == FlyingMovementController.Action.MOVE_TO) {
            this.action = FlyingMovementController.Action.WAIT;
            Vector3d targetPosition = new Vector3d(posX, posY, posZ);
            Vector3d vector = targetPosition.subtract(this.mob.getPositionVec());
            vector = vector.normalize().scale(moveSpeed);
            this.mob.setMotion(vector);
        }
    }
}
