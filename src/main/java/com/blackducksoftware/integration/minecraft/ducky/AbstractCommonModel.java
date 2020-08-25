/**
 * 1.16.1-0.6.0
 *
 * Copyright (c) 2020 Synopsys, Inc.
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
