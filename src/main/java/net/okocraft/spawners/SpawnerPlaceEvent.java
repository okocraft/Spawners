package net.okocraft.spawners;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * スポナーが設置された際に発火されるイベント。
 */
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

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * 設置されたスポナーを返す。
     *
     * @return 設置されたスポナー
     */
    public CreatureSpawner getSpawner() {
        return spawner;
    }
}
