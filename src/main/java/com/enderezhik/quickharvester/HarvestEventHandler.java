package com.enderezhik.quickharvester;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = QuickHarvester.MOD_ID)
public class HarvestEventHandler {
    @SubscribeEvent
    public static void MyOnBlockHarvest(BlockEvent.BreakEvent event) {
        System.out.println("start: " + event.getPos());
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getPlayer();
        BlockPos startPos = event.getPos();
        BlockState startState = event.getState();
        Block targetBlock = startState.getBlock();

        // Проверяем, что начальный блок вообще наш урожай
        if (!isHarvestableCrop(targetBlock, startState)) return;

        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        HashSet<BlockPos> visited = new HashSet<>();
        ArrayList<BlockPos> toHarvest = new ArrayList<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockState state = level.getBlockState(current);
            Block block = state.getBlock();

            if (block == targetBlock && isHarvestableCrop(block, state)) {
                toHarvest.add(current);

                for (Direction d : Direction.Plane.HORIZONTAL) {
                    BlockPos neighbor = current.relative(d);

                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        UUID id = player.getUUID();

        HarvestTask task = new HarvestTask(player, level);
        task.queue.addAll(toHarvest);

        HarvestScheduler.tasks.put(id, task);

        sendHarvestMessage(player, task.queue.size());

//        int harvested = 0;

//        for (BlockPos pos : toHarvest) {
//            BlockState state = level.getBlockState(pos);
//            Block block = state.getBlock();
//
//            if (block == targetBlock && isHarvestableCrop(block, state)) {
//                state.getBlock().playerDestroy(
//                        level,
//                        player,
//                        pos,
//                        state,
//                        null,
//                        player.getMainHandItem()
//                );
//                level.removeBlock(pos, false);
//                harvested++;
//            }
//        }
//
//        sendHarvestMessage(player, harvested);
    }

    private static void BFS(BlockPos startPos, Level level) {

    }

    private static boolean isHarvestableCrop(Block block, BlockState state) {
        if (block instanceof CropBlock crop) {
            return crop.isMaxAge(state);
        }
        return false;
    }

    private static void sendHarvestMessage(Player player, int countHarvested) {
        if (player instanceof ServerPlayer serverPlayer) {
            Component message = Component.literal("")
                    .append(Component.literal("[Урожай] ")
                            .withStyle(ChatFormatting.GREEN))
                    .append(Component.literal("Вы собрали: ")
                            .withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(Integer.toString(countHarvested))
                            .withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(" урожая!")
                            .withStyle(ChatFormatting.WHITE));

            serverPlayer.sendSystemMessage(message);
        }
    }
}
