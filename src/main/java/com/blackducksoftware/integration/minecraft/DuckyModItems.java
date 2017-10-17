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

import com.blackducksoftware.integration.minecraft.ducky.ItemDuckySpawnEgg;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class DuckyModItems {

    public static Item egg;

    public static void mainRegistry() {
        initItems();
        final IForgeRegistry<Item> itemRegistry = GameRegistry.findRegistry(Item.class);
        registerItem(itemRegistry, egg, ItemDuckySpawnEgg.DUCKY_EGG_NAME);
    }

    private static void initItems() {
        egg = new ItemDuckySpawnEgg();
    }

    private static void registerItem(final IForgeRegistry<Item> itemRegistry, final Item item, final String name) {
        item.setUnlocalizedName(name).setRegistryName(DuckyMod.MODID, name);
        itemRegistry.register(item);
    }

}
