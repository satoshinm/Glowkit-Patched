package org.bukkit.command.defaults;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SetBlockCommand extends VanillaCommand {
    public SetBlockCommand() {
        super("setblock");
        this.description = "Changes a block to another block.";
        this.usageMessage = "/setblock <x> <y> <z> <TileName> [dataValue] [oldBlockHandling] [dataTag]";
        this.setPermission("bukkit.command.setblock");
    }

    enum OldBlockHandling {
        DESTROY,
        KEEP,
        REPLACE
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 4 || args.length > 7) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        Location base = null;
        if (sender instanceof BlockCommandSender) {
            base = ((BlockCommandSender) sender).getBlock().getLocation();
        } else if (sender instanceof Entity) {
            base = ((Entity) sender).getLocation();
        }

        String xP = args[0], yP = args[1], zP = args[2];
        Location location = CommandUtils.getLocation(base, xP, yP, zP);

        String tileName = args[3];

        byte dataValue = 0;
        if (args.length > 4) {
            dataValue = (byte) getInteger(sender, args[3], 0);
        }
        OldBlockHandling oldBlockHandling = OldBlockHandling.REPLACE;
        if (args.length > 5) {
            if (args[4].equals("destroy")) {
                oldBlockHandling = OldBlockHandling.DESTROY;
            } else if (args[4].equals("keep")) {
                oldBlockHandling = OldBlockHandling.KEEP;
            } else if (args[5].equals("replace")) {
                oldBlockHandling = OldBlockHandling.REPLACE;
            } else {
                sender.sendMessage(ChatColor.RED + "oldBlockHandling must be one of: destroy, keep, replace");
                return false;
            }
        }

        String dataTag = null;
        if (args.length > 6) {
            dataTag = args[4];
            // TODO
        }

        World world = Bukkit.getServer().getWorlds().get(0);
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        }

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "No such world");
            return false;
        }

        Block block = world.getBlockAt(location);
        if (block == null) {
            sender.sendMessage(ChatColor.RED + "No block found at (" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")");
            return false;
        }

        if (oldBlockHandling == OldBlockHandling.KEEP) {
            if (block.getType() != Material.AIR) {
                // "keep" everything but air blocks TODO: non-gaseous
                return true;
            }
        }

        Material material = Material.getMaterial(tileName.toUpperCase()); // TODO: are these the correct names?
        if (material == null) {
            sender.sendMessage(ChatColor.RED + "Unrecognized material name: " + tileName);
            return false;
        }

        boolean applyPhysics = true;

        //block.setTypeIdAndData(material.getId(), dataValue, applyPhysics);
        block.setType(material);
        block.setData(dataValue);

        if (oldBlockHandling == OldBlockHandling.DESTROY) {
            throw new UnsupportedOperationException("Destroying blocks not supported.");
            // TODO: destroy old block, play breaking sound, drop item as if broken by player
        }
        sender.sendMessage("Block placed.");

        return true;
    }
}
