package com.enderezhik.quickharvester.tasks;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class QuickHarvestTask extends IHarvestTask {
    private final int maxHarvestBlocksInTick = 64;

    public QuickHarvestTask(Player player, Level level) { super(player, level); }

    @Override
    public void Harvest(java.util.Iterator<java.util.Map.Entry<java.util.UUID, IHarvestTask>> iterator) {
        int completedBlocks = 0;

        while (!blocksToHarvest.isEmpty()) {
            if (completedBlocks >= maxHarvestBlocksInTick) {
                return;
            }
            var blockPos = blocksToHarvest.poll();
            BlockState blockState = level.getBlockState(blockPos);
            Block block = blockState.getBlock();

            block.playerDestroy(
                    level,
                    player,
                    blockPos,
                    blockState,
                    null,
                    ItemStack.EMPTY
            );
            level.removeBlock(blockPos, false);
            completedBlocks++;
        }
        iterator.remove();
    }
}
