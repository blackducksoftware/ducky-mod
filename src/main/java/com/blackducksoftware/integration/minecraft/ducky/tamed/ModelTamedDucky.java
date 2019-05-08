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
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import com.blackducksoftware.integration.minecraft.ducky.AbstractCommonModel;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelTamedDucky extends AbstractCommonModel {
    public ModelRenderer head;
    public ModelRenderer hatTop;
    public ModelRenderer hatBottom;

    public ModelRenderer fireProofHatTop;
    public ModelRenderer fireProofHatBottom;

    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    public ModelRenderer fastRightLeg;
    public ModelRenderer fastLeftLeg;

    public ModelRenderer rightWing;
    public ModelRenderer leftWing;

    public ModelRenderer flyingRightWing;
    public ModelRenderer flyingLeftWing;

    public ModelRenderer bill;

    public ModelRenderer strongBill;

    public ModelRenderer tail;

    public ModelTamedDucky() {
        super(64, 32);

        head = createNewModelRenderer(0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, 15.0F, -4.0F);
        hatTop = createNewModelRenderer(40, 1, -2.5F, -7.0F, -2.0F, 5, 1, 4, 0.0F, 15.0F, -4.0F);
        hatBottom = createNewModelRenderer(39, 0, -2.5F, -6.0F, -3.0F, 5, 1, 5, 0.0F, 15.0F, -4.0F);

        fireProofHatTop = createNewModelRenderer(40, 8, -2.5F, -7.0F, -2.0F, 5, 1, 4, 0.0F, 15.0F, -4.0F);
        fireProofHatBottom = createNewModelRenderer(39, 7, -2.5F, -6.0F, -3.0F, 5, 1, 5, 0.0F, 15.0F, -4.0F);

        bill = createNewModelRenderer(14, 0, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        strongBill = createNewModelRenderer(12, 24, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        body = createNewModelRenderer(0, 9, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, 16.0F, 0.0F);
        tail = createNewModelRenderer(14, 4, -1.0F, 3.0F, 2.5F, 2, 2, 2, 0.0F, 16.0F, 0.0F);
        rightWing = createNewModelRenderer(24, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        leftWing = createNewModelRenderer(24, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        flyingRightWing = createNewModelRenderer(39, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        flyingLeftWing = createNewModelRenderer(39, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        rightLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        leftLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);

        fastRightLeg = createNewModelRenderer(0, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        fastLeftLeg = createNewModelRenderer(0, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);

        hideModelRenderers(fireProofHatTop, fireProofHatBottom, flyingLeftWing, flyingRightWing, strongBill, fastLeftLeg, fastRightLeg);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final EntityDucky entityDucky = (EntityDucky) entityIn;

        if (entityDucky.isFireProof()) {
            showModelRenderers(fireProofHatTop, fireProofHatBottom);
            hideModelRenderers(hatTop, hatBottom);
        } else {
            showModelRenderers(hatTop, hatBottom);
            hideModelRenderers(fireProofHatTop, fireProofHatBottom);
        }

        if (entityDucky.isCanFly()) {
            showModelRenderers(flyingLeftWing, flyingRightWing);
            hideModelRenderers(rightWing, leftWing);
        } else {
            showModelRenderers(rightWing, leftWing);
            hideModelRenderers(flyingLeftWing, flyingRightWing);
        }

        if (entityDucky.isStrong()) {
            showModelRenderers(strongBill);
            hideModelRenderers(bill);
        } else {
            showModelRenderers(bill);
            hideModelRenderers(strongBill);
        }

        if (entityDucky.isFast()) {
            showModelRenderers(fastLeftLeg, fastRightLeg);
            hideModelRenderers(rightLeg, leftLeg);
        } else {
            showModelRenderers(rightLeg, leftLeg);
            hideModelRenderers(fastLeftLeg, fastRightLeg);
        }

        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        head.render(scale);
        hatTop.render(scale);
        hatBottom.render(scale);

        fireProofHatTop.render(scale);
        fireProofHatBottom.render(scale);

        bill.render(scale);

        strongBill.render(scale);

        body.render(scale);
        tail.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);

        fastLeftLeg.render(scale);
        fastRightLeg.render(scale);

        rightWing.render(scale);
        leftWing.render(scale);

        flyingLeftWing.render(scale);
        flyingRightWing.render(scale);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwingAmount, final float ageInTicks, final float partialTickTime) {
        final EntityDucky entityDucky = (EntityDucky) entitylivingbaseIn;

        if (entityDucky.isSitting()) {
            setRotationPoint(0.0F, 20.0F, -4.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, bill, strongBill);

            setRotationPoint(0.0F, 21.0F, 0.0F, tail, body);

            setRotationPoint(-4.0F, 18.0F, 0.0F, rightWing, flyingRightWing);
            setRotationPoint(4.0F, 18.0F, 0.0F, leftWing, flyingLeftWing);

            setRotationPoint(-2.0F, 18.9F, 1.0F, rightLeg, fastRightLeg);
            setRotationPoint(1.0F, 18.9F, 1.0F, leftLeg, fastLeftLeg);
        } else {
            setRotationPoint(0.0F, 15.0F, -4.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, bill, strongBill);

            setRotationPoint(0.0F, 16.0F, 0.0F, tail, body);

            setRotationPoint(-4.0F, 13.0F, 0.0F, rightWing, flyingRightWing);
            setRotationPoint(4.0F, 13.0F, 0.0F, leftWing, flyingLeftWing);

            setRotationPoint(-2.0F, 19F, 1.0F, rightLeg, fastRightLeg);
            setRotationPoint(1.0F, 19F, 1.0F, leftLeg, fastLeftLeg);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how "far" arms
     * and legs can swing at most.
     */
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        head.rotateAngleX = headPitch * 0.017453292F;
        head.rotateAngleY = netHeadYaw * 0.017453292F;

        duplicateModelRotationAngles(head, hatTop, hatBottom, bill, strongBill, fireProofHatBottom, fireProofHatTop);

        body.rotateAngleX = ((float) Math.PI / 2F);
        duplicateModelRotationAngles(body, tail);

        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        duplicateModelRotationAngles(rightLeg, fastRightLeg);
        duplicateModelRotationAngles(leftLeg, fastLeftLeg);
        rightWing.rotateAngleZ = ageInTicks;
        leftWing.rotateAngleZ = -ageInTicks;
        duplicateModelRotationAngles(rightWing, flyingRightWing);
        duplicateModelRotationAngles(leftWing, flyingLeftWing);
    }
}
