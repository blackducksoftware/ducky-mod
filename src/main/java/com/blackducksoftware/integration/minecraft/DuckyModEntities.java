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
package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.RenderDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.RenderTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.RenderGiantTamedDucky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSprite;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class DuckyModEntities {
    public static final EntityType<EntityGiantTamedDucky> GIANT_TAMED_DUCKY;
    public static final EntityType<EntityTamedDucky> TAMED_DUCKY;
    public static final EntityType<EntityDucky> DUCKY;
    public static final EntityType<EntityDuckySpawnEgg> DUCKY_SPAWN_EGG;

    static {
        GIANT_TAMED_DUCKY = EntityType.Builder.create(EntityGiantTamedDucky.class, EntityGiantTamedDucky::new)
                                .tracker(96, 3, true)
                                .build(EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        GIANT_TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);

        TAMED_DUCKY = EntityType.Builder.create(EntityTamedDucky.class, EntityTamedDucky::new)
                          .tracker(96, 3, true)
                          .build(EntityTamedDucky.TAMED_DUCKY_NAME);
        TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityTamedDucky.TAMED_DUCKY_NAME);

        DUCKY = EntityType.Builder.create(EntityDucky.class, EntityDucky::new)
                    .tracker(96, 3, true)
                    .build(EntityDucky.DUCKY_NAME);
        DUCKY.setRegistryName(DuckyMod.MODID, EntityDucky.DUCKY_NAME);

        DUCKY_SPAWN_EGG = EntityType.Builder.create(EntityDuckySpawnEgg.class, EntityDuckySpawnEgg::new)
                              .tracker(64, 3, true)
                              .build(EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
        DUCKY_SPAWN_EGG.setRegistryName(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
    }

    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGiantTamedDucky.class, RenderGiantTamedDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTamedDucky.class, RenderTamedDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDucky.class, RenderDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDuckySpawnEgg.class, (RenderManager manager) -> new RenderSprite(manager, DuckyModItems.DUCKY_SPAWN_EGG, Minecraft.getInstance().getItemRenderer()));
    }
}
