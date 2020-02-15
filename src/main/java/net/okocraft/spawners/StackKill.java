package net.okocraft.spawners;

import com.github.siroshun09.sirolibrary.message.BukkitMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class StackKill {
    private final static StackKill INSTANCE = new StackKill();

    private final Set<Player> enabled = new HashSet<>();

    private StackKill() {
    }

    @NotNull
    public static StackKill get() {
        return INSTANCE;
    }

    public boolean isEnabled(@NotNull Player player) {
        return enabled.contains(player);
    }

    public void toggle(@NotNull Player player) {
        if (!player.hasPermission("mobmanager.stackkill")) {
            BukkitMessage.sendMessageWithColor(player, "&7* そのコマンドを実行する権限がありません。");
            return;
        }

        if (enabled.contains(player)) {
            enabled.remove(player);
        } else {
            enabled.add(player);
        }

        Messages.get().sendStackKillToggled(player, (enabled.contains(player) ? "ON" : "OFF"));
    }
}
