package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.types.StoneType;

/**
 * Represents the different types of stone.
 */
public class Stone extends MaterialData {

    public Stone() {
        super(Material.STONE);
    }

    public Stone(StoneType stone) {
        this();
        setType(stone);
    }

    /**
     * @deprecated Magic value
     *
     * @param type the type
     */
    @Deprecated
    public Stone(final int type) {
        super(type);
    }

    public Stone(final Material type) {
        super(type);
    }

    /**
     * @deprecated Magic value
     *
     * @param type the type
     * @param data the data
     */
    @Deprecated
    public Stone(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @deprecated Magic value
     *
     * @param type the type
     * @param data the data
     */
    @Deprecated
    public Stone(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current type of this stone.
     *
     * @return StoneType of this stone
     */
    public StoneType getType() {
        return StoneType.getByData(getData());
    }

    /**
     * Sets the type of this stone.
     *
     * @param stone New type of this stone
     */
    public void setType(StoneType stone) {
        setData(stone.getData());
    }

    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }

    @Override
    public Stone clone() {
        return (Stone) super.clone();
    }
}
