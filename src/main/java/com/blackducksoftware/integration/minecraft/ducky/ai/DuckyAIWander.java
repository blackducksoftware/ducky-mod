/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class DuckyAIWander extends RandomStrollGoal {
    private final EntityDucky entityDucky;

    public DuckyAIWander(EntityDucky entityDucky, double speedIn) {
        super(entityDucky, speedIn);
        this.entityDucky = entityDucky;
    }

    public DuckyAIWander(EntityDucky entityDucky, double speedIn, int chance) {
        super(entityDucky, speedIn, chance);
        this.entityDucky = entityDucky;
    }

    @Override
    public boolean canUse() {
        return entityDucky.canMove() && !entityDucky.isFlying() && !entityDucky.isAttacking() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return entityDucky.canMove() && !entityDucky.isFlying() && !entityDucky.isAttacking() && super.canContinueToUse();
    }
}
