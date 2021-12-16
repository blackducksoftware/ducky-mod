/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTamedDucky extends MobRenderer<EntityTamedDucky, ModelTamedDucky> {
    public RenderTamedDucky(EntityRendererProvider.Context context) {
        super(context, new ModelTamedDucky(), 0.4F);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityTamedDucky entity) {
        return new ResourceLocation(DuckyMod.MODID, "textures/entity/" + EntityTamedDucky.TAMED_DUCKY_NAME + ".png");
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityTamedDucky ducky, float partialTicks) {
        float f = ducky.oFlap + (ducky.wingRotation - ducky.oFlap) * partialTicks;
        float f1 = ducky.oFlapSpeed + (ducky.destPos - ducky.oFlapSpeed) * partialTicks;
        return (Mth.sin(f) + 1.0F) * f1;
    }

}
