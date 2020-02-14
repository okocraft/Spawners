package net.okocraft.spawners.events;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class SpawnerPlaceEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final CreatureSpawner spawner;

    public SpawnerPlaceEvent(@NotNull Player player, @NotNull CreatureSpawner spawner) {
        super(player);
        this.spawner = spawner;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public CreatureSpawner getSpawner() {
        return spawner;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }
}
