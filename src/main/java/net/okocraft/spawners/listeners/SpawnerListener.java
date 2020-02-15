package net.okocraft.spawners.listeners;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import net.okocraft.spawners.Configuration;
import net.okocraft.spawners.Messages;
import net.okocraft.spawners.Spawners;
import net.okocraft.spawners.events.SpawnerPlaceEvent;
import net.okocraft.spawners.stack.MobStacker;
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

import java.util.Optional;

public class SpawnerListener implements Listener {
    private final static SpawnerListener INSTANCE = new SpawnerListener();

    private SpawnerListener() {
    }

    @NotNull
    public static SpawnerListener get() {
        return INSTANCE;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSlimeSpawnerHold(@NotNull PlayerItemHeldEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();

        ItemStack spawner = player.getInventory().getItem(e.getNewSlot());
        if (spawner == null || !spawner.getType().equals(Material.SPAWNER)) {
            return;
        }

        Optional<EntityType> type = getEntityTypeFromLore(spawner);
        if (type.isEmpty() || !type.get().equals(EntityType.SLIME)) {
            return;
        }

        if (player.getWorld().getChunkAt(player.getLocation()).isSlimeChunk()) {
            Messages.get().sendSlimeChunk(player);
        } else {
            Messages.get().sendNotSlimeChunk(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(@NotNull BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (!e.getBlockPlaced().getType().equals(Material.SPAWNER)) {
            return;
        }

        ItemStack itemInHand = e.getItemInHand();
        if (!itemInHand.getType().equals(Material.SPAWNER)) {
            return;
        }

        Optional<EntityType> type = getEntityTypeFromLore(itemInHand);
        if (type.isEmpty()) {
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) e.getBlockPlaced().getState();
        spawner.setSpawnedType(type.get());
        spawner.update();

        BukkitUtil.callEvent(new SpawnerPlaceEvent(e.getPlayer(), spawner));

        Messages.get().sendPlaced(e.getPlayer(), type.get());
        Messages.get().sendSpawnerTips(e.getPlayer());
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(@NotNull SpawnerSpawnEvent e) {
        if (e.isCancelled()) {
            return;
        }

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

    @NotNull
    private Optional<EntityType> getEntityTypeFromLore(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }

        if (meta.getLore() == null || meta.getLore().size() < 1) {
            return Optional.empty();
        }

        String lore1 = ChatColor.stripColor(meta.getLore().get(0).toUpperCase());
        try {
            return Optional.of(EntityType.valueOf(lore1));
        } catch (IllegalArgumentException ex) {
            Spawners.get().getLogger().warning("不明なエンティティ名: " + lore1);
            return Optional.empty();
        }
    }
}