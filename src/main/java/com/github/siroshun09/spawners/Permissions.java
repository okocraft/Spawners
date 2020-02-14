package com.github.siroshun09.spawners;

import com.github.siroshun09.sirolibrary.bukkitutils.BukkitUtil;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions {
    public final static Permission RELOAD = new Permission("spawners.reload", PermissionDefault.OP);
    public final static Permission KILLALL = new Permission("spawners.killall", PermissionDefault.OP);
    public final static Permission GET = new Permission("spawners.get", PermissionDefault.OP);
    public final static Permission STACKKILL = new Permission("spawners.stackkill", PermissionDefault.OP);

    static {
        if (!BukkitUtil.existPermission("spawners.reload")) BukkitUtil.addPermission(RELOAD);
        if (!BukkitUtil.existPermission("spawners.killall")) BukkitUtil.addPermission(KILLALL);
        if (!BukkitUtil.existPermission("spawners.get")) BukkitUtil.addPermission(GET);
        if (!BukkitUtil.existPermission("spawners.stackkill")) BukkitUtil.addPermission(STACKKILL);
    }

}
