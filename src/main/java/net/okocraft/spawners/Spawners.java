package net.okocraft.spawners;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import net.okocraft.spawners.listeners.CommandListener;
import net.okocraft.spawners.listeners.EntityListener;
import net.okocraft.spawners.listeners.SpawnerListener;
import net.okocraft.spawners.stack.MobStacker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawners extends JavaPlugin {
    private static Spawners INSTANCE;

    public Spawners() {
        super();
        INSTANCE = this;
    }

    public static Spawners get() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Configuration.get();
        Messages.get();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MobStacker.get();
        BukkitUtil.registerEvents(EntityListener.get(), this);
        BukkitUtil.registerEvents(SpawnerListener.get(), this);
        BukkitUtil.setCommandExecutor(getCommand("spawner"), CommandListener.get());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        BukkitUtil.unregisterEvents(this);
        Bukkit.getScheduler().cancelTasks(this);
    }
}