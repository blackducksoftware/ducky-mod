/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class DuckyModSounds {
    private static final String DUCK_QUACK_NAME = "duckquack";
    private static final String DUCK_HURT_NAME = "duckhurt";
    private static final String DUCK_DEATH_NAME = "duckdeath";

    public static SoundEvent duckQuack;
    public static SoundEvent duckHurt;
    public static SoundEvent duckDeath;

    static {
        ResourceLocation duckQuackResource = new ResourceLocation(DuckyMod.MODID, DUCK_QUACK_NAME);
        duckQuack = new SoundEvent(duckQuackResource);
        duckQuack.setRegistryName(DuckyMod.MODID, DUCK_QUACK_NAME);

        ResourceLocation duckHurtResource = new ResourceLocation(DuckyMod.MODID, DUCK_HURT_NAME);
        duckHurt = new SoundEvent(duckHurtResource);
        duckHurt.setRegistryName(DuckyMod.MODID, DUCK_HURT_NAME);

        ResourceLocation duckDeathResource = new ResourceLocation(DuckyMod.MODID, DUCK_DEATH_NAME);
        duckDeath = new SoundEvent(duckDeathResource);
        duckDeath.setRegistryName(DuckyMod.MODID, DUCK_DEATH_NAME);
    }

}
