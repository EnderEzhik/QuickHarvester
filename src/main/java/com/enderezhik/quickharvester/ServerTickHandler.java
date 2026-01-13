package com.enderezhik.quickharvester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QuickHarvester.MOD_ID)
public class ServerTickHandler {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        event.
        var iterator = HarvestScheduler.tasks.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            HarvestTask task = entry.getValue();

            // если игрок вышел или мир недоступен
            if (task.player == null || !task.player.isAlive()) {
                iterator.remove();
                continue;
            }

            // ждём таймер
            if (--task.tickCooldown > 0)
                continue;

            task.tickCooldown = 20; // снова 1 сек ожидания

            // если очередь пуста — удалить задачу
            if (task.queue.isEmpty()) {
                iterator.remove();
                continue;
            }

            BlockPos pos = task.queue.poll();
            BlockState state = task.level.getBlockState(pos);

            // блок мог исчезнуть за это время → просто пропускаем
            if (state.isAir())
                continue;

            // ломаешь корректно
            state.getBlock().playerDestroy(
                    task.level,
                    task.player,
                    pos,
                    state,
                    null,
                    task.player.getMainHandItem()
            );
            task.level.removeBlock(pos, false);
        }
    }
}

