/*
 * ducky-mod
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackducksoftware.integration.minecraft.ducky;

import javax.annotation.Nonnull;

import com.blackducksoftware.integration.minecraft.DuckyModItems;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDuckySpawnEgg extends EggEntity {
    public static final String DUCKY_SPAWN_EGG_NAME = "ducky_spawn_egg";

    public EntityDuckySpawnEgg(World world) {
        super(EntityType.EGG, world);
    }

    public EntityDuckySpawnEgg(World world, LivingEntity entity) {
        super(world, entity);
    }

    public EntityDuckySpawnEgg(EntityType<? extends EntityDuckySpawnEgg> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                ItemParticleData particleData = new ItemParticleData(ParticleTypes.ITEM, new ItemStack(DuckyModItems.DUCKY_SPAWN_EGG));
                this.world.addParticle(particleData, this.getPosX(), this.getPosY(), this.getPosZ(), (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D,
                    (this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            // this.getThrower == this.func_234616_v_
            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0.0F);
        }
        if (!this.world.isRemote) {
            EntityDucky entityDucky = new EntityDucky(this.world);
            entityDucky.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            this.world.addEntity(entityDucky);

            this.remove();
        }
    }

}
