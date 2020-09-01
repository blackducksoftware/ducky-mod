/**
 * ducky-mod
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

public class DuckyModSounds {
    private static final String DUCK_QUACK_NAME = "duckquack";
    private static final String DUCK_HURT_NAME = "duckhurt";
    private static final String DUCK_DEATH_NAME = "duckdeath";

    public static SoundEvent duckQuack;
    public static SoundEvent duckHurt;
    public static SoundEvent duckDeath;

    static {
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

}
