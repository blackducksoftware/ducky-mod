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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityGiantTamedDucky extends EntityTamedDucky {
    public static final String TAMED_GIANT_DUCKY_NAME = "tamed_giant_bd_ducky";

    public EntityGiantTamedDucky(Level level) {
        super(DuckyModEntities.GIANT_TAMED_DUCKY, level);
    }

    public EntityGiantTamedDucky(EntityType<? extends EntityGiantTamedDucky> type, Level level) {
        super(type, level);
    }

    // registerAttributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                   .add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
                   .add(Attributes.FLYING_SPEED, BASE_SPEED)
                   .add(Attributes.FOLLOW_RANGE, 64.0D)
                   .add(Attributes.MAX_HEALTH, GIANT_HEALTH)
                   .add(Attributes.ATTACK_DAMAGE, TAMED_DAMAGE);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(GIANT_HEALTH);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) GIANT_HEALTH);
        }
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperienceReward(Player player) {
        return 30;
    }
}
