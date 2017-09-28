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

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class DuckyAIFollowOwner extends AbstractDuckyMoveAttack {
    private final float minDistance;
    private final float maxDistance;

    public DuckyAIFollowOwner(final EntityDucky ducky, final float minDistance, final float maxDistance) {
        super(ducky);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase owner = getDucky().getOwner();
        if (owner == null || getDucky().isSitting() || getDucky().isAttacking()) {
            return false;
        } else if (owner instanceof EntityPlayer && ((EntityPlayer) owner).isSpectator()) {
            return false;
        }
        distanceToTarget = getDucky().getDistanceSqToEntity(owner);
        if (distanceToTarget > maxDistance * maxDistance && !needToFly(owner)) {
            setTargetToFollow(owner);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        if (getDucky().isSitting() || getDucky().isAttacking() || getDucky().getNavigator().noPath()) {
            getDucky().getNavigator().clearPathEntity();
            return false;
        }
        if (getDucky().getDistanceSqToEntity(getTargetToFollow()) > minDistance * minDistance && !needToFly(getTargetToFollow())) {
            return true;
        }
        getDucky().getNavigator().clearPathEntity();
        return false;
    }

    @Override
    public void updateTask() {
        if (!updateCalc(distanceToTarget)) {
            return;
        }
        final double speedModifier = getSpeedModifier(distanceToTarget);
        getDucky().getNavigator().tryMoveToEntityLiving(getTargetToFollow(), speedModifier);

        if (getDucky().getNavigator().getPath() != null) {
            if (!getDucky().getLeashed() && !getDucky().isSitting()) {
                if (distanceToTarget >= 144.0D) {
                    final int i = MathHelper.floor_double(getTargetToFollow().posX) - 2;
                    final int j = MathHelper.floor_double(getTargetToFollow().posZ) - 2;
                    final int k = MathHelper.floor_double(getTargetToFollow().getEntityBoundingBox().minY);

                    for (int l = 0; l <= 4; ++l) {
                        for (int i1 = 0; i1 <= 4; ++i1) {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && getDucky().worldObj.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1))
                                    && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                getDucky().setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, getDucky().rotationYaw, getDucky().rotationPitch);
                                getDucky().getNavigator().clearPathEntity();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
