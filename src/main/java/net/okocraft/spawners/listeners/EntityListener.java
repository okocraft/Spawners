package net.okocraft.spawners.listeners;

import net.okocraft.spawners.Configuration;
import net.okocraft.spawners.stack.MobStacker;
import net.okocraft.spawners.stack.StackKill;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityListener implements Listener {
    private static EntityListener instance;

    private EntityListener() {
        instance = this;
    }

    public static EntityListener get() {
        if (instance == null) {
            new EntityListener();
        }
        return instance;
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
