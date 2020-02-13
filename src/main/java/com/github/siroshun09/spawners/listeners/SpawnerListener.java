package com.github.siroshun09.spawners.listeners;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import com.github.siroshun09.spawners.Configuration;
import com.github.siroshun09.spawners.Messages;
import com.github.siroshun09.spawners.events.SpawnerPlaceEvent;
import com.github.siroshun09.spawners.stack.MobStacker;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SpawnerListener implements Listener {
    private static SpawnerListener instance;

    private SpawnerListener() {
        instance = this;
    }

    public static SpawnerListener get() {
        if (instance == null) {
            new SpawnerListener();
        }
        return instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlimeSpawnerHold(@NotNull PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack spawner = player.getInventory().getItem(event.getNewSlot());
        if (spawner == null || spawner.getType() != Material.SPAWNER) {
            return;
        }

        ItemMeta spawnerMeta = spawner.getItemMeta();
        if (spawnerMeta == null) {
            return;
        }

        if (spawnerMeta.getLore() == null || spawnerMeta.getLore().size() < 1) {
            return;
        }

        EntityType type;
        try {
            type = EntityType.valueOf(ChatColor.stripColor(spawnerMeta.getLore().get(0).toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return;
        }

        if (type != EntityType.SLIME) {
            return;
        }
        
        if (player.getLocation().getChunk().isSlimeChunk()) {
            Messages.get().sendSlimeChunk(player);
        } else {
            Messages.get().sendNotSlimeChunk(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(@NotNull BlockPlaceEvent e) {

        if (!e.getBlockPlaced().getType().equals(Material.SPAWNER)) {
            return;
        }

        ItemStack itemInHand = e.getItemInHand();

        if (!itemInHand.getType().equals(Material.SPAWNER)) {
            return;
        }

        ItemMeta spawnerMeta = itemInHand.getItemMeta();
        if (spawnerMeta == null) {
            return;
        }

        if (spawnerMeta.getLore() == null || spawnerMeta.getLore().size() < 1) {
            return;
        }

        EntityType type;
        try {
            type = EntityType.valueOf(ChatColor.stripColor(spawnerMeta.getLore().get(0).toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) e.getBlockPlaced().getState();
        spawner.setSpawnedType(type);
        spawner.update();

        BukkitUtil.callEvent(new SpawnerPlaceEvent(e.getPlayer(), spawner));

        Messages.get().sendPlaced(e.getPlayer(), type);
        Messages.get().sendSpawnerTips(e.getPlayer());
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(@NotNull SpawnerSpawnEvent e) {
        if (!Configuration.get().isEnabledWorld(e.getEntity().getWorld())) {
            e.setCancelled(true);
            return;
        }

        if (e.getSpawner().getBlock().isBlockPowered() || e.getSpawner().getBlock().isBlockIndirectlyPowered()) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Mob) {

            MobStacker.get().setFromSpawner((Mob) e.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChange(@NotNull PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.SPAWNER)) {
            return;
        }

        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = e.getItem();

        if (item == null) {
            return;
        }

        if (item.getType().name().contains("SPAWN_EGG")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(@NotNull PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.SPAWNER)) {
            return;
        }

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if ((e.getHand() != null && !e.getHand().equals(EquipmentSlot.HAND))) {
            return;
        }

        if (e.getClickedBlock().isBlockPowered() || e.getClickedBlock().isBlockIndirectlyPowered()) {
            Messages.get().sendSpawnerOFF(e.getPlayer());
        } else {
            Messages.get().sendSpawnerON(e.getPlayer());
        }
    }
}
