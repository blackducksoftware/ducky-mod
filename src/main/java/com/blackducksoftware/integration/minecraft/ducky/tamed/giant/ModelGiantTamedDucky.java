/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.ducky.AbstractCommonModel;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelGiantTamedDucky extends AbstractCommonModel<EntityGiantTamedDucky> {
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

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList
                   .of(this.head, this.hatTop, this.hatBottom, this.fireProofHatTop, this.fireProofHatBottom, this.billBase, this.billFront, this.strongBillBase, this.strongBillFront,
                       this.body, this.flyingBody, this.rightLeg, this.leftLeg, this.rightFoot, this.leftFoot, this.fastRightFoot, this.fastLeftFoot, this.rightWing, this.leftWing, this.tailBase, this.tailTop);
    }

    @Override
    public void setRotationAngles(EntityGiantTamedDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(EntityGiantTamedDucky entityDucky, float limbSwingAmount, float ageInTicks, float partialTickTime) {
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

}
