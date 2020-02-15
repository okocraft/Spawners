package com.github.siroshun09.spawners;

import com.github.siroshun09.sirolibrary.config.BukkitConfig;
import com.github.siroshun09.sirolibrary.message.BukkitMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

public class Messages extends BukkitConfig {
    private final static Messages INSTANCE = new Messages();

    private Messages() {
        super(Spawners.get(), "messages.yml", true);
    }

    @NotNull
    public static Messages get() {
        return INSTANCE;
    }

    public void sendSlimeChunk(@NotNull Player player) {
        sendMessage(player, getString("spawner.slimechunk", "ここはスライムチャンクです。"));
    }

    public void sendNotSlimeChunk(@NotNull Player player) {
        sendMessage(player, getString("spawner.notslimechunk", "ここはスライムチャンクではありません。"));
    }

    public void sendSpawnerON(@NotNull Player player) {
        sendMessage(player, getString("spawner.on", "赤石信号が入力されていないため、湧き条件を満たすと Mob がスポーンします。"));
    }

    public void sendSpawnerOFF(@NotNull Player player) {
        sendMessage(player, getString("spawner.off", "赤石信号が入力されているため、スポーンが停止しています。"));
    }

    public void sendPlaced(@NotNull Player player, @NotNull EntityType type) {
        sendMessage(player, getString("spawner.placed", "&b%type% &7スポナーを設置しました。").replace("%type%", type.name()));
    }

    public void sendSpawnerTips(@NotNull Player player) {
        sendMessage(player, getString("spawner.tip", "TIPS: 赤石信号で湧き止めできます。"));
    }

    public void sendReloaded(@NotNull CommandSender sender) {
        sendMessage(sender, getString("Command.reload", "設定ファイルを再読込しました。"));
    }

    public void sendKilledAll(@NotNull CommandSender sender, int amount) {
        sendMessage(sender,
                getString("command.killall", "&b%amount%体 &7の Mob を削除しました。").replace("%amount%", String.valueOf(amount)));
    }

    public void sendGetSpawner(@NotNull Player player, @NotNull EntityType type) {
        sendMessage(player, getString("command.get", "&b%type% &7のスポナーを入手しました。").replace("%type%", type.name()));
    }

    public void sendStackKillToggled(@NotNull Player player, @NotNull String onOrOff) {
        sendMessage(player,
                getString("command.stackkill", "スタックキルモードを &b%toggled% &7に切り替えました。").replace("%toggled%", onOrOff));
    }

    public void sendKillAllCMDHelp(@NotNull CommandSender sender) {
        sendMessage(sender, getString("command.help.killall", "&b/spawner killall&8: &7スポナー由来の Mob をすべて削除します。"));
    }

    public void sendGetCMDHelp(@NotNull CommandSender sender) {
        sendMessage(sender, getString("command.help.get", "&b/spawner get <mob type>&8: &7指定した Mob のスポナーを入手します。"));
    }

    public void sendStackKillCMDHelp(@NotNull CommandSender sender) {
        sendMessage(sender, getString("command.help.stackkill", "&b/spawner stackkill&8: &7スタックキルモードを切り替えます。"));
    }

    public void sendReloadCMDHelp(@NotNull CommandSender sender) {
        sendMessage(sender, getString("command.help.reload", "&b/spawner reload&8: &7設定ファイルを再読込します。"));
    }

    public void sendNoPermission(@NotNull CommandSender sender, @NotNull Permission perm) {
        sendMessage(sender, getString("command.error.noPermission", "権限がありません: &b%perm%").replace("%perm%", perm.getName()));
    }

    public void sendInvalidType(@NotNull Player player, @NotNull String type) {
        sendMessage(player, getString("command.error.invalidType", "&c%type% というスポナーはありません。").replace("%type%", type));
    }

    public void sendFullInventory(@NotNull Player player) {
        sendMessage(player, getString("command.error.fullInventory", "&cインベントリが満杯です。"));
    }

    @NotNull
    private String getPrefix() {
        return getString("prefix", "&8[&6スポナー&8]&7 ");
    }

    private void sendMessage(@NotNull CommandSender to, @NotNull String msg) {
        BukkitMessage.sendMessageWithColor(to, getPrefix() + msg);
    }

    public void sendCMDHelp(@NotNull CommandSender sender) {
        sendGetCMDHelp(sender);
        sendKillAllCMDHelp(sender);
        sendStackKillCMDHelp(sender);
        sendReloadCMDHelp(sender);
    }
}
