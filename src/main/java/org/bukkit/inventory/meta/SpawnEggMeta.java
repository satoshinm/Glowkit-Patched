package org.bukkit.inventory.meta;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Represents a {@link Material#MONSTER_EGG} and its spawned type.
 */
public interface SpawnEggMeta extends ItemMeta {

    /**
     * Checks if an Entity type is attributed to the spawn egg.
     *
     * @return true if an Entity type is attributed to the spawn egg
     */
    boolean hasSpawnedType();

    /**
     * Get the type of entity this egg will spawn.
     *
     * @return The entity type. May be null for implementation specific default.
     */
    EntityType getSpawnedType();

    /**
     * Set the type of entity this egg will spawn.
     *
     * @param type The entity type. May be null for implementation specific
     * default.
     */
    void setSpawnedType(EntityType type);

    @Override
    SpawnEggMeta clone();
}
