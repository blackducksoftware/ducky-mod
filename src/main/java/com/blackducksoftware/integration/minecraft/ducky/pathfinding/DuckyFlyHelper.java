/**
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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

import com.blackducksoftware.integration.minecraft.ducky.BaseEntityDucky;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyHelper extends EntityMoveHelper {
    public DuckyFlyHelper(final BaseEntityDucky ducky) {
        super(ducky);
    }

    @Override
    public void tick() {
        if (action == EntityMoveHelper.Action.MOVE_TO) {
            action = EntityMoveHelper.Action.WAIT;
            final Vec3d targetPosition = new Vec3d(posX, posY + 1, posZ);
            Vec3d vector = targetPosition.subtract(entity.getPositionVector());
            vector = vector.normalize().scale(entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).getValue() * speed);
            entity.motionX = vector.x;
            entity.motionY = vector.y + 0.1F;
            entity.motionZ = vector.z;
        }
    }
}
