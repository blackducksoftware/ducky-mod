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
package com.blackducksoftware.integration.minecraft.ducky;

import com.blackducksoftware.integration.minecraft.DuckyMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDuckySpawnEgg extends RenderSnowball {

    public RenderDuckySpawnEgg(final RenderManager renderManager, final Item egg) {
        super(renderManager, egg, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return new ResourceLocation(DuckyMod.MODID + ":" + "textures/items/" + ItemDuckySpawnEgg.DUCKY_EGG_NAME + ".png");
    }

}
