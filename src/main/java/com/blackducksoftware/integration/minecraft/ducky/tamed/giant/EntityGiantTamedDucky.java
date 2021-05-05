/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
