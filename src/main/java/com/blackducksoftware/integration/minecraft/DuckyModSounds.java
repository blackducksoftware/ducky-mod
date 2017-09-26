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
