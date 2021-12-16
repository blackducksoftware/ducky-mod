/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DuckyModEntities {
    public static final EntityType<EntityGiantTamedDucky> GIANT_TAMED_DUCKY;
    public static final EntityType<EntityTamedDucky> TAMED_DUCKY;
    public static final EntityType<EntityDucky> DUCKY;
    public static final EntityType<EntityDuckySpawnEgg> DUCKY_SPAWN_EGG;

    static {
        GIANT_TAMED_DUCKY = EntityType.Builder.<EntityGiantTamedDucky>of(EntityGiantTamedDucky::new, MobCategory.CREATURE)
                                .sized(1.58F, 1.85F)
                                .setTrackingRange(96)
                                .setUpdateInterval(3)
                                .setShouldReceiveVelocityUpdates(true)
                                .build(EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        GIANT_TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        //        GlobalEntityTypeAttributes.put(GIANT_TAMED_DUCKY, EntityGiantTamedDucky.createAttributes().build());

        TAMED_DUCKY = EntityType.Builder.<EntityTamedDucky>of(EntityTamedDucky::new, MobCategory.CREATURE)
                          .sized(0.4F, 0.7F)
                          .setTrackingRange(96)
                          .setUpdateInterval(3)
                          .setShouldReceiveVelocityUpdates(true)
                          .build(EntityTamedDucky.TAMED_DUCKY_NAME);
        TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityTamedDucky.TAMED_DUCKY_NAME);
        //        GlobalEntityTypeAttributes.put(TAMED_DUCKY, EntityTamedDucky.createAttributes().build());

        DUCKY = EntityType.Builder.<EntityDucky>of(EntityDucky::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.7F)
                    .setTrackingRange(96)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(EntityDucky.DUCKY_NAME);
        DUCKY.setRegistryName(DuckyMod.MODID, EntityDucky.DUCKY_NAME);
        //GlobalEntityTypeAttributes.put(DUCKY, EntityDucky.createAttributes().build());

        ResourceLocation location = new ResourceLocation(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
        DUCKY_SPAWN_EGG = EntityType.Builder.<EntityDuckySpawnEgg>of(EntityDuckySpawnEgg::new, MobCategory.MISC)
                              .setTrackingRange(64)
                              .setUpdateInterval(3)
                              .setShouldReceiveVelocityUpdates(true)
                              .build(location.toString());
        //        Item spawnEgg = new SpawnEggItem(DUCKY, 1447446, 15690005, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC));
        DUCKY_SPAWN_EGG.setRegistryName(new ResourceLocation(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME));
    }
}
