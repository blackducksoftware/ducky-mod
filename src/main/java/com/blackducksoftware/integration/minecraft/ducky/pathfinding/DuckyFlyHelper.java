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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyHelper extends EntityFlyHelper {
    public DuckyFlyHelper(final EntityDucky ducky) {
        super(ducky);
    }

    @Override
    public void tick() {
        if (action == EntityMoveHelper.Action.MOVE_TO) {
            action = EntityMoveHelper.Action.WAIT;
            float moveSpeed = (float) (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).getValue() * speed);
            this.entity.setAIMoveSpeed(moveSpeed);
            final Vec3d targetPosition = new Vec3d(posX, posY + 1, posZ);
            Vec3d vector = targetPosition.subtract(entity.getPositionVector());
            vector = vector.normalize().scale(moveSpeed);
            entity.motionX = vector.x;
            entity.motionY = vector.y + 0.1F;
            entity.motionZ = vector.z;
        } else {
            float moveSpeed;
            if (this.entity.onGround) {
                moveSpeed = (float) (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).getValue() * speed);
            } else {
                moveSpeed = (float) (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * speed);
            }
            this.entity.setAIMoveSpeed(moveSpeed);
        }
    }
}
