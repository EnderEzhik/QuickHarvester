package com.enderezhik.quickharvester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;

public class HarvestTask {
    public final Player player;
    public final Level level;
    public final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
    public int tickCooldown = 20; // 1 секунда (20 тиков)

    public HarvestTask(Player player, Level level) {
        this.player = player;
        this.level = level;
    }
}

