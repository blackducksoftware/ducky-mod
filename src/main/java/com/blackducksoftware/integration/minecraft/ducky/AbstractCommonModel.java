/**
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
