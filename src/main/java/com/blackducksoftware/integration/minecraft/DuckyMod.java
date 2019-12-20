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
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = DuckyMod.MODID)
public class DuckyMod {
    public static final String MODID = "duckymod";

    public DuckyMod() {
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(RegistryEvent.Register<Item> evt) {
            evt.getRegistry().register(
                DuckyModItems.DUCKY_SPAWN_EGG
            );
        }

        @SubscribeEvent
        public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> evt) {
            evt.getRegistry().registerAll(
                DuckyModEntities.DUCKY,
                DuckyModEntities.DUCKY_SPAWN_EGG,
                DuckyModEntities.GIANT_TAMED_DUCKY,
                DuckyModEntities.TAMED_DUCKY);
        }

        @SubscribeEvent
        public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> evt) {
            evt.getRegistry().registerAll(
                DuckyModSounds.duckDeath,
                DuckyModSounds.duckHurt,
                DuckyModSounds.duckQuack);
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class RegistryClient {
        @SubscribeEvent
        public static void onRendererRegistry(FMLClientSetupEvent clientSetupEvent) {
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.GIANT_TAMED_DUCKY, RenderGiantTamedDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.TAMED_DUCKY, RenderTamedDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.DUCKY, RenderDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.DUCKY_SPAWN_EGG, (EntityRendererManager manager) -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        }
    }

}
