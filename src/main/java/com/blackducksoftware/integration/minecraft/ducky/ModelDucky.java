/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.bill, this.tail);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(EntityDucky entityDucky, float limbSwingAmount, float ageInTicks, float partialTickTime) {
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

    @Override
    public void setRotationAngles(EntityDucky entityDucky, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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

}
