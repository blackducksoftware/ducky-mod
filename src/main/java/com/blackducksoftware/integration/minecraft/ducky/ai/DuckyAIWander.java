/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.ai.goal.RandomWalkingGoal;

public class DuckyAIWander extends RandomWalkingGoal {
    private final EntityDucky entityDucky;

    public DuckyAIWander(final EntityDucky entityDucky, final double speedIn) {
        super(entityDucky, speedIn);
        this.entityDucky = entityDucky;
    }

    public DuckyAIWander(final EntityDucky entityDucky, final double speedIn, final int chance) {
        super(entityDucky, speedIn, chance);
        this.entityDucky = entityDucky;
    }

    @Override
    public boolean shouldExecute() {
        return entityDucky.canMove() && !entityDucky.isFlying() && !entityDucky.isAttacking() && super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return entityDucky.canMove() && !entityDucky.isFlying() && !entityDucky.isAttacking() && super.shouldContinueExecuting();
    }
}
