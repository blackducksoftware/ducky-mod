/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky.tamed;

import com.blackducksoftware.integration.minecraft.DuckyModEntities;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFollowOwner;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EntityTamedDucky extends EntityDucky {
    public static final String TAMED_DUCKY_NAME = "tamed_bd_ducky";

    public EntityTamedDucky(World worldIn) {
        this(DuckyModEntities.TAMED_DUCKY, worldIn);
    }

    public EntityTamedDucky(EntityType<? extends EntityTamedDucky> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new DuckyAIFollowOwner(this, 1.0F, 12.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    // registerAttributes
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                   .func_233815_a_(Attributes.field_233818_a_, TAMED_HEALTH)
                   .func_233815_a_(Attributes.field_233821_d_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233822_e_, BASE_SPEED)
                   .func_233815_a_(Attributes.field_233819_b_, 64.0D)
                   .func_233815_a_(Attributes.field_233823_f_, TAMED_DAMAGE);
    }

    //    @Override
    //    protected void registerAttributes() {
    //        super.registerAttributes();
    //        // MAX_HEALTH Attributes.field_233818_a_
    //        // ATTACK_DAMAGE Attributes.field_233823_f_
    //        this.getAttribute(Attributes.field_233818_a_).setBaseValue(TAMED_HEALTH);
    //        this.getAttribute(Attributes.field_233823_f_).setBaseValue(TAMED_DAMAGE);
    //    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            // MAX_HEALTH Attributes.field_233818_a_
            // ATTACK_DAMAGE Attributes.field_233823_f_
            this.getAttribute(Attributes.field_233818_a_).setBaseValue(TAMED_HEALTH);
            this.getAttribute(Attributes.field_233823_f_).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        }
    }

    //processInteract
    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (isBreedingItem(itemstack) && itemstack.getItem().isFood()) {
            Food itemfood = itemstack.getItem().getFood();
            if (this.getHealth() < TAMED_HEALTH) {
                if (!player.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                this.heal(itemfood.getHealing());
                if (!this.world.isRemote) {
                    this.world.setEntityState(this, (byte) 7);
                    //this.playTameEffect(true);
                }
            }
            return getSuccessResult();
        } else if (Items.MILK_BUCKET == itemstack.getItem() && (this instanceof EntityGiantTamedDucky || this.isFireProof() || this.isStrong() || this.isFast() || this.isCanFly())) {
            if (!player.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.setFireProof(false);
                entityTamedDucky.setCanFly(false);
                entityTamedDucky.setStrong(false);
                entityTamedDucky.setFast(false);
            }
            return getSuccessResult();
        } else if (Items.POTION == itemstack.getItem()) {
            PotionItem potion = (PotionItem) itemstack.getItem();
            if (potion.hasEffect(itemstack)) {
                Potion potionType = PotionUtils.getPotionFromItem(itemstack);
                // player.addChatMessage(new TextComponentString(potionType.getNamePrefixed("")));
                if ("healing".equalsIgnoreCase(potionType.getNamePrefixed("")) && !(this instanceof EntityGiantTamedDucky)) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (!this.world.isRemote) {
                        EntityTamedDucky entityTamedDucky = new EntityGiantTamedDucky(this.world);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return getSuccessResult();
                } else if ("weakness".equalsIgnoreCase(potionType.getNamePrefixed("")) && (this instanceof EntityGiantTamedDucky)) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (!this.world.isRemote) {
                        EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.world);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return getSuccessResult();
                } else if ("fire_resistance".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setFireProof(true);
                    return getSuccessResult();
                } else if ("swiftness".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setFast(true);
                    return getSuccessResult();
                } else if ("strength".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setStrong(true);
                    return getSuccessResult();
                } else if ("leaping".equalsIgnoreCase(potionType.getNamePrefixed(""))) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setCanFly(true);
                    return getSuccessResult();
                }

            }
        } else {
            if (this.isOwner(player) && !this.world.isRemote) {
                setSitting(!this.isSitting());
                this.isJumping = false;
                this.getNavigator().clearPath();
                this.setAttackTarget(null);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType getSuccessResult() {
        return ActionResultType.func_233537_a_(this.world.isRemote());
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 15;
    }

}
