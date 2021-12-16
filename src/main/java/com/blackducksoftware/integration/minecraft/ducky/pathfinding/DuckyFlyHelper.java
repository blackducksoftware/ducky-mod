/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class DuckyFlyHelper extends FlyingMoveControl {
    public DuckyFlyHelper(EntityDucky ducky) {
        super(ducky, 20, true);
    }

    @Override
    public void tick() {
        float moveSpeed;
        if (this.mob.isOnGround()) {
            moveSpeed = (float) (this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * getSpeedModifier());
        } else {
            moveSpeed = (float) (this.mob.getAttribute(Attributes.FLYING_SPEED).getValue() * getSpeedModifier());
        }
        this.mob.setSpeed(moveSpeed);
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            Vec3 targetPosition = new Vec3(getWantedX(), getWantedY(), getWantedZ());
            Vec3 vector = targetPosition.subtract(this.mob.position());
            vector = vector.normalize().scale(moveSpeed);
            this.mob.setDeltaMovement(vector);
        }
    }
}
