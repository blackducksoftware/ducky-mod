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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class DuckyModSounds {
    private static final String DUCK_QUACK_NAME = "duckquack";
    private static final String DUCK_HURT_NAME = "duckhurt";
    private static final String DUCK_DEATH_NAME = "duckdeath";

    public static SoundEvent duckQuack;
    public static SoundEvent duckHurt;
    public static SoundEvent duckDeath;

    public static void mainRegistry() {
        initSounds();
        final IForgeRegistry<SoundEvent> registry = GameRegistry.findRegistry(SoundEvent.class);

        registerSound(registry, duckQuack);
        registerSound(registry, duckHurt);
        registerSound(registry, duckDeath);
    }

    private static void initSounds() {
        final ResourceLocation duckQuackResource = new ResourceLocation(DuckyMod.MODID, DUCK_QUACK_NAME);
        duckQuack = new SoundEvent(duckQuackResource);
        duckQuack.setRegistryName(DuckyMod.MODID, DUCK_QUACK_NAME);

        final ResourceLocation duckHurtResource = new ResourceLocation(DuckyMod.MODID, DUCK_HURT_NAME);
        duckHurt = new SoundEvent(duckHurtResource);
        duckHurt.setRegistryName(DuckyMod.MODID, DUCK_HURT_NAME);

        final ResourceLocation duckDeathResource = new ResourceLocation(DuckyMod.MODID, DUCK_DEATH_NAME);
        duckDeath = new SoundEvent(duckDeathResource);
        duckDeath.setRegistryName(DuckyMod.MODID, DUCK_DEATH_NAME);
    }

    private static void registerSound(final IForgeRegistry<SoundEvent> registry, final SoundEvent soundEvent) {
        registry.register(soundEvent);
    }

}
