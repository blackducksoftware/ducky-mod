/**
 * 0.6.1-release
 *
 * Copyright (c) 2020 Synopsys, Inc.
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
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.DuckyModEntities;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EntityGiantTamedDucky extends EntityTamedDucky {
    public static final String TAMED_GIANT_DUCKY_NAME = "tamed_giant_bd_ducky";

    public EntityGiantTamedDucky(World worldIn) {
        super(DuckyModEntities.GIANT_TAMED_DUCKY, worldIn);
    }

    public EntityGiantTamedDucky(EntityType<? extends EntityGiantTamedDucky> type, World worldIn) {
        super(type, worldIn);
    }

    // registerAttributes
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                   .func_233815_a_(Attributes.field_233818_a_, GIANT_HEALTH)
                   .func_233815_a_(Attributes.field_233821_d_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233822_e_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233819_b_, 64.0D)
                   .func_233815_a_(Attributes.field_233823_f_, TAMED_DAMAGE);
    }

    //    @Override
    //    protected void registerAttributes() {
    //        super.registerAttributes();
    //        // MAX_HEALTH Attributes.field_233818_a_
    //        // ATTACK_DAMAGE Attributes.field_233823_f_
    //        this.getAttribute(Attributes.field_233818_a_).setBaseValue(GIANT_HEALTH);
    //        this.getAttribute(Attributes.field_233823_f_).setBaseValue(TAMED_DAMAGE);
    //    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            // MAX_HEALTH Attributes.field_233818_a_
            // ATTACK_DAMAGE Attributes.field_233823_f_
            this.getAttribute(Attributes.field_233818_a_).setBaseValue(GIANT_HEALTH);
            this.getAttribute(Attributes.field_233823_f_).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) GIANT_HEALTH);
        }
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 30;
    }
}
