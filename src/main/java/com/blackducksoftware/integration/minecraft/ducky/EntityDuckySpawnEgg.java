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

import com.blackducksoftware.integration.minecraft.DuckyModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDuckySpawnEgg extends EntityEgg {

    public EntityDuckySpawnEgg(final World world) {
        super(world);
    }

    public EntityDuckySpawnEgg(final World world, final EntityLivingBase entity) {
        super(world, entity);
    }

    public EntityDuckySpawnEgg(final World world, final double x, final double y, final double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(final RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote) {
            final EntityDucky entityDucky = new EntityDucky(this.world);
            entityDucky.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.world.spawnEntity(entityDucky);
        }

        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D,
                new int[] { Item.getIdFromItem(DuckyModItems.egg) });

        if (!this.world.isRemote) {
            this.setDead();
        }

    }

}
