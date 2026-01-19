package com.enderezhik.quickharvester;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.enderezhik.quickharvester.tasks.IHarvestTask;

@Mod.EventBusSubscriber(modid = QuickHarvester.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTickHandler {
    @SubscribeEvent
    public static void serverTickHandler(TickEvent.ServerTickEvent.Post event) {
        ServerTickCounter.currentTick++;

        if (HarvestScheduler.tasks.isEmpty()) {
            return;
        }

        var iterator = HarvestScheduler.tasks.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            IHarvestTask task = entry.getValue();
            task.Harvest(iterator);
        }
    }
}
