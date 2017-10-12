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

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class AbstractCommonModel extends ModelBase {
    private final int textureWidth;
    private final int textureHeight;

    public AbstractCommonModel(final int textureWidth, final int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public ModelRenderer createNewModelRenderer(final int textureOffsetX, final int textureOffsetY, final float boxOffsetX, final float boxOffsetY, final float boxOffsetZ, final int boxWidth, final int boxHeight, final int boxDepth,
            final float rotationPointX, final float rotationPointY, final float rotationPointZ) {
        final ModelRenderer renderer = new ModelRenderer(this, textureOffsetX, textureOffsetY).setTextureSize(textureWidth, textureHeight);
        renderer.addBox(boxOffsetX, boxOffsetY, boxOffsetZ, boxWidth, boxHeight, boxDepth);
        renderer.setRotationPoint(rotationPointX, rotationPointY, rotationPointZ);
        return renderer;
    }

    public void showModelRenderers(final ModelRenderer... renderers) {
        for (final ModelRenderer renderer : renderers) {
            renderer.isHidden = false;
        }
    }

    public void hideModelRenderers(final ModelRenderer... renderers) {
        for (final ModelRenderer renderer : renderers) {
            renderer.isHidden = true;
        }
    }

    public void setRotationPoint(final float xAmount, final float yAmount, final float zAmount, final ModelRenderer... renderers) {
        for (final ModelRenderer renderer : renderers) {
            renderer.setRotationPoint(xAmount, yAmount, zAmount);
        }
    }

    public void duplicateModelRotationAngles(final ModelRenderer rendererToCopy, final ModelRenderer... renderers) {
        for (final ModelRenderer renderer : renderers) {
            renderer.rotateAngleX = rendererToCopy.rotateAngleX;
            renderer.rotateAngleY = rendererToCopy.rotateAngleY;
            renderer.rotateAngleZ = rendererToCopy.rotateAngleZ;
        }
    }
}
