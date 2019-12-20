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
package com.blackducksoftware.integration.minecraft.ducky;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelDucky extends AbstractCommonModel<EntityDucky> {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer tail;

    public ModelDucky() {
        super(64, 32);
        this.head = createNewModelRenderer(0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, 15.0F, -4.0F);
        this.bill = createNewModelRenderer(14, 0, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        this.body = createNewModelRenderer(0, 9, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, 16.0F, 0.0F);
        this.tail = createNewModelRenderer(14, 4, -1.0F, 3.0F, 2.5F, 2, 2, 2, 0.0F, 16.0F, 0.0F);
        this.rightWing = createNewModelRenderer(24, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        this.leftWing = createNewModelRenderer(24, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        this.rightLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        this.leftLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);
    }

    @Override
    public Iterable<ModelRenderer> func_225601_a_() {
        return ImmutableList.of(this.head, this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.bill, this.tail);
    }

    @Override
    public void func_225597_a_(final EntityDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        duplicateModelRotationAngles(head, bill);

        this.body.rotateAngleX = ((float) Math.PI / 2F);
        duplicateModelRotationAngles(body, tail);

        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;

    }

    //    /**
    //     * Sets the models various rotation angles then renders the model.
    //     */
    //    @Override
    //    public void render(final EntityDucky entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
    //        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    //
    //        this.head.render(scale);
    //        this.bill.render(scale);
    //        this.body.render(scale);
    //        this.tail.render(scale);
    //        this.rightLeg.render(scale);
    //        this.leftLeg.render(scale);
    //        this.rightWing.render(scale);
    //        this.leftWing.render(scale);
    //    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(final EntityDucky entityDucky, final float limbSwingAmount, final float ageInTicks, final float partialTickTime) {
        if (entityDucky.isSitting()) {
            setRotationPoint(0.0F, 20.0F, -4.0F, head, bill);
            setRotationPoint(0.0F, 21.0F, 0.0F, tail, body);

            this.rightWing.setRotationPoint(-4.0F, 18.0F, 0.0F);
            this.leftWing.setRotationPoint(4.0F, 18.0F, 0.0F);

            this.rightLeg.setRotationPoint(-2.0F, 18.9F, 1.0F);
            this.leftLeg.setRotationPoint(1.0F, 18.9F, 1.0F);
        } else {
            setRotationPoint(0.0F, 15.0F, -4.0F, head, bill);
            setRotationPoint(0.0F, 16.0F, 0.0F, tail, body);

            this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
            this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);

            this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
            this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
        }
    }

    //    /**
    //     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how "far" arms
    //     * and legs can swing at most.
    //     */
    //    @Override
    //    public void setRotationAngles(final EntityDucky entityDucky, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
    //        this.head.rotateAngleX = headPitch * 0.017453292F;
    //        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    //        duplicateModelRotationAngles(head, bill);
    //
    //        this.body.rotateAngleX = ((float) Math.PI / 2F);
    //        duplicateModelRotationAngles(body, tail);
    //
    //        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    //        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    //        this.rightWing.rotateAngleZ = ageInTicks;
    //        this.leftWing.rotateAngleZ = -ageInTicks;
    //    }
}
