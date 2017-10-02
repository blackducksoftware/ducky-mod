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

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.DuckyModSounds;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIFlyTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.ai.DuckyAIMoveTowardsTargetAndAttack;
import com.blackducksoftware.integration.minecraft.ducky.tamed.EntityTamedDucky;
import com.blackducksoftware.integration.minecraft.ducky.tamed.giant.EntityGiantTamedDucky;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDucky extends EntityTameable {
    public static final double BASE_HEALTH = 15.0D;
    public static final double BASE_DAMAGE = 6.0D;

    public static final String DUCKY_NAME = "bd_ducky";

    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    private boolean isFlying;
    private boolean isAttacking;

    public EntityDucky(final World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.setScale(1.0F);
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(2, new DuckyAIWatchTarget(this, predicate, 32.0F, 5));
        this.tasks.addTask(3, new DuckyAIMoveTowardsTargetAndAttack(this, 32.0F));
        this.tasks.addTask(4, new DuckyAIFlyTowardsTargetAndAttack(this, 32.0F, 32));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityShulker.class, true, false));
        this.tasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityGhast.class, true, false));
        this.targetTasks.addTask(6, new EntityAIHurtByTarget(this, true, EntityMob.class, EntityShulker.class, EntityGhast.class));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BASE_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE);
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    @Override
    public boolean canAttackClass(final Class<? extends EntityLivingBase> cls) {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) (this.destPos + (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }
        this.wingRotDelta = (float) (this.wingRotDelta * 0.9D);
        this.wingRotation += this.wingRotDelta * 2.0F;
        if (!this.onGround && this.motionY < 0.0D && !isFlying()) { // && !this.isInWater() && !this.isInLava()) {
            this.motionY *= 0.6D;
        }
    }

    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean canAttackFrom = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
        if (canAttackFrom) {
            this.applyEnchantments(this, entityIn);
        }
        return canAttackFrom;
    }

    /**
     * Return true if you want to skip processing the other hand
     */
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (isBreedingItem(itemstack)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.worldObj.isRemote) {
                EntityTamedDucky tamedDucky = null;
                if (this.rand.nextInt(9) == 0) {
                    tamedDucky = new EntityGiantTamedDucky(this.worldObj);
                    final ItemStack firework = createFirework();
                    final EntityFireworkRocket rocket = new EntityFireworkRocket(worldObj, this.posX, this.posY, this.posZ, firework);
                    worldObj.spawnEntityInWorld(rocket);
                } else {
                    tamedDucky = new EntityTamedDucky(this.worldObj);
                }
                if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(tamedDucky, player)) {
                    tamedDucky.moveToBlockPosAndAngles(this.getPosition(), 0.0F, 0.0F);
                    tamedDucky.setTamed(true);
                    tamedDucky.navigator.clearPathEntity();
                    tamedDucky.setAttackTarget((EntityLivingBase) null);
                    tamedDucky.setOwnerId(player.getUniqueID());
                    tamedDucky.playTameEffect(true);
                    tamedDucky.worldObj.setEntityState(this, (byte) 7);
                    tamedDucky.onInitialSpawn(tamedDucky.worldObj.getDifficultyForLocation(this.getPosition()), (IEntityLivingData) null);
                    tamedDucky.worldObj.spawnEntityInWorld(tamedDucky);
                    this.setDead();
                } else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }
            return true;
        }
        return false;
    }

    private ItemStack createFirework() {
        final ItemStack firework = new ItemStack(Items.FIREWORKS, 3);

        final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        // explosion
        final NBTTagCompound explosionCompound = new NBTTagCompound();
        final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();

        nbttagcompound2.setBoolean("Flicker", true);
        nbttagcompound2.setBoolean("Trail", true);
        nbttagcompound2.setByte("Type", (byte) 2);
        explosionCompound.setTag("Explosion", nbttagcompound2);

        nbttaglist.appendTag(explosionCompound);

        nbttagcompound3.setTag("Explosions", nbttaglist);
        nbttagcompound3.setByte("Flight", (byte) 1);
        nbttagcompound1.setTag("Fireworks", nbttagcompound3);
        firework.setTagCompound(nbttagcompound1);

        return firework;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            if (this.aiSit != null) {
                setSitting(false);
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean canMove() {
        return !isSitting() && !getLeashed();
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() == Items.BREAD;
    }

    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return DuckyModSounds.duckQuack;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return DuckyModSounds.duckHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DuckyModSounds.duckDeath;
    }

    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public boolean isSitting() {
        final byte tamedByte = this.dataManager.get(TAMED).byteValue();
        final boolean sitting = (tamedByte & 1) != 0;
        return sitting;
    }

    @Override
    public void setSitting(final boolean sitting) {
        this.aiSit.setSitting(sitting);
        final byte tamedByte = this.dataManager.get(TAMED).byteValue();
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte | 1)));
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (tamedByte & -2)));
        }
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return 20;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return false;
    }

    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        return null;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    @Override
    public void setScaleForAge(final boolean child) {
        this.setScale(1.0F);
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(final boolean isFlying) {
        this.isFlying = isFlying;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(final boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}
