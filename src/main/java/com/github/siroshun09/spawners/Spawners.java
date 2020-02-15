package com.github.siroshun09.spawners;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import com.github.siroshun09.spawners.listeners.CommandListener;
import com.github.siroshun09.spawners.listeners.EntityListener;
import com.github.siroshun09.spawners.listeners.SpawnerListener;
import com.github.siroshun09.spawners.stack.MobStacker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawners extends JavaPlugin {
    private static Spawners instance;

    public Spawners() {
        super();
        instance = this;
    }

    public static Spawners get() {
        return instance;
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