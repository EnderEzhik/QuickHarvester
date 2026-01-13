//package com.enderezhik.quickharvester;
//
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//
//public class test {
//    public static void TestEventHandler(PlayerEvent.HarvestCheck event){
//        if (event.canHarvest()) {
//            var block = event.getTargetBlock().getBlock();
//            var blockName = block.getName();
//            Player player = event.getEntity();
//            player.displayClientMessage(Component.literal(blockName.getString()), false);
//        }
//    }
//}
