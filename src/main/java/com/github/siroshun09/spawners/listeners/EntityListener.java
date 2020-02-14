package com.github.siroshun09.spawners.listeners;

import com.github.siroshun09.spawners.Configuration;
import com.github.siroshun09.spawners.stack.MobStacker;
import com.github.siroshun09.spawners.stack.StackKill;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityListener implements Listener {
    private final static EntityListener INSTANCE = new EntityListener();

    private EntityListener() {
    }

    @NotNull
    public static EntityListener get() {
        return INSTANCE;
    }

    @EventHandler
    public void onDeath(@NotNull EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Mob) ||
                Configuration.get().isDisabledMob(e.getEntityType()) ||
                !Configuration.get().isEnabledWorld(e.getEntity().getWorld())) {
            return;
        }

        Mob killed = (Mob) e.getEntity();
        int stacked = MobStacker.get().getStackedAmount(killed);
        if (stacked <= 1) {
            return;
        }

        if (killed.getKiller() != null && StackKill.get().isEnabled(killed.getKiller())) {
            for (ItemStack i : e.getDrops()) {
                i.setAmount(i.getAmount() * stacked);
            }
            e.setDroppedExp(e.getDroppedExp() * stacked);
            return;
        }

        MobStacker.get().unstackOne(killed);
    }

}
