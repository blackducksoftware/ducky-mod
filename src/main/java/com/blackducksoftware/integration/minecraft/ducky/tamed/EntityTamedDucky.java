/**
 * ducky-mod
 *
 * Copyright (c) 2020 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import com.blackducksoftware.integration.minecraft.DuckyModEntities;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwner;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EntityTamedDucky extends EntityDucky {
    public static final String TAMED_DUCKY_NAME = "tamed_bd_ducky";

    public EntityTamedDucky(final World worldIn) {
        this(DuckyModEntities.TAMED_DUCKY, worldIn);
    }

    public EntityTamedDucky(EntityType<? extends EntityTamedDucky> type, final World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new DuckyAIFollowOwner(this, 1.0F, 12.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
    }

    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
            this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        }
    }

    /**
     * Return true if you want to skip processing the other hand
     */
    @Override
    public boolean processInteract(final PlayerEntity player, final Hand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (isBreedingItem(itemstack) && itemstack.getItem().isFood()) {
            final Food itemfood = itemstack.getItem().getFood();
            if (this.getHealth() < TAMED_HEALTH) {
                if (!player.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                this.heal(itemfood.getHealing());
                this.playTameEffect(true);
            }
            return true;
        } else if (Items.MILK_BUCKET == itemstack.getItem() && (this instanceof EntityGiantTamedDucky || this.isImmuneToFire() || this.isStrong() || this.isFast() || this.isCanFly())) {
            if (!player.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                entityTamedDucky.setFireProof(false);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.setCanFly(false);
                entityTamedDucky.setStrong(false);
                entityTamedDucky.setFast(false);
            }
            return true;
        } else if (Items.POTION == itemstack.getItem()) {
            final PotionItem potion = (PotionItem) itemstack.getItem();
            if (potion.hasEffect(itemstack)) {
                final Potion potionType = PotionUtils.getPotionFromItem(itemstack);
                // player.addChatMessage(new TextComponentString(potionType.getNamePrefixed("")));
                if ("healing".equalsIgnoreCase(potionType.getNamePrefixed("")) && !(this instanceof EntityGiantTamedDucky)) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (!this.world.isRemote) {
                        final EntityTamedDucky entityTamedDucky = new EntityGiantTamedDucky(this.world);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return true;
                } else if ("weakness".equalsIgnoreCase(potionType.getNamePrefixed("")) && (this instanceof EntityGiantTamedDucky)) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (!this.world.isRemote) {
                        final EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return true;
                } else if ("fire_resistance".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setFireProof(true);
                    return true;
                } else if ("swiftness".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setFast(true);
                    return true;
                } else if ("strength".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setStrong(true);
                    return true;
                } else if ("leaping".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setCanFly(true);
                    return true;
                }

            }
        } else {
            if (this.isOwner(player) && !this.world.isRemote) {
                setSitting(!this.isSitting());
                this.isJumping = false;
                this.getNavigator().clearPath();
                this.setAttackTarget(null);
            }
            return true;
        }
        return false;
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(final PlayerEntity player) {
        return 15;
    }

}
