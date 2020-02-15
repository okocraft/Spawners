package com.github.siroshun09.spawners;

import com.github.siroshun09.sirolibrary.config.BukkitConfig;
import com.github.siroshun09.sirolibrary.message.BukkitMessage;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration extends BukkitConfig {
    private final static Configuration INSTANCE = new Configuration();

    private final Set<EntityType> disabledMobs = new HashSet<>();

    private Configuration() {
        super(Spawners.get(), "config.yml", true);
        setDisabledMobs();
    }

    @NotNull
    public static Configuration get() {
        return INSTANCE;
    }

    public String getStackedMobName(int amount, @NotNull EntityType type) {
        return BukkitMessage.setColor(getString("Stack.nameFormat", "&e&l%number%x &6&l%type%")
                .replace("%number%", String.valueOf(amount)).replace("%type%", type.name()));
    }

    public int getStackRadius() {
        return getInt("Stack.radius", 3);
    }

    public int getMaxStackAmount() {
        int value = getInt("Stack.max", 25);
        return value == -1 ? Integer.MAX_VALUE : value;
    }

    public long getStackInterval() {
        return getLong("Stack.interval", 60);
    }

    @NotNull
    public List<String> getEnabledWorlds() {
        return new ArrayList<>(getStringList("Spawner.enabledWorlds"));
    }

    public boolean isEnabledWorld(@NotNull World world) {
        return getStringList("Spawner.enabledWorlds").contains(world.getName());
    }

    public boolean isDisabledMob(@NotNull EntityType type) {
        return disabledMobs.contains(type);
    }

    private void setDisabledMobs() {
        disabledMobs.clear();
        Collections.addAll(disabledMobs, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, EntityType.SLIME, EntityType.MAGMA_CUBE);

        for (String type : getStringList("Stack.disabledMobType")) {
            EntityType entityType = null;
            try {
                entityType = EntityType.valueOf(type);
            } catch (IllegalArgumentException e) {
                Spawners.get().getLogger().severe("無効なエンティティタイプです: " + type);
            }
            if (entityType != null) {
                disabledMobs.add(entityType);
            }
        }
    }
}
