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
package com.blackducksoftware.integration.minecraft.ducky.tamed.giant;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGiantTamedDucky extends ModelBase {
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 128;

    public ModelRenderer head;
    public ModelRenderer hatTop;
    public ModelRenderer hatBottom;
    public ModelRenderer billBase;
    public ModelRenderer billFront;

    public ModelRenderer body;

    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    public ModelRenderer rightFoot;
    public ModelRenderer leftFoot;

    public ModelRenderer rightWing;
    public ModelRenderer leftWing;

    public ModelRenderer tailBase;
    public ModelRenderer tailTop;

    public ModelGiantTamedDucky() {
        // For the parts attached to another body part, they must have the same rotation point for them to rotate correctly when that main body part turns
        // Ex: the Head and the bill must have the same rotation point, and the bill should offset from that point to render correctly
        // Ex: the body and the tail, legs and the feet
        this.head = createNewModelRenderer(0, 0, 0.0F, -2.0F, -9.0F, 10, 10, 10, -5.0F, -2.0F, -7.0F);
        this.hatTop = createNewModelRenderer(4, 54, 0.0F, -5.0F, -9.0F, 11, 3, 11, -5.0F, -2.0F, -7.0F);
        this.hatBottom = createNewModelRenderer(0, 52, 0.0F, -3.0F, -12.0F, 11, 1, 15, -5.0F, -2.0F, -7.0F);
        this.billBase = createNewModelRenderer(0, 20, 2.0F, 2.0F, -12.0F, 6, 4, 7, -5.0F, -2.0F, -7.0F);
        this.billFront = createNewModelRenderer(0, 31, 2.0F, 3.0F, -14.0F, 6, 3, 7, -5.0F, -2.0F, -7.0F);

        this.body = createNewModelRenderer(40, 0, 0.0F, 0.0F, 0.0F, 16, 27, 11, -8.0F, 15.0F, -11.0F);
        this.tailBase = createNewModelRenderer(79, 39, 4.0F, 23.0F, 10.0F, 8, 5, 3, -8.0F, 15.0F, -11.0F);
        this.tailTop = createNewModelRenderer(79, 39, 6.0F, 25.0F, 13.0F, 4, 3, 2, -8.0F, 15.0F, -11.0F);

        this.rightLeg = createNewModelRenderer(64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, -6.0F, 15.0F, -3.0F);
        this.rightFoot = createNewModelRenderer(41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, -6.0F, 15.0F, -3.0F);

        this.leftLeg = createNewModelRenderer(64, 39, 0.0F, 0.0F, 0.0F, 3, 9, 3, 2.0F, 15.0F, -3.0F);
        this.leftFoot = createNewModelRenderer(41, 39, -1.0F, 7.0F, -3.0F, 5, 2, 4, 2.0F, 15.0F, -3.0F);

        this.rightWing = createNewModelRenderer(94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, -10.0F, 5.0F, -9.0F);
        this.leftWing = createNewModelRenderer(94, 0, 0.0F, 0.0F, 0.0F, 2, 8, 20, 8.0F, 5.0F, -9.0F);
    }

    private ModelRenderer createNewModelRenderer(final int textureOffsetX, final int textureOffsetY, final float boxOffsetX, final float boxOffsetY, final float boxOffsetZ, final int boxWidth, final int boxHeight, final int boxDepth,
            final float rotationPointX, final float rotationPointY, final float rotationPointZ) {
        final ModelRenderer renderer = new ModelRenderer(this, textureOffsetX, textureOffsetY).setTextureSize(TEXTURE_WIDTH, TEXTURE_HEIGHT);
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
        this.hatTop.render(scale);
        this.hatBottom.render(scale);
        this.billBase.render(scale);
        this.billFront.render(scale);
        this.body.render(scale);
        this.tailBase.render(scale);
        this.tailTop.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
        this.rightFoot.render(scale);
        this.leftFoot.render(scale);
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
            // When Ducky sits we want his parts to move down but his legs should remain in place
            // to move Ducky down, for some reason that is an increase in the Y rotation point
            this.head.setRotationPoint(-5.0F, 5.0F, -7.0F);
            this.hatTop.setRotationPoint(-5.0F, 5.0F, -7.0F);
            this.hatBottom.setRotationPoint(-5.0F, 5.0F, -7.0F);
            this.billBase.setRotationPoint(-5.0F, 5.0F, -7.0F);
            this.billFront.setRotationPoint(-5.0F, 5.0F, -7.0F);
            this.body.setRotationPoint(-8.0F, 24.0F, -11.0F);

            this.rightLeg.setRotationPoint(-6.0F, 14.5F, -3.0F);
            this.leftLeg.setRotationPoint(2.0F, 14.5F, -3.0F);

            this.leftFoot.setRotationPoint(2.0F, 14.5F, -3.0F);
            this.rightFoot.setRotationPoint(-6.0F, 14.5F, -3.0F);

            this.rightWing.setRotationPoint(-10.0F, 14.0F, -9.0F);
            this.leftWing.setRotationPoint(8.0F, 14.0F, -9.0F);

            this.tailBase.setRotationPoint(-8.0F, 24.0F, -11.0F);
            this.tailTop.setRotationPoint(-8.0F, 24.0F, -11.0F);
        } else {
            // When Ducky stands up we want his parts to move up again but his legs should remain in place
            // to move Ducky up, for some reason that is an decrease in the Y rotation point
            this.head.setRotationPoint(-5.0F, -2.0F, -7.0F);
            this.hatTop.setRotationPoint(-5.0F, -2.0F, -7.0F);
            this.hatBottom.setRotationPoint(-5.0F, -2.0F, -7.0F);
            this.billBase.setRotationPoint(-5.0F, -2.0F, -7.0F);
            this.billFront.setRotationPoint(-5.0F, -2.0F, -7.0F);
            this.body.setRotationPoint(-8.0F, 15.0F, -11.0F);

            this.rightLeg.setRotationPoint(-6.0F, 15.0F, -3.0F);
            this.leftLeg.setRotationPoint(2.0F, 15.0F, -3.0F);

            this.leftFoot.setRotationPoint(2.0F, 15.0F, -3.0F);
            this.rightFoot.setRotationPoint(-6.0F, 15.0F, -3.0F);

            this.leftWing.setRotationPoint(8.0F, 5.0F, -9.0F);
            this.rightWing.setRotationPoint(-10.0F, 5.0F, -9.0F);

            this.tailBase.setRotationPoint(-8.0F, 15.0F, -11.0F);
            this.tailTop.setRotationPoint(-8.0F, 15.0F, -11.0F);
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
        this.hatTop.rotateAngleX = this.head.rotateAngleX;
        this.hatTop.rotateAngleY = this.head.rotateAngleY;
        this.hatTop.rotateAngleZ = this.head.rotateAngleZ;
        this.hatBottom.rotateAngleX = this.head.rotateAngleX;
        this.hatBottom.rotateAngleY = this.head.rotateAngleY;
        this.hatBottom.rotateAngleZ = this.head.rotateAngleZ;
        // we want the bill to rotate the same as the head
        this.billBase.rotateAngleX = this.head.rotateAngleX;
        this.billBase.rotateAngleY = this.head.rotateAngleY;
        this.billBase.rotateAngleZ = this.head.rotateAngleZ;
        this.billFront.rotateAngleX = this.head.rotateAngleX;
        this.billFront.rotateAngleY = this.head.rotateAngleY;
        this.billFront.rotateAngleZ = this.head.rotateAngleZ;

        this.body.rotateAngleX = ((float) Math.PI / 2F);
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        // we want the feet to rotate the same as the legs
        this.rightFoot.rotateAngleX = this.rightLeg.rotateAngleX;
        this.rightFoot.rotateAngleY = this.rightLeg.rotateAngleY;
        this.rightFoot.rotateAngleZ = this.rightLeg.rotateAngleZ;
        this.leftFoot.rotateAngleX = this.leftLeg.rotateAngleX;
        this.leftFoot.rotateAngleY = this.leftLeg.rotateAngleY;
        this.leftFoot.rotateAngleZ = this.leftLeg.rotateAngleZ;

        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;

        // we want the tail to rotate the same as the legs
        this.tailBase.rotateAngleX = this.body.rotateAngleX;
        this.tailBase.rotateAngleY = this.body.rotateAngleY;
        this.tailBase.rotateAngleZ = this.body.rotateAngleZ;
        this.tailTop.rotateAngleX = this.body.rotateAngleX;
        this.tailTop.rotateAngleY = this.body.rotateAngleY;
        this.tailTop.rotateAngleZ = this.body.rotateAngleZ;
    }
}
