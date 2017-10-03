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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.ai.EntityAIWander;

public class DuckyAIWander extends EntityAIWander {
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
    public boolean continueExecuting() {
        return entityDucky.canMove() && !entityDucky.isFlying() && !entityDucky.isAttacking() && super.continueExecuting();
    }
}
