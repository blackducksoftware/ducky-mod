/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public abstract class AbstractCommonModel<T extends Entity> extends SegmentedModel<T> {
    private final int textureWidth;
    private final int textureHeight;

    public AbstractCommonModel(int textureWidth, int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public ModelRenderer createNewModelRenderer(int textureOffsetX, int textureOffsetY, float boxOffsetX, float boxOffsetY, float boxOffsetZ, int boxWidth, int boxHeight, int boxDepth,
        float rotationPointX, float rotationPointY, float rotationPointZ) {
        ModelRenderer renderer = new ModelRenderer(this, textureOffsetX, textureOffsetY).setTextureSize(textureWidth, textureHeight);
        renderer.addBox(boxOffsetX, boxOffsetY, boxOffsetZ, boxWidth, boxHeight, boxDepth, 0.0F);
        renderer.setRotationPoint(rotationPointX, rotationPointY, rotationPointZ);
        return renderer;
    }

    public void showModelRenderers(ModelRenderer... renderers) {
        for (ModelRenderer renderer : renderers) {
            renderer.showModel = true;
        }
    }

    public void hideModelRenderers(ModelRenderer... renderers) {
        for (ModelRenderer renderer : renderers) {
            renderer.showModel = false;
        }
    }

    public void setRotationPoint(float xAmount, float yAmount, float zAmount, ModelRenderer... renderers) {
        for (ModelRenderer renderer : renderers) {
            renderer.setRotationPoint(xAmount, yAmount, zAmount);
        }
    }

    public void duplicateModelRotationAngles(ModelRenderer rendererToCopy, ModelRenderer... renderers) {
        for (ModelRenderer renderer : renderers) {
            renderer.rotateAngleX = rendererToCopy.rotateAngleX;
            renderer.rotateAngleY = rendererToCopy.rotateAngleY;
            renderer.rotateAngleZ = rendererToCopy.rotateAngleZ;
        }
    }
}
