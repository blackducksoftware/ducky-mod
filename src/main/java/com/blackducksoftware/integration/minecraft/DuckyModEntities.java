package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.RenderDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.RenderTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.RenderGiantTamedDucky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSprite;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class DuckyModEntities {
    public static final EntityType<EntityGiantTamedDucky> GIANT_TAMED_DUCKY;
    public static final EntityType<EntityTamedDucky> TAMED_DUCKY;
    public static final EntityType<EntityDucky> DUCKY;
    public static final EntityType<EntityDuckySpawnEgg> DUCKY_SPAWN_EGG;

    static {
        GIANT_TAMED_DUCKY = EntityType.Builder.create(EntityGiantTamedDucky.class, EntityGiantTamedDucky::new)
                                .tracker(80, 3, true)
                                .build(EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);
        GIANT_TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME);

        TAMED_DUCKY = EntityType.Builder.create(EntityTamedDucky.class, EntityTamedDucky::new)
                          .tracker(80, 3, true)
                          .build(EntityTamedDucky.TAMED_DUCKY_NAME);
        TAMED_DUCKY.setRegistryName(DuckyMod.MODID, EntityTamedDucky.TAMED_DUCKY_NAME);

        DUCKY = EntityType.Builder.create(EntityDucky.class, EntityDucky::new)
                    .tracker(64, 10, true)
                    .build(EntityDucky.DUCKY_NAME);
        DUCKY.setRegistryName(DuckyMod.MODID, EntityDucky.DUCKY_NAME);

        DUCKY_SPAWN_EGG = EntityType.Builder.create(EntityDuckySpawnEgg.class, EntityDuckySpawnEgg::new)
                              .tracker(64, 5, true)
                              .build(EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
        DUCKY_SPAWN_EGG.setRegistryName(DuckyMod.MODID, EntityDuckySpawnEgg.DUCKY_SPAWN_EGG_NAME);
    }

    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGiantTamedDucky.class, RenderGiantTamedDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTamedDucky.class, RenderTamedDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDucky.class, RenderDucky::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDuckySpawnEgg.class, (RenderManager manager) -> new RenderSprite(manager, DuckyModItems.egg, Minecraft.getInstance().getItemRenderer()));
    }
}
