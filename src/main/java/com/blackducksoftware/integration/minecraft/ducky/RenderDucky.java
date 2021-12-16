/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Mth;

public class RenderDucky extends MobRenderer<EntityDucky, ModelDucky> {

    public RenderDucky(EntityRendererProvider.Context context) {
        super(context, new ModelDucky(context.bakeLayer()), 0.4F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    public ResourceLocation getTextureLocation(EntityDucky ducky) {
        return new ResourceLocation(DuckyMod.MODID, "textures/entity/" + EntityDucky.DUCKY_NAME + ".png");
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityDucky ducky, float partialTicks) {
        float f = ducky.oFlap + (ducky.wingRotation - ducky.oFlap) * partialTicks;
        float f1 = ducky.oFlapSpeed + (ducky.destPos - ducky.oFlapSpeed) * partialTicks;
        return (Mth.sin(f) + 1.0F) * f1;
    }

}
