package com.enderezhik.quickharvester.tasks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class QuickHarvestTask extends IHarvestTask {
    public QuickHarvestTask(Player player, Level level) {
        super(player, level);
    }

    @Override
    public void Harvest(java.util.Iterator<java.util.Map.Entry<java.util.UUID, IHarvestTask>> iterator) {
        var blocksIterator = blocksToHarvest.iterator();

        while (blocksIterator.hasNext()) {
            var blockPos = blocksIterator.next();
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
            blocksIterator.remove();
        }
    }
}
