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
