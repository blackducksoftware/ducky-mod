/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemDuckySpawnEgg extends Item {
    public static final String DUCKY_EGG_NAME = "ducky_spawn_egg";

    public ItemDuckySpawnEgg() {
        super(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC));
        this.setRegistryName(DuckyMod.MODID, DUCKY_EGG_NAME);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (!player.abilities.isCreativeMode) {
            itemStack.shrink(1);
        }
        worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityDuckySpawnEgg entityegg = new EntityDuckySpawnEgg(worldIn, player);
            // entityegg.shoot == entityegg.func_234612_a_
            entityegg.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.addEntity(entityegg);
        }
        player.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }
}
