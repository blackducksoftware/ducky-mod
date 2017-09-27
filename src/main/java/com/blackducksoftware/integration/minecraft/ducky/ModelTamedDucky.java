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
public class ModelTamedDucky extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer billBase;
    public ModelRenderer billFront;

    public ModelRenderer body;

    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    public ModelRenderer rightWing;
    public ModelRenderer leftWing;

    public ModelRenderer tailBase;
    public ModelRenderer tailTop;

    public ModelTamedDucky() {
        this.head = new ModelRenderer(this, 0, 0).setTextureSize(256, 128);
        this.head.addBox(0.0F, 0.0F, 0.0F, 10, 10, 10);
        this.head.setRotationPoint(-5.0F, -2.0F, -16.0F);

        this.billBase = new ModelRenderer(this, 0, 20).setTextureSize(256, 128);
        this.billBase.addBox(0.0F, 0.0F, 0.0F, 6, 4, 7);
        this.billBase.setRotationPoint(-3.0F, 2.0F, -19.0F);

        this.billFront = new ModelRenderer(this, 0, 31).setTextureSize(256, 128);
        this.billFront.addBox(0.0F, 0.0F, 0.0F, 6, 3, 7);
        this.billFront.setRotationPoint(-3.0F, 3.0F, -22.0F);

        this.body = new ModelRenderer(this, 40, 0).setTextureSize(256, 128);
        this.body.addBox(0.0F, 0.0F, 0.0F, 16, 27, 11);
        this.body.setRotationPoint(-8.0F, 15.0F, -11.0F);

        this.rightLeg = new ModelRenderer(this, 38, 39).setTextureSize(256, 128);
        this.rightLeg.addBox(0.0F, 0.0F, 0.0F, 5, 9, 6);
        this.rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);

        this.leftLeg = new ModelRenderer(this, 38, 39).setTextureSize(256, 128);
        this.leftLeg.addBox(0.0F, 0.0F, 0.0F, 5, 9, 6);
        this.leftLeg.setRotationPoint(1.0F, 15.0F, -3.0F);

        this.rightWing = new ModelRenderer(this, 94, 0).setTextureSize(256, 128);
        this.rightWing.addBox(0.0F, 0.0F, 0.0F, 2, 8, 20);
        this.rightWing.setRotationPoint(-10.0F, 5.0F, -9.0F);

        this.leftWing = new ModelRenderer(this, 94, 0).setTextureSize(256, 128);
        this.leftWing.addBox(0.0F, 0.0F, 0.0F, 2, 8, 20);
        this.leftWing.setRotationPoint(8.0F, 5.0F, -9.0F);

        this.tailBase = new ModelRenderer(this, 51, 0).setTextureSize(256, 128);
        this.tailBase.addBox(0.0F, 0.0F, 0.0F, 8, 5, 3);
        this.tailBase.setRotationPoint(-4.0F, 7.0F, 13.0F);

        this.tailTop = new ModelRenderer(this, 51, 0).setTextureSize(256, 128);
        this.tailTop.addBox(0.0F, 0.0F, 0.0F, 4, 3, 2);
        this.tailTop.setRotationPoint(-2.0F, 4.0F, 15.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        this.head.render(scale);
        this.billBase.render(scale);
        this.billFront.render(scale);
        this.body.render(scale);
        this.tailBase.render(scale);
        this.tailTop.render(scale);
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
            this.head.setRotationPoint(-5.0F, 5.0F, -16.0F);
            this.billBase.setRotationPoint(-3.0F, 9.0F, -19.0F);
            this.billFront.setRotationPoint(-3.0F, 10.0F, -22.0F);
            this.body.setRotationPoint(-8.0F, 24.0F, -11.0F);

            this.rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);
            this.leftLeg.setRotationPoint(1.0F, 15.0F, -3.0F);

            this.rightWing.setRotationPoint(-10.0F, 14.0F, -9.0F);
            this.leftWing.setRotationPoint(8.0F, 14.0F, -9.0F);

            this.tailBase.setRotationPoint(-4.0F, 16.0F, 13.0F);
            this.tailTop.setRotationPoint(-2.0F, 13.0F, 15.0F);
        } else {
            this.head.setRotationPoint(-5.0F, -2.0F, -16.0F);
            this.billBase.setRotationPoint(-3.0F, 2.0F, -20.0F);
            this.billFront.setRotationPoint(-3.0F, 3.0F, -22.0F);
            this.body.setRotationPoint(-8.0F, 15.0F, -11.0F);

            this.rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);
            this.leftLeg.setRotationPoint(1.0F, 15.0F, -3.0F);

            this.leftWing.setRotationPoint(8.0F, 5.0F, -9.0F);
            this.rightWing.setRotationPoint(-10.0F, 5.0F, -9.0F);

            this.tailBase.setRotationPoint(-4.0F, 7.0F, 13.0F);
            this.tailTop.setRotationPoint(-2.0F, 4.0F, 15.0F);
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
        this.billBase.rotateAngleX = this.head.rotateAngleX;
        this.billFront.rotateAngleY = this.head.rotateAngleY;

        this.body.rotateAngleX = ((float) Math.PI / 2F);
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;

        this.tailBase.rotateAngleX = ((float) Math.PI / 2F);
        this.tailTop.rotateAngleX = ((float) Math.PI / 2F);
    }
}
