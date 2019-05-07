/**
 * Copyright (C) 2018 Black Duck Software, Inc.
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
package com.blackducksoftware.integration.minecraft.ducky;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemDuckySpawnEgg extends Item {
    public static final String DUCKY_EGG_NAME = "ducky_spawn_egg";

    public ItemDuckySpawnEgg() {
        super(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemStack = player.getHeldItem(hand);
        if (!player.abilities.isCreativeMode) {
            itemStack.shrink(1);
        }
        worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            final EntityDuckySpawnEgg entityegg = new EntityDuckySpawnEgg(worldIn, player);
            entityegg.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entityegg);
        }
        player.addStat(StatList.ITEM_USED.get(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
