package com.enderezhik.quickharvester;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod(QuickHarvester.MOD_ID)
public final class QuickHarvester {
    public static final String MOD_ID = "quickharvester";

    public QuickHarvester(FMLJavaModLoadingContext context) {
        PlayerInteractEvent.RightClickBlock.BUS.addListener(HarvestHandler::onHarvest);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
