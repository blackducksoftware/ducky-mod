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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.Vec3d;

public class DuckyFlyHelper extends EntityMoveHelper {

    public DuckyFlyHelper(final EntityDucky ducky) {
        super(ducky);
    }

    @Override
    public void onUpdateMoveHelper() {
        if (action == EntityMoveHelper.Action.MOVE_TO) {
            action = EntityMoveHelper.Action.WAIT;
            final Vec3d targetPosition = new Vec3d(posX, posY, posZ);
            Vec3d vector = targetPosition.subtract(entity.getPositionVector());
            vector = vector.normalize().scale(entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.field_193334_e).getAttributeValue() * speed);
            entity.motionX = vector.xCoord;
            final float yAdjustment = 0.1F;
            entity.motionY = vector.yCoord + yAdjustment;
            entity.motionZ = vector.zCoord;
        }
    }
}
