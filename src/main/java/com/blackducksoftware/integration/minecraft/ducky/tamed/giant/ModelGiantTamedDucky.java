/**
 * Copyright (C) 2018 Black Duck Software, Inc.
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
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.ducky.AbstractCommonModel;
import com.blackducksoftware.integration.minecraft.ducky.BaseEntityDucky;

import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelGiantTamedDucky extends AbstractCommonModel {
    public ModelRenderer head;
    public ModelRenderer hatTop;
    public ModelRenderer hatBottom;

    public ModelRenderer fireProofHatTop;
    public ModelRenderer fireProofHatBottom;

    public ModelRenderer billBase;
    public ModelRenderer billFront;

    public ModelRenderer strongBillBase;
    public ModelRenderer strongBillFront;

    public ModelRenderer body;

    public ModelRenderer flyingBody;

    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    public ModelRenderer rightFoot;
    public ModelRenderer leftFoot;

    public ModelRenderer fastRightFoot;
    public ModelRenderer fastLeftFoot;

    public ModelRenderer rightWing;
    public ModelRenderer leftWing;

    public ModelRenderer tailBase;
    public ModelRenderer tailTop;

    public ModelGiantTamedDucky() {
        super(256, 128);
        // For the parts attached to another body part, they must have the same rotation point for them to rotate correctly when that main body part turns
        // Ex: the Head and the bill must have the same rotation point, and the bill should offset from that point to render correctly
        // Ex: the body and the tail, legs and the feet
        head = createNewModelRenderer(0, 0, 0.0F, -2.0F, -9.0F, 10, 10, 10, -5.0F, -2.0F, -7.0F);
        hatTop = createNewModelRenderer(4, 54, 0.0F, -5.0F, -9.0F, 11, 3, 11, -5.0F, -2.0F, -7.0F);
        hatBottom = createNewModelRenderer(0, 52, 0.0F, -3.0F, -12.0F, 11, 1, 15, -5.0F, -2.0F, -7.0F);

        fireProofHatTop = createNewModelRenderer(4, 71, 0.0F, -5.0F, -9.0F, 11, 3, 11, -5.0F, -2.0F, -7.0F);
        fireProofHatBottom = createNewModelRenderer(0, 69, 0.0F, -3.0F, -12.0F, 11, 1, 15, -5.0F, -2.0F, -7.0F);

        billBase = createNewModelRenderer(0, 20, 2.0F, 2.0F, -12.0F, 6, 4, 7, -5.0F, -2.0F, -7.0F);
        billFront = createNewModelRenderer(0, 31, 2.0F, 3.0F, -14.0F, 6, 3, 7, -5.0F, -2.0F, -7.0F);

        strongBillBase = createNewModelRenderer(79, 68, 2.0F, 2.0F, -12.0F, 6, 4, 7, -5.0F, -2.0F, -7.0F);
        strongBillFront = createNewModelRenderer(79, 79, 2.0F, 3.0F, -14.0F, 6, 3, 7, -5.0F, -2.0F, -7.0F);

        body = createNewModelRenderer(40, 0, 0.0F, 0.0F, 0.0F, 16, 27, 11, -8.0F, 15.0F, -11.0F);

        flyingBody = createNewModelRenderer(107, 29, 0.0F, 0.0F, 0.0F, 16, 27, 11, -8.0F, 15.0F, -11.0F);

        tailBase = createNewModelRenderer(79, 39, 4.0F, 23.0F, 10.0F, 8, 5, 3, -8.0F, 15.0F, -11.0F);
        tailTop = createNewModelRenderer(79, 39, 6.0F, 25.0F, 13.0F, 4, 3, 2, -8.0F, 15.0F, -11.0F);

        rightLeg = createNewModelRenderer(64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, -6.0F, 15.0F, -3.0F);
        leftLeg = createNewModelRenderer(64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, 2.0F, 15.0F, -3.0F);

        rightFoot = createNewModelRenderer(41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, -6.0F, 15.0F, -3.0F);
        leftFoot = createNewModelRenderer(41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);

        fastLeftFoot = createNewModelRenderer(41, 46, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);
        fastRightFoot = createNewModelRenderer(41, 46, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);

        rightWing = createNewModelRenderer(94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, -10.0F, 5.0F, -9.0F);
        leftWing = createNewModelRenderer(94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, 8.0F, 5.0F, -9.0F);

        hideModelRenderers(fireProofHatTop, fireProofHatBottom, flyingBody, strongBillBase, strongBillFront, fastLeftFoot, fastRightFoot);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final BaseEntityDucky entityDucky = (BaseEntityDucky) entityIn;

        if (entityDucky.isFireProof()) {
            showModelRenderers(fireProofHatTop, fireProofHatBottom);
            hideModelRenderers(hatTop, hatBottom);
        } else {
            showModelRenderers(hatTop, hatBottom);
            hideModelRenderers(fireProofHatTop, fireProofHatBottom);
        }

        if (entityDucky.isCanFly()) {
            showModelRenderers(flyingBody);
            hideModelRenderers(body);
        } else {
            showModelRenderers(body);
            hideModelRenderers(flyingBody);
        }

        if (entityDucky.isStrong()) {
            showModelRenderers(strongBillBase, strongBillFront);
            hideModelRenderers(billBase, billFront);
        } else {
            showModelRenderers(billBase, billFront);
            hideModelRenderers(strongBillBase, strongBillFront);
        }

        if (entityDucky.isFast()) {
            showModelRenderers(fastRightFoot, fastLeftFoot);
            hideModelRenderers(rightFoot, leftFoot);
        } else {
            showModelRenderers(rightFoot, leftFoot);
            hideModelRenderers(fastRightFoot, fastLeftFoot);
        }

        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        head.render(scale);
        hatTop.render(scale);
        hatBottom.render(scale);

        fireProofHatTop.render(scale);
        fireProofHatBottom.render(scale);

        billBase.render(scale);
        billFront.render(scale);

        strongBillBase.render(scale);
        strongBillFront.render(scale);

        body.render(scale);

        flyingBody.render(scale);

        tailBase.render(scale);
        tailTop.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);
        rightFoot.render(scale);
        leftFoot.render(scale);

        fastLeftFoot.render(scale);
        fastRightFoot.render(scale);

        rightWing.render(scale);
        leftWing.render(scale);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwingAmount, final float ageInTicks, final float partialTickTime) {
        final BaseEntityDucky entityDucky = (BaseEntityDucky) entitylivingbaseIn;

        if (entityDucky.isSitting()) {
            // When Ducky sits we want his parts to move down but his legs should remain in place
            // to move Ducky down, for some reason that is an increase in the Y rotation point

            setRotationPoint(-5.0F, 5.0F, -7.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, billBase, billFront, strongBillBase, strongBillFront);

            setRotationPoint(-8.0F, 24.0F, -11.0F, body, tailBase, tailTop, flyingBody);

            rightLeg.setRotationPoint(-6.0F, 14.5F, -3.0F);
            leftLeg.setRotationPoint(2.0F, 14.5F, -3.0F);

            setRotationPoint(2.0F, 14.5F, -3.0F, leftFoot, fastLeftFoot);
            setRotationPoint(-6.0F, 14.5F, -3.0F, rightFoot, fastRightFoot);

            rightWing.setRotationPoint(-10.0F, 14.0F, -9.0F);
            leftWing.setRotationPoint(8.0F, 14.0F, -9.0F);
        } else {
            // When Ducky stands up we want his parts to move up again but his legs should remain in place
            // to move Ducky up, for some reason that is an decrease in the Y rotation point
            setRotationPoint(-5.0F, -2.0F, -7.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, billBase, billFront, strongBillBase, strongBillFront);

            setRotationPoint(-8.0F, 15.0F, -11.0F, body, tailBase, tailTop, flyingBody);

            rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);
            leftLeg.setRotationPoint(2.0F, 15.0F, -3.0F);

            setRotationPoint(2.0F, 15.0F, -3.0F, leftFoot, fastLeftFoot);
            setRotationPoint(-6.0F, 15.0F, -3.0F, rightFoot, fastRightFoot);

            rightWing.setRotationPoint(-10.0F, 5.0F, -9.0F);
            leftWing.setRotationPoint(8.0F, 5.0F, -9.0F);
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

        duplicateModelRotationAngles(head, hatTop, hatBottom, billBase, billFront, strongBillBase, strongBillFront, fireProofHatBottom, fireProofHatTop);

        body.rotateAngleX = ((float) Math.PI / 2F);
        duplicateModelRotationAngles(body, tailBase, tailTop, flyingBody);

        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        duplicateModelRotationAngles(rightLeg, rightFoot, fastRightFoot);
        duplicateModelRotationAngles(leftLeg, leftFoot, fastLeftFoot);

        rightWing.rotateAngleZ = ageInTicks;
        leftWing.rotateAngleZ = -ageInTicks;
    }
}
