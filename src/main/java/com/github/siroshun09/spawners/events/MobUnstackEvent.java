package com.github.siroshun09.spawners.events;

import org.bukkit.entity.Mob;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class MobUnstackEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private final Mob target;
    private int amount;
    private boolean isCancelled;


    public MobUnstackEvent(@NotNull Mob target, int amount) {
        super(target);
        this.target = target;
        this.amount = amount;
        isCancelled = false;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Mob getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int i) {
        amount = i;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }
}
