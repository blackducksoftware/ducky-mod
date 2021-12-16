/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import java.util.Random;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemDuckySpawnEgg extends Item {
    public static final String DUCKY_EGG_NAME = "ducky_spawn_egg";

    public ItemDuckySpawnEgg() {
        super(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MISC));
        this.setRegistryName(DuckyMod.MODID, DUCKY_EGG_NAME);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        Random random = new Random();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (level.isClientSide()) {
            EntityDuckySpawnEgg entityegg = new EntityDuckySpawnEgg(level, player);
            entityegg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(entityegg);
        }
        //        player.addStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.success(itemStack);
    }
}
