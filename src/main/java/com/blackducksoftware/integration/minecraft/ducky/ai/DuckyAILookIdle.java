/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.ai.goal.LookRandomlyGoal;

public class DuckyAILookIdle extends LookRandomlyGoal {
    /**
     * The entity that is looking idle.
     */
    private final EntityDucky entityDucky;

    public DuckyAILookIdle(final EntityDucky entityDucky) {
        super(entityDucky);
        this.entityDucky = entityDucky;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return !entityDucky.isFlying() && !entityDucky.isAttacking() && super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !entityDucky.isFlying() && !entityDucky.isAttacking() && super.shouldContinueExecuting();
    }
}
