package org.bukkit.block;

import org.bukkit.Nameable;
import com.destroystokyo.paper.loottable.LootableInventory; // Paper
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a hopper.
 */
public interface Hopper extends BlockState, InventoryHolder, Lockable, Nameable, LootableInventory { // Paper

}
