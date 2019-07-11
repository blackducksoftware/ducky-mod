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

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DuckyMod.MODID)
public class DuckyMod {
    public static final String MODID = "duckymod";

    public DuckyMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent evt) {
        DuckyModEntities.registerEntityRenders();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> evt) {
            evt.getRegistry().registerAll(
                DuckyModItems.DUCKY_SPAWN_EGG
            );
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> evt) {
            evt.getRegistry().registerAll(
                DuckyModEntities.DUCKY,
                DuckyModEntities.DUCKY_SPAWN_EGG,
                DuckyModEntities.GIANT_TAMED_DUCKY,
                DuckyModEntities.TAMED_DUCKY);
        }

        @SubscribeEvent
        public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> evt) {
            evt.getRegistry().registerAll(
                DuckyModSounds.duckDeath,
                DuckyModSounds.duckHurt,
                DuckyModSounds.duckQuack);
        }
    }

}
