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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwner;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityTamedDucky extends EntityDucky {
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
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED);
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
        } else if (Items.MILK_BUCKET == itemstack.getItem() && (this instanceof EntityGiantTamedDucky || this.isImmuneToFire() || this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() == FAST_SPEED)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.worldObj.isRemote) {
                final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.worldObj);
                entityTamedDucky.setFireProof(false);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.setCanFly(false);
                entityTamedDucky.setStrength(false);
                entityTamedDucky.setSpeed(false);
            }
            return true;
        } else if (Items.POTIONITEM == itemstack.getItem()) {
            final ItemPotion potion = (ItemPotion) itemstack.getItem();
            if (potion.hasEffect(itemstack)) {
                final PotionType potionType = PotionUtils.getPotionFromItem(itemstack);
                // player.addChatMessage(new TextComponentString(potionType.getNamePrefixed("")));
                if ("healing".equalsIgnoreCase(potionType.getNamePrefixed("")) && !(this instanceof EntityGiantTamedDucky)) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    if (!this.worldObj.isRemote) {
                        final EntityTamedDucky entityTamedDucky = new EntityGiantTamedDucky(this.worldObj);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrength(this.isStrong());
                        entityTamedDucky.setSpeed(this.isFast());
                    }
                    return true;
                } else if ("weakness".equalsIgnoreCase(potionType.getNamePrefixed("")) && (this instanceof EntityGiantTamedDucky)) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    if (!this.worldObj.isRemote) {
                        final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.worldObj);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrength(this.isStrong());
                        entityTamedDucky.setSpeed(this.isFast());
                    }
                    return true;
                } else if ("fire_resistance".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    this.setFireProof(true);
                    return true;
                } else if ("swiftness".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    this.setSpeed(true);
                    return true;
                } else if ("strength".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    this.setStrength(true);
                    return true;
                } else if ("leaping".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    this.setCanFly(true);
                    return true;
                }

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
        return false;
    }

}
