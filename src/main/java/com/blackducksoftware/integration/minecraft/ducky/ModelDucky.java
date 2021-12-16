/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ModelDucky extends AgeableListModel<EntityDucky> {
    public ModelPart head;
    public ModelPart body;
    public ModelPart rightLeg;
    public ModelPart leftLeg;
    public ModelPart rightWing;
    public ModelPart leftWing;
    public ModelPart bill;
    public ModelPart tail;

    public ModelDucky(ModelPart part) {
        super(false, 64, 32);
        this.head = part.getChild("head");
        this.bill = part.getChild("bill");
        this.body = part.getChild("body");
        this.leftWing = part.getChild("left_wing");
        this.rightWing = part.getChild("right_wing");
        this.leftLeg = part.getChild("left_leg");
        this.rightLeg = part.getChild("right_leg");
        this.tail = part.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3), PartPose.offset(0.0F, 15.0F, -4.0F));
        def.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2), PartPose.offset(0.0F, 15.0F, -4.0F));
        def.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6), PartPose.offset(0.0F, 16.0F, 0.0F));
        def.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, 3.0F, 2.5F, 2, 2, 2), PartPose.offset(0.0F, 16.0F, 0.0F));

        def.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(01.0F, 0.0F, -3.0F, 1, 4, 6), PartPose.offset(-4.0F, 13.0F, 0.0F));
        def.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6), PartPose.offset(4.0F, 13.0F, 0.0F));

        def.addOrReplaceChild("feet_right", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3), PartPose.offset(-2.0F, 19.0F, 1.0F));
        def.addOrReplaceChild("feet_left", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3), PartPose.offset(1.0F, 19.0F, 1.0F));

        return LayerDefinition.create(modelDefinition, 64, 32);
    }

    @Override
    public Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.bill);
    }

    @Override
    public Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.tail);
    }

    //    /**
    //     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
    //     */
    //    @Override
    //    public void setLivingAnimations(EntityDucky entityDucky, float limbSwingAmount, float ageInTicks, float partialTickTime) {
    //        if (entityDucky.isSitting()) {
    //            setRotationPoint(0.0F, 20.0F, -4.0F, head, bill);
    //            setRotationPoint(0.0F, 21.0F, 0.0F, tail, body);
    //
    //            this.rightWing.setRotationPoint(-4.0F, 18.0F, 0.0F);
    //            this.leftWing.setRotationPoint(4.0F, 18.0F, 0.0F);
    //
    //            this.rightLeg.setRotationPoint(-2.0F, 18.9F, 1.0F);
    //            this.leftLeg.setRotationPoint(1.0F, 18.9F, 1.0F);
    //        } else {
    //            setRotationPoint(0.0F, 15.0F, -4.0F, head, bill);
    //            setRotationPoint(0.0F, 16.0F, 0.0F, tail, body);
    //
    //            this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
    //            this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
    //
    //            this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
    //            this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
    //        }
    //    }

    @Override
    public void setupAnim(EntityDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = netHeadYaw * 0.017453292F;
        //this.head.zRot = (Mth.cos(limbSwing * 1.3324F) * 1.4F * limbSwingAmount) / 6;

        this.bill.xRot = this.head.xRot;
        this.bill.yRot = this.head.yRot;
        //this.bill.zRot = this.head.zRot;

        this.body.xRot = ((float) Math.PI / 2F);
        this.tail.xRot = this.body.xRot;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;

    }

}
