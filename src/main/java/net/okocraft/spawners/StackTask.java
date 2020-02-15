package net.okocraft.spawners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

class StackTask implements Runnable {

    @Override
    public void run() {
        int radius = Configuration.get().getStackRadius();
        for (World world : Bukkit.getServer().getWorlds()) {
            if (Configuration.get().isEnabledWorld(world)) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (!(entity instanceof Mob)) continue;
                    if (Configuration.get().isDisabledMob(entity.getType())) continue;
                    for (Entity nearby : entity.getNearbyEntities(radius, radius, radius)) {
                        if (!(nearby instanceof Mob)) continue;
                        if (canStack((Mob) entity, (Mob) nearby)) {
                            MobStacker.get().stack((Mob) entity, (Mob) nearby);
                        }
                    }
                }
            }
        }
    }

    private boolean canStack(@NotNull Mob mob, @NotNull Mob other) {
        if (!other.getType().equals(mob.getType()) || !mob.isValid() || mob.isLeashed() || !other.isValid() || other.isLeashed()) {
            return false;
        }

        if (!MobStacker.get().isFromSpawner(mob) || !MobStacker.get().isFromSpawner(other)) {
            return false;
        }

        if (mob.isCustomNameVisible()) {
            if (MobStacker.get().isNotStacked(mob)) {
                return false;
            }
        }

        if (other.isCustomNameVisible()) {
            if (MobStacker.get().isNotStacked(other)) {
                return false;
            }
        }

        if ((other instanceof Ageable) && (mob instanceof Ageable)) {
            if (((Ageable) other).isAdult() != ((Ageable) mob).isAdult()) {
                return false;
            }
        }

        if ((other instanceof Sittable) && (mob instanceof Sittable)) {
            if (((Sittable) other).isSitting() != ((Sittable) mob).isSitting()) {
                return false;
            }
        }

        if ((other instanceof Tameable) && (mob instanceof Tameable)) {
            if (((Tameable) other).isTamed() || ((Tameable) mob).isTamed()) {
                return false;
            }
        }

        if ((other instanceof Zombie) && (mob instanceof Zombie)) {
            if (((Zombie) other).isBaby() != ((Zombie) mob).isBaby()) {
                return false;
            }
        }

        return true;
    }
}