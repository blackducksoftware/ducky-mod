/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import com.blackducksoftware.integration.minecraft.ducky.AbstractCommonModel;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ModelTamedDucky extends AbstractCommonModel<EntityTamedDucky> {
    public ModelPart head;
    public ModelPart hatTop;
    public ModelPart hatBottom;

    public ModelPart fireProofHatTop;
    public ModelPart fireProofHatBottom;

    public ModelPart body;
    public ModelPart rightLeg;
    public ModelPart leftLeg;

    public ModelPart fastRightLeg;
    public ModelPart fastLeftLeg;

    public ModelPart rightWing;
    public ModelPart leftWing;

    public ModelPart flyingRightWing;
    public ModelPart flyingLeftWing;

    public ModelPart bill;

    public ModelPart strongBill;

    public ModelPart tail;

    public ModelTamedDucky(ModelPart part) {
        this.head = part.getChild("head");
        this.hatTop = part.getChild("hat_top");
        this.hatBottom = part.getChild("hat_bottom");
        //        this.fireProofHatTop = part.getChild("fire_proof_hat_top");
        //        this.fireProofHatBottom = part.getChild("fire_proof_hat_bottom");
        this.bill = part.getChild("bill");
        //        this.strongBill = part.getChild("strong_bill");
        this.body = part.getChild("body");
        this.leftWing = part.getChild("left_wing");
        this.rightWing = part.getChild("right_wing");
        //        this.flyingLeftWing = part.getChild("flying_left_wing");
        //        this.flyingRightWing = part.getChild("flying_right_wing");
        this.leftLeg = part.getChild("left_leg");
        this.rightLeg = part.getChild("right_leg");
        //        this.fastLeftLeg = part.getChild("fast_left_leg");
        //        this.fastRightLeg = part.getChild("fast_right_leg");
        this.tail = part.getChild("tail");

        //  hideModelRenderers(fireProofHatTop, fireProofHatBottom, flyingLeftWing, flyingRightWing, strongBill, fastLeftLeg, fastRightLeg);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        addNewBodyPart(def, "head", 0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, 15.0F, -4.0F);
        addNewBodyPart(def, "hat_top", 40, 1, -2.5F, -7.0F, -2.0F, 5, 1, 4, 0.0F, 15.0F, -4.0F);
        addNewBodyPart(def, "hat_bottom", 39, 0, -2.5F, -6.0F, -3.0F, 5, 1, 5, 0.0F, 15.0F, -4.0F);

        //        fireProofHatTop = addNewBodyPart(def, "head", 40, 8, -2.5F, -7.0F, -2.0F, 5, 1, 4, 0.0F, 15.0F, -4.0F);
        //        fireProofHatBottom = addNewBodyPart(def, "head", 39, 7, -2.5F, -6.0F, -3.0F, 5, 1, 5, 0.0F, 15.0F, -4.0F);

        addNewBodyPart(def, "bill", 14, 0, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        //        strongBill = addNewBodyPart(def, "head", 12, 24, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        addNewBodyPart(def, "body", 0, 9, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, 16.0F, 0.0F);
        addNewBodyPart(def, "tail", 14, 4, -1.0F, 3.0F, 2.5F, 2, 2, 2, 0.0F, 16.0F, 0.0F);
        addNewBodyPart(def, "right_wing", 24, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        addNewBodyPart(def, "left_wing", 24, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        //        flyingRightWing = addNewBodyPart(def, "head", 39, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        //        flyingLeftWing = addNewBodyPart(def, "head", 39, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        addNewBodyPart(def, "right_leg", 26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        addNewBodyPart(def, "left_leg", 26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);

        //        fastRightLeg = addNewBodyPart(def, "head", 0, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        //        fastLeftLeg = addNewBodyPart(def, "head", 0, 24, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);

        return LayerDefinition.create(modelDefinition, 64, 32);
    }

    //    @Override
    //    public Iterable<ModelPart> getParts() {
    //        return ImmutableList
    //                   .of(this.head, this.hatTop, this.hatBottom, this.fireProofHatTop, this.fireProofHatBottom, this.body, this.rightLeg, this.leftLeg, this.fastRightLeg, this.fastLeftLeg, this.rightWing, this.leftWing, this.flyingRightWing,
    //                       this.flyingLeftWing, this.bill, this.strongBill, this.tail);
    //    }

    @Override
    public Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.bill, this.hatTop, this.hatBottom);
    }

    @Override
    public Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.tail);
    }

    @Override
    public void setupAnim(EntityTamedDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //        if (entityDucky.isFireProof()) {
        //            showModelRenderers(fireProofHatTop, fireProofHatBottom);
        //            hideModelRenderers(hatTop, hatBottom);
        //        } else {
        //            showModelRenderers(hatTop, hatBottom);
        //            hideModelRenderers(fireProofHatTop, fireProofHatBottom);
        //        }
        //
        //        if (entityDucky.isCanFly()) {
        //            showModelRenderers(flyingLeftWing, flyingRightWing);
        //            hideModelRenderers(rightWing, leftWing);
        //        } else {
        //            showModelRenderers(rightWing, leftWing);
        //            hideModelRenderers(flyingLeftWing, flyingRightWing);
        //        }
        //
        //        if (entityDucky.isStrong()) {
        //            showModelRenderers(strongBill);
        //            hideModelRenderers(bill);
        //        } else {
        //            showModelRenderers(bill);
        //            hideModelRenderers(strongBill);
        //        }
        //
        //        if (entityDucky.isFast()) {
        //            showModelRenderers(fastLeftLeg, fastRightLeg);
        //            hideModelRenderers(rightLeg, leftLeg);
        //        } else {
        //            showModelRenderers(rightLeg, leftLeg);
        //            hideModelRenderers(fastLeftLeg, fastRightLeg);
        //        }

        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;

        // duplicateModelRotationAngles(head, hatTop, hatBottom, bill, strongBill, fireProofHatBottom, fireProofHatTop);

        body.xRot = ((float) Math.PI / 2F);
        //duplicateModelRotationAngles(body, tail);

        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        //        duplicateModelRotationAngles(rightLeg, fastRightLeg);
        //        duplicateModelRotationAngles(leftLeg, fastLeftLeg);
        rightWing.zRot = ageInTicks;
        leftWing.zRot = -ageInTicks;
        //        duplicateModelRotationAngles(rightWing, flyingRightWing);
        //        duplicateModelRotationAngles(leftWing, flyingLeftWing);
    }

    //    /**
    //     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
    //     */
    //    @Override
    //    public void setLivingAnimations(EntityTamedDucky entityDucky, float limbSwingAmount, float ageInTicks, float partialTickTime) {
    //        if (entityDucky.isSitting()) {
    //            setRotationPoint(0.0F, 20.0F, -4.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, bill, strongBill);
    //
    //            setRotationPoint(0.0F, 21.0F, 0.0F, tail, body);
    //
    //            setRotationPoint(-4.0F, 18.0F, 0.0F, rightWing, flyingRightWing);
    //            setRotationPoint(4.0F, 18.0F, 0.0F, leftWing, flyingLeftWing);
    //
    //            setRotationPoint(-2.0F, 18.9F, 1.0F, rightLeg, fastRightLeg);
    //            setRotationPoint(1.0F, 18.9F, 1.0F, leftLeg, fastLeftLeg);
    //        } else {
    //            setRotationPoint(0.0F, 15.0F, -4.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, bill, strongBill);
    //
    //            setRotationPoint(0.0F, 16.0F, 0.0F, tail, body);
    //
    //            setRotationPoint(-4.0F, 13.0F, 0.0F, rightWing, flyingRightWing);
    //            setRotationPoint(4.0F, 13.0F, 0.0F, leftWing, flyingLeftWing);
    //
    //            setRotationPoint(-2.0F, 19F, 1.0F, rightLeg, fastRightLeg);
    //            setRotationPoint(1.0F, 19F, 1.0F, leftLeg, fastLeftLeg);
    //        }
    //    }

}
