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
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import java.util.List;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwner;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwnerFlying;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityTamedDucky extends EntityDucky {
    public static final double TAMED_HEALTH = 64.0D;
    public static final double TAMED_DAMAGE = 30.0D;

    public static final String TAMED_DUCKY_NAME = "tamed_bd_ducky";

    public EntityTamedDucky(final World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.setScale(1.0F);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new DuckyAIFollowOwner(this, 3.0F, 8.0F));
        this.tasks.addTask(7, new DuckyAIFollowOwnerFlying(this, 3.0F, 8.0F));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
    }

    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        }
    }

    /**
     * Return true if you want to skip processing the other hand
     */
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (this.isTamed()) {
            if (isBreedingItem(itemstack)) {
                final ItemFood itemfood = (ItemFood) itemstack.getItem();
                if (this.getHealth() < TAMED_HEALTH) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    this.heal(itemfood.getHealAmount(itemstack));
                    this.playTameEffect(true);
                }
                return true;
            } else if (Items.POTIONITEM == itemstack.getItem()) {
                final ItemPotion potion = (ItemPotion) itemstack.getItem();
                if (potion.hasEffect(itemstack)) {
                    final List<PotionEffect> potionEffects = PotionUtils.getEffectsFromStack(itemstack);
                    potionEffects.get(0).getEffectName();
                    player.addChatMessage(new TextComponentString(potionEffects.get(0).getEffectName()));
                }
            } else {
                if (this.isOwner(player) && !this.worldObj.isRemote) {
                    setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.navigator.clearPathEntity();
                    this.setAttackTarget((EntityLivingBase) null);
                }
                return true;
            }
        } else if (isBreedingItem(itemstack)) {
            return true;
        }
        return false;
    }
}
