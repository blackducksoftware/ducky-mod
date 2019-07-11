/**
 * Copyright (C) 2019 Synopsys, Inc.
 * https://www.synopsys.com/
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
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderGiantTamedDucky extends MobRenderer<EntityGiantTamedDucky, ModelGiantTamedDucky> {
    public RenderGiantTamedDucky(final EntityRendererManager manager) {
        super(manager, new ModelGiantTamedDucky(), 1.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(final EntityGiantTamedDucky ducky) {
        return new ResourceLocation(DuckyMod.MODID, "textures/entity/" + EntityGiantTamedDucky.TAMED_GIANT_DUCKY_NAME + ".png");
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(final EntityGiantTamedDucky ducky, final float partialTicks) {
        final float f = ducky.oFlap + (ducky.wingRotation - ducky.oFlap) * partialTicks;
        final float f1 = ducky.oFlapSpeed + (ducky.destPos - ducky.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

}
