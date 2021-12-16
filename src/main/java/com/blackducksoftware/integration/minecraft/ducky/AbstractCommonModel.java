/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.Entity;

public abstract class AbstractCommonModel<T extends Entity> extends AgeableListModel<T> {

    public AbstractCommonModel() {
    }

    public static PartDefinition addNewBodyPart(PartDefinition def, String partName, int textureOffsetX, int textureOffsetY, float boxOffsetX, float boxOffsetY, float boxOffsetZ, int boxWidth, int boxHeight, int boxDepth,
        float rotationPointX, float rotationPointY, float rotationPointZ) {
        PartDefinition partDefinition = def.addOrReplaceChild(partName,
            CubeListBuilder.create().texOffs(textureOffsetX, textureOffsetY)
                .addBox(boxOffsetX, boxOffsetY, boxOffsetZ, boxWidth, boxHeight, boxDepth),
            PartPose.offset(rotationPointX, rotationPointY, rotationPointZ));
        return partDefinition;
    }

    //    public void showModelRenderers(ModelRenderer... renderers) {
    //        for (ModelRenderer renderer : renderers) {
    //            renderer.showModel = true;
    //        }
    //    }
    //
    //    public void hideModelRenderers(ModelRenderer... renderers) {
    //        for (ModelRenderer renderer : renderers) {
    //            renderer.showModel = false;
    //        }
    //    }
    //
    //    public void setRotationPoint(float xAmount, float yAmount, float zAmount, ModelRenderer... renderers) {
    //        for (ModelRenderer renderer : renderers) {
    //            renderer.setRotationPoint(xAmount, yAmount, zAmount);
    //        }
    //    }
    //
    //    public void duplicateModelRotationAngles(ModelRenderer rendererToCopy, ModelRenderer... renderers) {
    //        for (ModelRenderer renderer : renderers) {
    //            renderer.rotateAngleX = rendererToCopy.rotateAngleX;
    //            renderer.rotateAngleY = rendererToCopy.rotateAngleY;
    //            renderer.rotateAngleZ = rendererToCopy.rotateAngleZ;
    //        }
    //    }
}
