package org.bukkit;

/**
 * An Enum of categories for sounds.
 */
public enum SoundCategory {

    MASTER,
    MUSIC,
    RECORDS,
    WEATHER,
    BLOCKS,
    HOSTILE,
    NEUTRAL,
    PLAYERS,
    AMBIENT,
    VOICE;

    public static SoundCategory getCategory(String name) {
        for (SoundCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return SoundCategory.MASTER;
    }

    public static SoundCategory fromInt(int i) {
        return values()[i];
    }
}
