package net.okocraft.spawners.listeners;

import com.github.siroshun09.sirolibrary.bukkitutils.ItemUtil;
import net.okocraft.spawners.Configuration;
import net.okocraft.spawners.Messages;
import net.okocraft.spawners.Permissions;
import net.okocraft.spawners.stack.MobStacker;
import net.okocraft.spawners.stack.StackKill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener implements CommandExecutor, TabCompleter {
    private final static CommandListener INSTANCE = new CommandListener();
    private final List<String> subCommands = Arrays.asList("killall", "stackkill", "get", "reload");
    private final List<String> mobTypeList = Arrays.stream(EntityType.values()).filter(EntityType::isSpawnable).map(EntityType::name).collect(Collectors.toList());

    private CommandListener() {
    }

    @NotNull
    public static CommandListener get() {
        return INSTANCE;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (0 < args.length) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission(Permissions.RELOAD)) {
                    Configuration.get().reload();
                    Messages.get().reload();
                    Messages.get().sendReloaded(sender);
                } else {
                    Messages.get().sendNoPermission(sender, Permissions.RELOAD);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("killall")) {
                if (sender.hasPermission(Permissions.KILLALL)) {
                    killAll(sender);
                } else {
                    Messages.get().sendNoPermission(sender, Permissions.KILLALL);
                }
                return true;
            }

            if (!(sender instanceof Player)) {
                return true;
            }

            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("stackkill")) {
                if (player.hasPermission(Permissions.STACKKILL)) {
                    StackKill.get().toggle(player);
                } else {
                    Messages.get().sendNoPermission(sender, Permissions.STACKKILL);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("get")) {
                if (player.hasPermission(Permissions.GET)) {
                    if (2 <= args.length) {
                        giveSpawner(player, args[1]);
                    } else {
                        Messages.get().sendGetCMDHelp(player);
                    }
                } else {
                    Messages.get().sendNoPermission(sender, Permissions.GET);
                }
                return true;
            }
        }
        Messages.get().sendCMDHelp(sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], subCommands, new ArrayList<>());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            return StringUtil.copyPartialMatches(args[1], mobTypeList, new ArrayList<>());
        }

        return new ArrayList<>();
    }

    private void killAll(CommandSender sender) {
        int i = 0;
        for (String world : Configuration.get().getEnabledWorlds()) {
            World w = Bukkit.getWorld(world);
            if (w != null) {
                for (LivingEntity entity : w.getLivingEntities()) {
                    if (entity instanceof Mob) {
                        if (MobStacker.get().isFromSpawner((Mob) entity)) {
                            entity.remove();
                            i++;
                        }
                    }
                }
            }
        }
        Messages.get().sendKilledAll(sender, i);
    }

    private void giveSpawner(Player player, String type) {
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type);
        } catch (IllegalArgumentException e) {
            Messages.get().sendInvalidType(player, type);
            return;
        }

        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        if (spawner.getItemMeta() == null) {
            return;
        }
        spawner.setItemMeta(ItemUtil.setLore(spawner.getItemMeta(), "&e" + entityType.name()));

        if (player.getInventory().addItem(spawner).isEmpty()) {
            Messages.get().sendGetSpawner(player, entityType);
        } else {
            Messages.get().sendFullInventory(player);
        }
    }
}
