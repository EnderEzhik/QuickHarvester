package com.enderezhik.quickharvester.tasks;

import com.enderezhik.quickharvester.ServerTickCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SlowHarvestTask extends IHarvestTask {
    public int tickCooldownDefault = 20;
    public int tickCooldown;
    public long lastTick;

    public SlowHarvestTask(Player player, Level level) {
        super(player, level);
        this.tickCooldown = tickCooldownDefault;
        this.lastTick = ServerTickCounter.currentTick;
    }

    @Override
    public void Harvest(java.util.Iterator<java.util.Map.Entry<java.util.UUID, IHarvestTask>> iterator) {
        if (ServerTickCounter.currentTick - lastTick > 5) {
            iterator.remove();
            return;
        }

        if (tickCooldown > 0) {
            tickCooldown--;
            return;
        }

        tickCooldown = tickCooldownDefault;

        if (blocksToHarvest.isEmpty()) {
            iterator.remove();
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
                player.getMainHandItem()
        );
        level.removeBlock(blockPos, false);
    }
}
