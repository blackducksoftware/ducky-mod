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
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityGiantTamedDucky extends EntityTamedDucky {
    public static final String TAMED_GIANT_DUCKY_NAME = "tamed_giant_bd_ducky";

    public EntityGiantTamedDucky(final World worldIn) {
        super(worldIn);
        this.setSize(1.58F, 1.85F);
        this.setScale(1.0F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GIANT_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
    }

    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        }
    }
}
