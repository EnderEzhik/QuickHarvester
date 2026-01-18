package com.enderezhik.quickharvester.tasks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayDeque;

public class IHarvestTask {
    public final Player player;
    public final Level level;
    public final ArrayDeque<BlockPos> blocksToHarvest = new ArrayDeque<>();

    public IHarvestTask(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    public void Harvest(java.util.Iterator<java.util.Map.Entry<java.util.UUID, IHarvestTask>> iterator) {
        throw new NotImplementedException();
    }
}
