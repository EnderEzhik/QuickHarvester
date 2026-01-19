package com.enderezhik.quickharvester.tasks;

import com.enderezhik.quickharvester.ServerTickCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SlowHarvestTask extends IHarvestTask {
    private final int maxSkipTicks = 10;
    private final int tickCooldownDefault = 5;
    private int tickCooldown;
    public long lastTick;

    public SlowHarvestTask(Player player, Level level) {
        super(player, level);
        this.tickCooldown = tickCooldownDefault;
        this.lastTick = ServerTickCounter.currentTick;
    }

    @Override
    public void Harvest(java.util.Iterator<java.util.Map.Entry<java.util.UUID, IHarvestTask>> iterator) {
        if (blocksToHarvest.isEmpty()) {
            iterator.remove();
            return;
        }

        if (ServerTickCounter.currentTick - lastTick > maxSkipTicks) {
            iterator.remove();
            return;
        }

        if (tickCooldown > 0) {
            tickCooldown--;
            return;
        }

        tickCooldown = tickCooldownDefault;

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
    }
}
