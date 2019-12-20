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
package com.blackducksoftware.integration.minecraft.ducky.pathfinding;

import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;

import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.world.Region;
import net.minecraft.world.World;

public class DuckyPathNavigateFlying extends FlyingPathNavigator {
    public DuckyPathNavigateFlying(EntityDucky ducky, World world) {
        super(ducky, world);
    }

    @Override
    protected PathFinder getPathFinder(int var) {
        this.nodeProcessor = new DuckyFlyingNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);

        Region region = new Region(this.world, this.entity.getPosition().add(-32, -32, -32), this.entity.getPosition().add(32, 32, 32));

        //func_225578_a_ == init
        this.nodeProcessor.func_225578_a_(region, this.entity);
        return new PathFinder(this.nodeProcessor, var);
    }

}
