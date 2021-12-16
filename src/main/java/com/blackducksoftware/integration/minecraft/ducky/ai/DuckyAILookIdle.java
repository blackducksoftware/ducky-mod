/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class DuckyAILookIdle extends RandomLookAroundGoal {
    private final EntityDucky entityDucky;

    public DuckyAILookIdle(EntityDucky entityDucky) {
        super(entityDucky);
        this.entityDucky = entityDucky;
    }

    @Override
    public boolean canUse() {
        return !entityDucky.isFlying() && !entityDucky.isAttacking() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !entityDucky.isFlying() && !entityDucky.isAttacking() && super.canContinueToUse();
    }
}
