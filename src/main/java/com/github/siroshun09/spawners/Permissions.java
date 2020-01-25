package com.github.siroshun09.spawners;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions {
    public final static Permission RELOAD = new Permission("spawners.reload", PermissionDefault.OP);
    public final static Permission KILLALL = new Permission("spawners.killall", PermissionDefault.OP);
    public final static Permission GET = new Permission("spawners.get", PermissionDefault.OP);
    public final static Permission STACKKILL = new Permission("spawners.stackkill", PermissionDefault.OP);

    static {
        if (Bukkit.getPluginManager().getPermission("spawners.reload") == null)
            Bukkit.getPluginManager().addPermission(RELOAD);
        if (Bukkit.getPluginManager().getPermission("spawners.killall") == null)
            Bukkit.getPluginManager().addPermission(KILLALL);
        if (Bukkit.getPluginManager().getPermission("spawners.get") == null)
            Bukkit.getPluginManager().addPermission(GET);
        if (Bukkit.getPluginManager().getPermission("spawners.stackkill") == null)
            Bukkit.getPluginManager().addPermission(STACKKILL);
    }

}
