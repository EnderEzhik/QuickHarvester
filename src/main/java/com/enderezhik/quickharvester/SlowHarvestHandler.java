package com.enderezhik.quickharvester;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QuickHarvester.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SlowHarvestHandler {
    @SubscribeEvent
    public static void serverTickHandler(TickEvent.ServerTickEvent.Post event) {
        if (HarvestScheduler.tasks.isEmpty()) {
            System.out.println("tasks is empty");
            return;
        }

        var iterator = HarvestScheduler.tasks.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            HarvestTask task = entry.getValue();
            if (ServerTickCounter.currentTick - task.lastTick > 5) {
                iterator.remove();
                System.out.println("remove task");
                continue;
            }

            System.out.println(task.tickCooldown);

            if (task.tickCooldown > 0) {
                task.tickCooldown--;
                continue;
            }

            System.out.println("Harvesting");

            task.tickCooldown = task.tickCooldownDefault;

            System.out.println("two");

            if (task.queue.isEmpty()) {
                iterator.remove();
                continue;
            }

            System.out.println("tri");

            var blockPos = task.queue.poll();
            BlockState blockState = task.level.getBlockState(blockPos);
            Block block = blockState.getBlock();

            block.playerDestroy(
                    task.level,
                    task.player,
                    blockPos,
                    blockState,
                    null,
                    task.player.getMainHandItem()
            );
            task.level.removeBlock(blockPos, false);
        }
        ServerTickCounter.currentTick++;
    }

    private static void print(Level level, String text) {
        System.out.println(level.getGameTime() + " :: " + text);
    }
}
