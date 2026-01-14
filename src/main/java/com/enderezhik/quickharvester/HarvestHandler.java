package com.enderezhik.quickharvester;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

//@Mod.EventBusSubscriber(modid = QuickHarvester.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarvestHandler {
    @SubscribeEvent
    public static void onHarvest(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos targetBlockPos = event.getPos();
        InteractionHand hand = event.getHand();
        System.out.println(hand);

        if (level.isClientSide()) {
            return;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }
        if (!player.getMainHandItem().isEmpty()) {
            return;
        }

        if (HarvestScheduler.tasks.containsKey(player.getUUID())) {
            print("update lastTick");
            HarvestScheduler.tasks.get(player.getUUID()).lastTick = ServerTickCounter.currentTick;
            return;
        }
        print("Task not contains");

        BlockState targetBlockState = level.getBlockState(targetBlockPos);
        Block targetBlock = targetBlockState.getBlock();
        if (!isCrop(targetBlock)) {
            return;
        }

        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        HashSet<BlockPos> visited = new HashSet<>();
        ArrayList<BlockPos> toHarvest = new ArrayList<>();

        queue.add(targetBlockPos);
        visited.add(targetBlockPos);

        while(!queue.isEmpty()) {
            BlockPos currentBlockPos = queue.poll();
            BlockState currentBlockState = level.getBlockState(currentBlockPos);
            Block currentBlock = currentBlockState.getBlock();

            if (currentBlock == targetBlock) {
                if (isHarvestableCrop(currentBlock, currentBlockState)) {
                    toHarvest.add(currentBlockPos);
                }

                for (Direction d : Direction.Plane.HORIZONTAL) {
                    BlockPos neighborBlockPos = currentBlockPos.relative(d);

                    if (!visited.contains(neighborBlockPos)) {
                        visited.add(neighborBlockPos);

                        BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                        Block neighborBlock = neighborBlockState.getBlock();
                        if (isCrop(neighborBlock)) {
                            queue.add(neighborBlockPos);
                        }
                    }
                }
            }
        }

        UUID playerUUID = player.getUUID();

        HarvestTask newTask = new HarvestTask(player, level);
        newTask.queue.addAll(toHarvest.reversed());
        newTask.lastTick = ServerTickCounter.currentTick;

        HarvestScheduler.tasks.put(playerUUID, newTask);
        print("Task added");
//        for (BlockPos blockPos : toHarvest) {
//            BlockState blockState = level.getBlockState(blockPos);
//            Block block = blockState.getBlock();
//
//            block.playerDestroy(
//                    level,
//                    player,
//                    blockPos,
//                    blockState,
//                    null,
//                    player.getMainHandItem()
//            );
//            level.removeBlock(blockPos, false);
//        }
    }

    private static boolean isCrop(Block block) {
        return block instanceof CropBlock;
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

    private static void print(String text) {
//        System.out.println(text);
    }
}
