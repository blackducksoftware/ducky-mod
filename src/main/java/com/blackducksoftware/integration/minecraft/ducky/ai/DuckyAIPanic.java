/**
 * Copyright (C) 2019 Synopsys, Inc.
 * https://www.synopsys.com/
 *
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
package com.blackducksoftware.integration.minecraft.ducky.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DuckyAIPanic extends Goal {
    protected final EntityDucky ducky;
    protected final double speed;
    protected double randPosX;
    protected double randPosY;
    protected double randPosZ;

    public DuckyAIPanic(EntityDucky ducky, double speedIn) {
        this.ducky = ducky;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (ducky.isBurning()) {
            BlockPos blockpos = this.getRandomWaterPosition(ducky.world, ducky, 20, 6);
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
        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(ducky, 5, 4);

        if (vec3d == null) {
            return false;
        } else {
            this.randPosX = vec3d.x;
            this.randPosY = vec3d.y;
            this.randPosZ = vec3d.z;
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
    public boolean shouldContinueExecuting() {
        return !ducky.getNavigator().noPath();
    }

    @Nullable
    private BlockPos getRandomWaterPosition(World worldIn, Entity entityIn, int horizontalRange, int verticalRange) {
        BlockPos blockpos = new BlockPos(entityIn);
        int x = blockpos.getX();
        int y = blockpos.getY();
        int z = blockpos.getZ();
        float f = horizontalRange * horizontalRange * verticalRange * 2;
        BlockPos randomPosition = null;
        BlockPos.Mutable mutablePosition = new BlockPos.Mutable();

        for (int l = x - horizontalRange; l <= x + horizontalRange; ++l) {
            for (int i1 = y - verticalRange; i1 <= y + verticalRange; ++i1) {
                for (int j1 = z - horizontalRange; j1 <= z + horizontalRange; ++j1) {
                    mutablePosition.setPos(l, i1, j1);
                    BlockState blockstate = worldIn.getBlockState(mutablePosition);

                    if (blockstate.getMaterial() == Material.WATER) {
                        float f1 = (l - x) * (l - x) + (i1 - y) * (i1 - y) + (j1 - z) * (j1 - z);

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
