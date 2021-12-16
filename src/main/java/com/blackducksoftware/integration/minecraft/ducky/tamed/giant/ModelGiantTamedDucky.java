/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.ducky.AbstractCommonModel;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ModelGiantTamedDucky extends AbstractCommonModel<EntityGiantTamedDucky> {
    public ModelPart head;
    public ModelPart hatTop;
    public ModelPart hatBottom;

    public ModelPart fireProofHatTop;
    public ModelPart fireProofHatBottom;

    public ModelPart billBase;
    public ModelPart billFront;

    public ModelPart strongBillBase;
    public ModelPart strongBillFront;

    public ModelPart body;

    public ModelPart flyingBody;

    public ModelPart rightLeg;
    public ModelPart leftLeg;

    public ModelPart rightFoot;
    public ModelPart leftFoot;

    public ModelPart fastRightFoot;
    public ModelPart fastLeftFoot;

    public ModelPart rightWing;
    public ModelPart leftWing;

    public ModelPart tailBase;
    public ModelPart tailTop;

    public ModelGiantTamedDucky(ModelPart part) {
        // For the parts attached to another body part, they must have the same rotation point for them to rotate correctly when that main body part turns
        // Ex: the Head and the bill must have the same rotation point, and the bill should offset from that point to render correctly
        // Ex: the body and the tail, legs and the feet
        this.head = part.getChild("head");
        this.hatTop = part.getChild("hat_top");
        this.hatBottom = part.getChild("hat_bottom");
        //        this.fireProofHatTop = part.getChild("fire_proof_hat_top");
        //        this.fireProofHatBottom = part.getChild("fire_proof_hat_bottom");
        this.billBase = part.getChild("bill_base");
        this.billFront = part.getChild("bill_front");
        //        this.strongBillBase = part.getChild("strong_bill_base");
        //        this.strongBillFront = part.getChild("strong_bill_front");
        this.body = part.getChild("body");
        this.flyingBody = part.getChild("flying_body");
        this.leftWing = part.getChild("left_wing");
        this.rightWing = part.getChild("right_wing");
        //        this.flyingLeftWing = part.getChild("flying_left_wing");
        //        this.flyingRightWing = part.getChild("flying_right_wing");
        this.leftLeg = part.getChild("left_leg");
        this.rightLeg = part.getChild("right_leg");
        this.leftFoot = part.getChild("left_foot");
        this.rightFoot = part.getChild("right_foot");
        //        this.fastLeftFoot = part.getChild("fast_left_foot");
        //        this.fastRightFoot = part.getChild("fast_right_foot");
        this.tailBase = part.getChild("tail_base");
        this.tailTop = part.getChild("tail_top");

        //        hideModelRenderers(fireProofHatTop, fireProofHatBottom, flyingBody, strongBillBase, strongBillFront, fastLeftFoot, fastRightFoot);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        addNewBodyPart(def, "head", 0, 0, 0.0F, -2.0F, -9.0F, 10, 10, 10, -5.0F, -2.0F, -7.0F);
        addNewBodyPart(def, "hat_top", 4, 54, 0.0F, -5.0F, -9.0F, 11, 3, 11, -5.0F, -2.0F, -7.0F);
        addNewBodyPart(def, "hat_bottom", 0, 52, 0.0F, -3.0F, -12.0F, 11, 1, 15, -5.0F, -2.0F, -7.0F);

        //        fireProofHatTop = addNewBodyPart(def, "head", 4, 71, 0.0F, -5.0F, -9.0F, 11, 3, 11, -5.0F, -2.0F, -7.0F);
        //        fireProofHatBottom = addNewBodyPart(def, "head", 0, 69, 0.0F, -3.0F, -12.0F, 11, 1, 15, -5.0F, -2.0F, -7.0F);

        addNewBodyPart(def, "bill_base", 0, 20, 2.0F, 2.0F, -12.0F, 6, 4, 7, -5.0F, -2.0F, -7.0F);
        addNewBodyPart(def, "bill_front", 0, 31, 2.0F, 3.0F, -14.0F, 6, 3, 7, -5.0F, -2.0F, -7.0F);

        //        addNewBodyPart(def, "strong_bill_base", 79, 68, 2.0F, 2.0F, -12.0F, 6, 4, 7, -5.0F, -2.0F, -7.0F);
        //        addNewBodyPart(def, "strong_bill_front", 79, 79, 2.0F, 3.0F, -14.0F, 6, 3, 7, -5.0F, -2.0F, -7.0F);

        addNewBodyPart(def, "body", 40, 0, 0.0F, 0.0F, 0.0F, 16, 27, 11, -8.0F, 15.0F, -11.0F);

        //        addNewBodyPart(def, "flying_body", 107, 29, 0.0F, 0.0F, 0.0F, 16, 27, 11, -8.0F, 15.0F, -11.0F);

        addNewBodyPart(def, "tail_base", 79, 39, 4.0F, 23.0F, 10.0F, 8, 5, 3, -8.0F, 15.0F, -11.0F);
        addNewBodyPart(def, "tail_top", 79, 39, 6.0F, 25.0F, 13.0F, 4, 3, 2, -8.0F, 15.0F, -11.0F);

        addNewBodyPart(def, "right_wing", 94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, -10.0F, 5.0F, -9.0F);
        addNewBodyPart(def, "left_wing", 94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, 8.0F, 5.0F, -9.0F);

        //        addNewBodyPart(def, "right_leg", 64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, -6.0F, 15.0F, -3.0F);
        //        addNewBodyPart(def, "left_leg", 64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, 2.0F, 15.0F, -3.0F);

        //        addNewBodyPart(def, "right_foot", 41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, -6.0F, 15.0F, -3.0F);
        //        addNewBodyPart(def, "left_foot", 41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);

        //        addNewBodyPart(def, "fast_right_foot", 41, 46, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);
        //        addNewBodyPart(def, "fast_left_foot", 41, 46, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);

        return LayerDefinition.create(modelDefinition, 64, 32);
    }

    @Override
    public Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.billBase, this.billFront, this.hatTop, this.hatBottom);
    }

    @Override
    public Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightFoot, this.leftFoot, this.rightWing, this.leftWing, this.tailBase, this.tailTop);
    }

    @Override
    public void setupAnim(EntityGiantTamedDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //        if (entityDucky.isFireProof()) {
        //            showModelRenderers(fireProofHatTop, fireProofHatBottom);
        //            hideModelRenderers(hatTop, hatBottom);
        //        } else {
        //            showModelRenderers(hatTop, hatBottom);
        //            hideModelRenderers(fireProofHatTop, fireProofHatBottom);
        //        }
        //
        //        if (entityDucky.isCanFly()) {
        //            showModelRenderers(flyingBody);
        //            hideModelRenderers(body);
        //        } else {
        //            showModelRenderers(body);
        //            hideModelRenderers(flyingBody);
        //        }
        //
        //        if (entityDucky.isStrong()) {
        //            showModelRenderers(strongBillBase, strongBillFront);
        //            hideModelRenderers(billBase, billFront);
        //        } else {
        //            showModelRenderers(billBase, billFront);
        //            hideModelRenderers(strongBillBase, strongBillFront);
        //        }
        //
        //        if (entityDucky.isFast()) {
        //            showModelRenderers(fastRightFoot, fastLeftFoot);
        //            hideModelRenderers(rightFoot, leftFoot);
        //        } else {
        //            showModelRenderers(rightFoot, leftFoot);
        //            hideModelRenderers(fastRightFoot, fastLeftFoot);
        //        }

        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;

        // duplicateModelRotationAngles(head, hatTop, hatBottom, billBase, billFront, strongBillBase, strongBillFront, fireProofHatBottom, fireProofHatTop);

        body.xRot = ((float) Math.PI / 2F);
        //duplicateModelRotationAngles(body, tailBase, tailTop, flyingBody);

        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        //duplicateModelRotationAngles(rightLeg, rightFoot, fastRightFoot);
        // duplicateModelRotationAngles(leftLeg, leftFoot, fastLeftFoot);

        rightWing.zRot = ageInTicks;
        leftWing.zRot = -ageInTicks;
    }

    //    /**
    //     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
    //     */
    //    @Override
    //    public void setLivingAnimations(EntityGiantTamedDucky entityDucky, float limbSwingAmount, float ageInTicks, float partialTickTime) {
    //        if (entityDucky.isSitting()) {
    //            // When Ducky sits we want his parts to move down but his legs should remain in place
    //            // to move Ducky down, for some reason that is an increase in the Y rotation point
    //
    //            setRotationPoint(-5.0F, 5.0F, -7.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, billBase, billFront, strongBillBase, strongBillFront);
    //
    //            setRotationPoint(-8.0F, 24.0F, -11.0F, body, tailBase, tailTop, flyingBody);
    //
    //            rightLeg.setRotationPoint(-6.0F, 14.5F, -3.0F);
    //            leftLeg.setRotationPoint(2.0F, 14.5F, -3.0F);
    //
    //            setRotationPoint(2.0F, 14.5F, -3.0F, leftFoot, fastLeftFoot);
    //            setRotationPoint(-6.0F, 14.5F, -3.0F, rightFoot, fastRightFoot);
    //
    //            rightWing.setRotationPoint(-10.0F, 14.0F, -9.0F);
    //            leftWing.setRotationPoint(8.0F, 14.0F, -9.0F);
    //        } else {
    //            // When Ducky stands up we want his parts to move up again but his legs should remain in place
    //            // to move Ducky up, for some reason that is an decrease in the Y rotation point
    //            setRotationPoint(-5.0F, -2.0F, -7.0F, head, hatTop, hatBottom, fireProofHatTop, fireProofHatBottom, billBase, billFront, strongBillBase, strongBillFront);
    //
    //            setRotationPoint(-8.0F, 15.0F, -11.0F, body, tailBase, tailTop, flyingBody);
    //
    //            rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);
    //            leftLeg.setRotationPoint(2.0F, 15.0F, -3.0F);
    //
    //            setRotationPoint(2.0F, 15.0F, -3.0F, leftFoot, fastLeftFoot);
    //            setRotationPoint(-6.0F, 15.0F, -3.0F, rightFoot, fastRightFoot);
    //
    //            rightWing.setRotationPoint(-10.0F, 5.0F, -9.0F);
    //            leftWing.setRotationPoint(8.0F, 5.0F, -9.0F);
    //        }
    //    }

}
