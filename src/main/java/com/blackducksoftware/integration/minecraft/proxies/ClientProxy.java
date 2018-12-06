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

import com.blackducksoftware.integration.minecraft.DuckyModItems;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.RenderDucky;
import com.blackducksoftware.integration.minecraft.ducky.RenderDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.RenderTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.RenderGiantTamedDucky;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInitRenders() {
        registerRender(DuckyModItems.egg);

    }

    @Override
    public void registerEntities() {
        super.registerEntities();

        RenderingRegistry.registerEntityRenderingHandler(EntityGiantTamedDucky.class, new IRenderFactory<EntityGiantTamedDucky>() {
            @Override
            public Render<? super EntityGiantTamedDucky> createRenderFor(final RenderManager manager) {
                return new RenderGiantTamedDucky(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityTamedDucky.class, new IRenderFactory<EntityTamedDucky>() {
            @Override
            public Render<? super EntityTamedDucky> createRenderFor(final RenderManager manager) {
                return new RenderTamedDucky(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityDucky.class, new IRenderFactory<EntityDucky>() {
            @Override
            public Render<? super EntityDucky> createRenderFor(final RenderManager manager) {
                return new RenderDucky(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityDuckySpawnEgg.class, new IRenderFactory<EntityDuckySpawnEgg>() {
            @Override
            public Render<? super EntityDuckySpawnEgg> createRenderFor(final RenderManager manager) {
                return new RenderDuckySpawnEgg(manager, DuckyModItems.egg);
            }
        });

    }

    private void registerRender(final Item i, final String name, int... meta) {
        if (meta.length < 1) {
            meta = new int[] { 0 };
        }
        for (final int m : meta) {
            ModelLoader.setCustomModelResourceLocation(i, m, new ModelResourceLocation(name, "inventory"));
        }
    }

    private void registerRender(final Item i, final int... meta) {
        registerRender(i, i.getRegistryName().toString(), meta);
    }

}
