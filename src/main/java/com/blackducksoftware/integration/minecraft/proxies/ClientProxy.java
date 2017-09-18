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
package com.blackducksoftware.integration.minecraft.proxies;

import com.blackducksoftware.integration.minecraft.DuckyModItems;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.RenderDucky;
import com.blackducksoftware.integration.minecraft.ducky.RenderDuckySpawnEgg;

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
