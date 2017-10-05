package com.blackducksoftware.integration.minecraft;

import com.blackducksoftware.integration.minecraft.proxies.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DuckyMod.MODID, version = DuckyMod.VERSION)
public class DuckyMod {
    public static final String MODID = "duckymod";
    public static final String VERSION = "0.0.1";

    @SidedProxy(modId = MODID, clientSide = "com.blackducksoftware.integration.minecraft.proxies.ClientProxy", serverSide = "com.blackducksoftware.integration.minecraft.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(DuckyMod.MODID)
    public static DuckyMod instance;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        DuckyModItems.mainRegistry();
        DuckyModSounds.mainRegistry();
        proxy.registerEntities();
        proxy.preInitRenders();
        proxy.initEvents();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        // GameRegistry.addRecipe(new ItemStack(DuckyModItems.egg), new Object[] { "OF ", "O F", "OF ", 'F', Items.FEATHER, 'O', Blocks.OBSIDIAN });
        // GameRegistry.addShapedRecipe(name, group, output, params);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }

}
