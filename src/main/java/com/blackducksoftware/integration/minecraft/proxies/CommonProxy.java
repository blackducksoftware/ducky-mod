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
package com.blackducksoftware.integration.minecraft.proxies;

import com.blackducksoftware.integration.minecraft.DuckyMod;
import com.blackducksoftware.integration.minecraft.ducky.EntityDucky;
import com.blackducksoftware.integration.minecraft.ducky.EntityDuckySpawnEgg;
import com.blackducksoftware.integration.minecraft.ducky.ItemDuckySpawnEgg;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {

    protected static int entityCount;

    public void preInitRenders() {

    }

    public void initEvents() {
    }

    public void registerEntities() {
        entityCount = 0;
        register(EntityDucky.class, EntityDucky.DUCKY_NAME);
        register(EntityDuckySpawnEgg.class, ItemDuckySpawnEgg.DUCKY_EGG_NAME);
    }

    /** registers the entity **/
    protected static void register(final Class entityClass, final String name) {
        EntityRegistry.registerModEntity(entityClass, name, ++entityCount, DuckyMod.instance, 16 * 4, 3, true);
    }
}
