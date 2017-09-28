/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDucky extends ModelBase {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer tail;

    public ModelDucky() {
        this.head = createNewModelRenderer(0, 0, -2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F, 15.0F, -4.0F);
        this.bill = createNewModelRenderer(14, 0, -2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F, 15.0F, -4.0F);

        this.body = createNewModelRenderer(0, 9, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, 16.0F, 0.0F);
        this.tail = createNewModelRenderer(14, 4, -1.0F, 3.0F, 2.5F, 2, 2, 2, 0.0F, 16.0F, 0.0F);
        this.rightWing = createNewModelRenderer(24, 13, 0.0F, 0.0F, -3.0F, 1, 4, 6, -4.0F, 13.0F, 0.0F);
        this.leftWing = createNewModelRenderer(24, 13, -1.0F, 0.0F, -3.0F, 1, 4, 6, 4.0F, 13.0F, 0.0F);

        this.rightLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, -2.0F, 19.0F, 1.0F);
        this.leftLeg = createNewModelRenderer(26, 0, -1.0F, 0.0F, -3.0F, 3, 5, 3, 1.0F, 19.0F, 1.0F);
    }

    private ModelRenderer createNewModelRenderer(final int textureOffsetX, final int textureOffsetY, final float boxOffsetX, final float boxOffsetY, final float boxOffsetZ, final int boxWidth, final int boxHeight, final int boxDepth,
            final float rotationPointX, final float rotationPointY, final float rotationPointZ) {
        final ModelRenderer renderer = new ModelRenderer(this, textureOffsetX, textureOffsetY);
        renderer.addBox(boxOffsetX, boxOffsetY, boxOffsetZ, boxWidth, boxHeight, boxDepth);
        renderer.setRotationPoint(rotationPointX, rotationPointY, rotationPointZ);
        return renderer;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        this.head.render(scale);
        this.bill.render(scale);
        this.body.render(scale);
        this.tail.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
        this.rightWing.render(scale);
        this.leftWing.render(scale);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwingAmount, final float ageInTicks, final float partialTickTime) {
        final EntityDucky entityDucky = (EntityDucky) entitylivingbaseIn;

        if (entityDucky.isSitting()) {
            this.head.setRotationPoint(0.0F, 20.0F, -4.0F);
            this.bill.setRotationPoint(0.0F, 20.0F, -4.0F);
            this.tail.setRotationPoint(0.0F, 21.0F, 0.0F);
            this.body.setRotationPoint(0.0F, 21.0F, 0.0F);
            this.rightWing.setRotationPoint(-4.0F, 18.0F, 0.0F);
            this.leftWing.setRotationPoint(4.0F, 18.0F, 0.0F);

            this.rightLeg.setRotationPoint(-2.0F, 18.9F, 1.0F);
            this.leftLeg.setRotationPoint(1.0F, 18.9F, 1.0F);
        } else {
            this.head.setRotationPoint(0.0F, 15.0F, -4.0F);
            this.bill.setRotationPoint(0.0F, 15.0F, -4.0F);
            this.tail.setRotationPoint(0.0F, 16.0F, 0.0F);
            this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
            this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
            this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);

            this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
            this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how "far" arms
     * and legs can swing at most.
     */
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.bill.rotateAngleZ = this.head.rotateAngleZ;

        this.body.rotateAngleX = ((float) Math.PI / 2F);
        this.tail.rotateAngleX = this.body.rotateAngleX;
        this.tail.rotateAngleY = this.body.rotateAngleY;
        this.tail.rotateAngleZ = this.body.rotateAngleZ;

        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;
    }
}
