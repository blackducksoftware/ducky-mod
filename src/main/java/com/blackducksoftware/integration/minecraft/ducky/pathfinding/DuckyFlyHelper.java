/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
