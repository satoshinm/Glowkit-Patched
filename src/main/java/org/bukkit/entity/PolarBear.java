package org.bukkit.entity;

/**
 * Represents a polar bear.
 */
public interface PolarBear extends Animals {
    /**
     * Whether the Polar Bear is standing.
     *
     * @return true if the Polar Bear is standing, false otherwise
     */
    boolean isStanding();

    /**
     * Changes the standing state of the Polar Bear.
     *
     * @param standing true if the Polar Bear is standing, false otherwise
     */
    void setStanding(boolean standing);
}
