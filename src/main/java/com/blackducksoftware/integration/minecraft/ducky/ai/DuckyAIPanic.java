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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DuckyAIPanic extends EntityAIBase {
    protected final EntityDucky ducky;
    protected final double speed;
    protected double randPosX;
    protected double randPosY;
    protected double randPosZ;

    public DuckyAIPanic(final EntityDucky ducky, final double speedIn) {
        this.ducky = ducky;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (ducky.isBurning()) {
            final BlockPos blockpos = this.getRandomWaterPosition(ducky.worldObj, ducky, 15, 6);
            if (blockpos != null) {
                this.randPosX = blockpos.getX();
                this.randPosY = blockpos.getY();
                this.randPosZ = blockpos.getZ();
                ducky.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
                return true;
            } else if (findAndSetRandomPosition()) {
                ducky.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
            }
        }
        return false;
    }

    protected boolean findAndSetRandomPosition() {
        final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(ducky, 5, 4);

        if (vec3d == null) {
            return false;
        } else {
            this.randPosX = vec3d.xCoord;
            this.randPosY = vec3d.yCoord;
            this.randPosZ = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        ducky.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        return !ducky.getNavigator().noPath();
    }

    @Nullable
    private BlockPos getRandomWaterPosition(final World worldIn, final Entity entityIn, final int horizontalRange, final int verticalRange) {
        final BlockPos blockpos = new BlockPos(entityIn);
        final int x = blockpos.getX();
        final int y = blockpos.getY();
        final int z = blockpos.getZ();
        float f = horizontalRange * horizontalRange * verticalRange * 2;
        BlockPos randomPosition = null;
        final BlockPos.MutableBlockPos mutablePosition = new BlockPos.MutableBlockPos();

        for (int l = x - horizontalRange; l <= x + horizontalRange; ++l) {
            for (int i1 = y - verticalRange; i1 <= y + verticalRange; ++i1) {
                for (int j1 = z - horizontalRange; j1 <= z + horizontalRange; ++j1) {
                    mutablePosition.setPos(l, i1, j1);
                    final IBlockState iblockstate = worldIn.getBlockState(mutablePosition);

                    if (iblockstate.getMaterial() == Material.WATER) {
                        final float f1 = (l - x) * (l - x) + (i1 - y) * (i1 - y) + (j1 - z) * (j1 - z);

                        if (f1 < f) {
                            f = f1;
                            randomPosition = new BlockPos(mutablePosition);
                            break;
                        }
                    }
                }
            }
        }

        return randomPosition;
    }
}
