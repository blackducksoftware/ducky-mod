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

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class EntityTamedDucky extends EntityDucky {
    public static final String TAMED_DUCKY_NAME = "tamed_bd_ducky";

    public EntityTamedDucky(Level level) {
        this(DuckyModEntities.TAMED_DUCKY, level);
    }

    public EntityTamedDucky(EntityType<? extends EntityTamedDucky> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new DuckyAIFollowOwner(this, 1.0F, 12.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    // registerAttributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                   .add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
                   .add(Attributes.FLYING_SPEED, BASE_SPEED)
                   .add(Attributes.FOLLOW_RANGE, 64.0D)
                   .add(Attributes.MAX_HEALTH, TAMED_HEALTH)
                   .add(Attributes.ATTACK_DAMAGE, TAMED_DAMAGE);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(TAMED_HEALTH);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(TAMED_DAMAGE);
            this.setHealth((float) TAMED_HEALTH);
        }
    }

    //processInteract
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isFood(itemstack)) {
            Item itemfood = itemstack.getItem();
            if (this.getHealth() < TAMED_HEALTH) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.heal(itemfood.getFoodProperties().getNutrition());
                if (this.level.isClientSide()) {
                    this.setTame(true);
                    //                    this.level.setEntityState(this, (byte) 7);
                    //this.playTameEffect(true);
                }
            }
            return getSuccessResult();
        } else if (Items.MILK_BUCKET == itemstack.getItem() && (this instanceof EntityGiantTamedDucky || this.isFireProof() || this.isStrong() || this.isFast() || this.isCanFly())) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            if (this.level.isClientSide()) {
                EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.level);
                spawnTamedDucky(player, entityTamedDucky);
                entityTamedDucky.setFireProof(false);
                entityTamedDucky.setCanFly(false);
                entityTamedDucky.setStrong(false);
                entityTamedDucky.setFast(false);
            }
            return getSuccessResult();
        } else if (Items.POTION == itemstack.getItem()) {
            PotionItem potion = (PotionItem) itemstack.getItem();
            if (potion.isFoil(itemstack)) {
                Potion potionType = PotionUtils.getPotion(itemstack);
                // player.addChatMessage(new TextComponentString(potionType.getNamePrefixed("")));
                if ("healing".equalsIgnoreCase(potionType.getName("")) && !(this instanceof EntityGiantTamedDucky)) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    if (this.level.isClientSide()) {
                        EntityTamedDucky entityTamedDucky = new EntityGiantTamedDucky(this.level);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return getSuccessResult();
                } else if ("weakness".equalsIgnoreCase(potionType.getName("")) && (this instanceof EntityGiantTamedDucky)) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    if (this.level.isClientSide()) {
                        EntityTamedDucky entityTamedDucky = new EntityTamedDucky(this.level);
                        entityTamedDucky.setFireProof(this.isFireProof());
                        spawnTamedDucky(player, entityTamedDucky);
                        entityTamedDucky.setCanFly(this.isCanFly());
                        entityTamedDucky.setStrong(this.isStrong());
                        entityTamedDucky.setFast(this.isFast());
                    }
                    return getSuccessResult();
                } else if ("fire_resistance".equalsIgnoreCase(potionType.getName(""))) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    this.setFireProof(true);
                    return getSuccessResult();
                } else if ("swiftness".equalsIgnoreCase(potionType.getName(""))) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    this.setFast(true);
                    return getSuccessResult();
                } else if ("strength".equalsIgnoreCase(potionType.getName(""))) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    this.setStrong(true);
                    return getSuccessResult();
                } else if ("leaping".equalsIgnoreCase(potionType.getName(""))) {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }
                    this.setCanFly(true);
                    return getSuccessResult();
                }

            }
        } else {
            if (this.getOwnerUUID().equals(player.getUUID()) && this.level.isClientSide()) {
                setOrderedToSit(!this.isInSittingPose() && !this.isOrderedToSit());
                this.setJumping(false);
                this.getNavigation().stop();
                this.setTarget(null);
            }
            return getSuccessResult();
        }
        return InteractionResult.FAIL;
    }

    private InteractionResult getSuccessResult() {
        return InteractionResult.sidedSuccess(!this.level.isClientSide);
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperienceReward(Player player) {
        return 15;
    }

}
