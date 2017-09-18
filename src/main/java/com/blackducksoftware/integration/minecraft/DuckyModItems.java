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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DuckyModItems {

    public static Item egg;

    public static void mainRegistry() {
        initItems();

        // registerWithItemBlock(bomToolBlock, bomToolItemBlock, BlockBomTool.BOM_TOOL_NAME);

        register(egg, ItemDuckySpawnEgg.DUCKY_EGG_NAME);
    }

    private static void initItems() {
        egg = new ItemDuckySpawnEgg();
    }

    private static void register(final Item item, final String name) {
        item.setUnlocalizedName(name).setRegistryName(DuckyMod.MODID, name);
        GameRegistry.register(item);
    }

    private static void registerWithItemBlock(final Block block, final ItemBlock itemBlock, final String name) {
        register(block, name);
        itemBlock.setUnlocalizedName(name).setRegistryName(DuckyMod.MODID, name);
        GameRegistry.register(itemBlock);
    }

    private static void register(final Block block, final String name) {
        block.setUnlocalizedName(name).setRegistryName(DuckyMod.MODID, name);
        GameRegistry.register(block);
    }

}
