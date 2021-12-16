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

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDuckySpawnEgg extends ThrownEgg {
    public static final String DUCKY_SPAWN_EGG_NAME = "ducky_spawn_egg";

    public EntityDuckySpawnEgg(Level level) {
        super(EntityType.EGG, level);
    }

    public EntityDuckySpawnEgg(Level level, LivingEntity entity) {
        super(level, entity);
    }

    public EntityDuckySpawnEgg(EntityType<? extends EntityDuckySpawnEgg> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                ItemParticleOption particleOption = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(DuckyModItems.DUCKY_SPAWN_EGG));
                this.level.addParticle(particleOption, this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D,
                    (this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        if (this.level.isClientSide()) {
            EntityDucky entityDucky = new EntityDucky(this.level);
            entityDucky.setPos(this.getX(), this.getY(), this.getZ());
            entityDucky.setXRot(this.getXRot());
            entityDucky.setYRot(this.getYRot());
            this.level.addFreshEntity(entityDucky);

            this.discard();
        }
    }

}
