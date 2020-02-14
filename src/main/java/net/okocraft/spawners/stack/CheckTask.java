package net.okocraft.spawners.stack;

import net.okocraft.spawners.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

class CheckTask implements Runnable {

    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (Configuration.get().isEnabledWorld(world)) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (!(entity instanceof Mob)) continue;
                    Mob mob = (Mob) entity;
                    if (Configuration.get().isDisabledMob(mob.getType()) || MobStacker.get().isNotStacked(mob))
                        continue;
                    MobStacker.get().setMcMMOMeta(mob);
                    MobStacker.get().setCustomName(mob);
                }
            }
        }
    }
}
