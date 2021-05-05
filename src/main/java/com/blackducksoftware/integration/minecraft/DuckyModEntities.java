/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

public class DuckyModEntities {
    public static final EntityType<EntityGiantTamedDucky> GIANT_TAMED_DUCKY;
    public static final EntityType<EntityTamedDucky> TAMED_DUCKY;
    public static final EntityType<EntityDucky> DUCKY;
    public static final EntityType<EntityDuckySpawnEgg> DUCKY_SPAWN_EGG;

    static {
        GIANT_TAMED_DUCKY = EntityType.Builder.<EntityGiantTamedDucky>create(EntityGiantTamedDucky::new, EntityClassification.CREATURE)
                                .size(1.58F, 1.85F)
                                .setTrackingRange(96)
                                .setUpdateInterval(3)
                                .setShouldReceiveVelocityUpdates(true)
                                .build(EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        GIANT_TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        GlobalEntityTypeAttributes.put(GIANT_TAMED_DUCKY, EntityGiantTamedDucky.registerAttributes().func_233813_a_());

        TAMED_DUCKY = EntityType.Builder.<EntityTamedDucky>create(EntityTamedDucky::new, EntityClassification.CREATURE)
                          .size(0.4F, 0.7F)
                          .setTrackingRange(96)
                          .setUpdateInterval(3)
                          .setShouldReceiveVelocityUpdates(true)
                          .build(EntityTamedDucky.TAMED_DUCKY_NAME);
        TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityTamedDucky.TAMED_DUCKY_NAME);
        GlobalEntityTypeAttributes.put(TAMED_DUCKY, EntityTamedDucky.registerAttributes().func_233813_a_());

        DUCKY = EntityType.Builder.<EntityDucky>create(EntityDucky::new, EntityClassification.CREATURE)
                    .size(0.4F, 0.7F)
                    .setTrackingRange(96)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(EntityDucky.DUCKY_NAME);
        DUCKY.setRegistryName(DuckyMod.MODID, EntityDucky.DUCKY_NAME);
        GlobalEntityTypeAttributes.put(DUCKY, EntityDucky.registerAttributes().func_233813_a_());

        DUCKY_SPAWN_EGG = EntityType.Builder.<EntityDuckySpawnEgg>create(EntityDuckySpawnEgg::new, EntityClassification.MISC)
                              .setTrackingRange(64)
                              .setUpdateInterval(3)
                              .setShouldReceiveVelocityUpdates(true)
                              .build(EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
        DUCKY_SPAWN_EGG.setRegistryName(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
    }
}
