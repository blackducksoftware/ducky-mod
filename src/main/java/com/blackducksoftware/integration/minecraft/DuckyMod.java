/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.RenderDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.RenderTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.RenderGiantTamedDucky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = DuckyMod.MODID)
public class DuckyMod {
    public static final String MODID = "duckymod";

    public DuckyMod() {
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(RegistryEvent.Register<Item> evt) {
            evt.getRegistry().register(
                DuckyModItems.DUCKY_SPAWN_EGG
            );
        }

        @SubscribeEvent
        public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> evt) {
            evt.getRegistry().registerAll(
                DuckyModEntities.DUCKY,
                DuckyModEntities.DUCKY_SPAWN_EGG,
                DuckyModEntities.GIANT_TAMED_DUCKY,
                DuckyModEntities.TAMED_DUCKY);
        }

        @SubscribeEvent
        public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> evt) {
            evt.getRegistry().registerAll(
                DuckyModSounds.duckDeath,
                DuckyModSounds.duckHurt,
                DuckyModSounds.duckQuack);
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class RegistryClient {
        @SubscribeEvent
        public static void onRendererRegistry(FMLClientSetupEvent clientSetupEvent) {
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.GIANT_TAMED_DUCKY, RenderGiantTamedDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.TAMED_DUCKY, RenderTamedDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.DUCKY, RenderDucky::new);
            RenderingRegistry.registerEntityRenderingHandler(DuckyModEntities.DUCKY_SPAWN_EGG, (EntityRendererManager manager) -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));

            EntityRenderers.register(DuckyModEntities.TAMED_DUCKY, RenderTamedDucky::new);
            EntityRenderers.register(DuckyModEntities.GIANT_TAMED_DUCKY, RenderGiantTamedDucky::new);
            EntityRenderers.register(DuckyModEntities.DUCKY, RenderDucky::new);

            //            RenderingRegistry.registerLayerDefinition(PENGUIN_LAYER, PenguinModel::createBodyLayer);

        }
    }

}
