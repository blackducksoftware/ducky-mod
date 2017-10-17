/**
 * Copyright (C) 2017 Black Duck Software, Inc.
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
package com.blackducksoftware.integration.minecraft;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.RegistryNamespaced;

public class DuckyModSounds {
    private static final String DUCK_QUACK_FILE_NAME = "duckquack";
    private static final String DUCK_HURT_FILE_NAME = "duckhurt";
    private static final String DUCK_DEATH_FILE_NAME = "duckdeath";

    public static ResourceLocation duckQuackResource;
    public static ResourceLocation duckHurtResource;
    public static ResourceLocation duckDeathResource;

    public static SoundEvent duckQuack;
    public static SoundEvent duckHurt;
    public static SoundEvent duckDeath;

    private static int soundEventId = 0;

    public static void mainRegistry() {
        initSounds();
        final RegistryNamespaced<ResourceLocation, SoundEvent> registry = SoundEvent.REGISTRY;

        registerSound(registry, duckQuack, duckQuackResource);
        registerSound(registry, duckHurt, duckHurtResource);
        registerSound(registry, duckDeath, duckDeathResource);
    }

    private static void initSounds() {
        duckQuackResource = new ResourceLocation(DuckyMod.MODID, DUCK_QUACK_FILE_NAME);
        duckQuack = new SoundEvent(duckQuackResource);

        duckHurtResource = new ResourceLocation(DuckyMod.MODID, DUCK_HURT_FILE_NAME);
        duckHurt = new SoundEvent(duckHurtResource);

        duckDeathResource = new ResourceLocation(DuckyMod.MODID, DUCK_DEATH_FILE_NAME);
        duckDeath = new SoundEvent(duckDeathResource);
    }

    private static void registerSound(final RegistryNamespaced<ResourceLocation, SoundEvent> registry, final SoundEvent soundEvent, final ResourceLocation resourceLocation) {
        registry.register(soundEventId++, resourceLocation, soundEvent);
    }

}
