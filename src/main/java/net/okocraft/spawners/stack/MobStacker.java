package net.okocraft.spawners.stack;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import net.okocraft.spawners.Configuration;
import net.okocraft.spawners.Spawners;
import net.okocraft.spawners.events.MobStackEvent;
import net.okocraft.spawners.events.MobUnstackEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MobStacker {
    private final static MobStacker INSTANCE = new MobStacker();

    private final NamespacedKey stackKey = new NamespacedKey(Spawners.get(), "stacked_amount");
    private final NamespacedKey fromSpawnerkey = new NamespacedKey(Spawners.get(), "from_spawner");

    private String mcMMOMetaKey;
    private FixedMetadataValue mcMMOMetaValue;

    private MobStacker() {
        Plugin mcMMO = Bukkit.getPluginManager().getPlugin("mcMMO");
        if (mcMMO != null) {
            mcMMOMetaKey = "mcMMO: Spawned Entity";
            mcMMOMetaValue = new FixedMetadataValue(mcMMO, true);
        }

        BukkitUtil.runTimer(Spawners.get(), new StackTask(), 20, Configuration.get().getStackInterval());
        BukkitUtil.runTimer(Spawners.get(), new CheckTask(), 20, Configuration.get().getStackInterval());
    }

    @NotNull
    public static MobStacker get() {
        return INSTANCE;
    }

    public void stack(@NotNull Mob target, @NotNull Mob mob) {
        int newAmount = getStackedAmount(target) + getStackedAmount(mob);
        if (Configuration.get().getMaxStackAmount() < newAmount || newAmount < 1) {
            return;
        }

        MobStackEvent e = new MobStackEvent(target, mob, newAmount);
        BukkitUtil.callEvent(e);

        if (e.isCancelled()) {
            return;
        }

        setStackedAmount(e.getTarget(), e.getAmount());
        setMcMMOMeta(e.getTarget());
        setCustomName(e.getTarget());
        e.getStacked().remove();
    }

    public void unstackOne(@NotNull Mob mob) {
        int stacked = getStackedAmount(mob);
        if (stacked <= 1) {
            return;
        }

        MobUnstackEvent e = new MobUnstackEvent(mob, stacked - 1);
        BukkitUtil.callEvent(e);

        if (e.isCancelled()) {
            return;
        }

        Mob copiedMob = (Mob) e.getTarget().getWorld().spawnEntity(e.getTarget().getLocation(), e.getTarget().getType());
        setStackedAmount(copiedMob, e.getAmount());
        setMcMMOMeta(copiedMob);
        setFromSpawner(copiedMob);
        setCustomName(copiedMob);
    }

    public void setCustomName(@NotNull Mob mob) {
        mob.setCustomName(Configuration.get().getStackedMobName(getStackedAmount(mob), mob.getType()));
        mob.setCustomNameVisible(true);
    }

    public boolean isNotStacked(@NotNull Mob mob) {
        return getStackedAmount(mob) <= 1;
    }

    public int getStackedAmount(@NotNull Mob mob) {
        Integer amount = mob.getPersistentDataContainer().get(stackKey, PersistentDataType.INTEGER);
        return amount == null ? 1 : amount;
    }

    private void setStackedAmount(@NotNull Mob mob, int amount) {
        mob.getPersistentDataContainer().set(stackKey, PersistentDataType.INTEGER, amount);
    }

    public boolean isFromSpawner(@NotNull Mob mob) {
        String str = mob.getPersistentDataContainer().get(fromSpawnerkey, PersistentDataType.STRING);
        if (str == null) {
            return false;
        }
        return str.equals("true");
    }

    public void setFromSpawner(@NotNull Mob mob) {
        mob.getPersistentDataContainer().set(fromSpawnerkey, PersistentDataType.STRING, "true");
    }

    void setMcMMOMeta(@NotNull Mob mob) {
        if (mcMMOMetaKey == null || mcMMOMetaValue == null) {
            return;
        }

        if (mob.hasMetadata(mcMMOMetaKey)) {
            return;
        }

        mob.setMetadata(mcMMOMetaKey, mcMMOMetaValue);
    }
}