package com.enderezhik.quickharvester;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;

public class HarvestTask {
    public final Player player;
    public final Level level;
    public final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
    public int tickCooldownDefault = 20;
    public int tickCooldown;
    public long lastTick;

    public HarvestTask(Player player, Level level) {
        this.player = player;
        this.level = level;
        this.tickCooldown = tickCooldownDefault;
    }
}
