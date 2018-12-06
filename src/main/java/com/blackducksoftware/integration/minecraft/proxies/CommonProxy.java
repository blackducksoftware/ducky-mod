/**
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
package com.blackducksoftware.integration.minecraft.proxies;

import com.blackducksoftware.integration.minecraft.DuckyMod;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.ItemDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
    protected static int entityCount;

    public void preInitRenders() {

    }

    public void initEvents() {
    }

    public void registerEntities() {
        entityCount = 0;
        register(EntityGiantTamedDucky.class, EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        register(EntityTamedDucky.class, EntityTamedDucky.TAMED_DUCKY_NAME);
        register(EntityDucky.class, EntityDucky.DUCKY_NAME);
        register(EntityDuckySpawnEgg.class, ItemDuckySpawnEgg.DUCKY_EGG_NAME);
    }

    /** registers the entity **/
    protected static void register(final Class entityClass, final String name) {

        EntityRegistry.registerModEntity(new ResourceLocation(DuckyMod.MODID + ":" + name), entityClass, name, ++entityCount, DuckyMod.instance, 16 * 4, 3, true);
    }
}
