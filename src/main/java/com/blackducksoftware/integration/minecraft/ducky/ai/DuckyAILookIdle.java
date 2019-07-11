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
