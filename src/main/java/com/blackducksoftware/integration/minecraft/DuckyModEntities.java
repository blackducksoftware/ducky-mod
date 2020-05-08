/**
 * ducky-mod
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
package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

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

        TAMED_DUCKY = EntityType.Builder.<EntityTamedDucky>create(EntityTamedDucky::new, EntityClassification.CREATURE)
                          .size(0.4F, 0.7F)
                          .setTrackingRange(96)
                          .setUpdateInterval(3)
                          .setShouldReceiveVelocityUpdates(true)
                          .build(EntityTamedDucky.TAMED_DUCKY_NAME);
        TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityTamedDucky.TAMED_DUCKY_NAME);

        DUCKY = EntityType.Builder.<EntityDucky>create(EntityDucky::new, EntityClassification.CREATURE)
                    .size(0.4F, 0.7F)
                    .setTrackingRange(96)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(EntityDucky.DUCKY_NAME);
        DUCKY.setRegistryName(DuckyMod.MODID, EntityDucky.DUCKY_NAME);

        DUCKY_SPAWN_EGG = EntityType.Builder.<EntityDuckySpawnEgg>create(EntityDuckySpawnEgg::new, EntityClassification.MISC)
                              .setTrackingRange(64)
                              .setUpdateInterval(3)
                              .setShouldReceiveVelocityUpdates(true)
                              .build(EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
        DUCKY_SPAWN_EGG.setRegistryName(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
    }
}
