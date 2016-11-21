package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class StopSoundCommand extends VanillaCommand {

    public StopSoundCommand() {
        super("stopsound");
        setDescription("Stops a sound for a player.");
        setUsage("/stopsound <player> [source] [sound]");
        setPermission("bukkit.command.stopsound");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0 || args.length > 3) {
            sender.sendMessage(usageMessage);
            return false;
        }

        String sourceName, soundName;
        SoundCategory source = null;
        Sound sound = null;

        if (args.length > 1) {
            sourceName = args[1];
            source = SoundCategory.getCategory(sourceName);
            if (source == null) {
                sender.sendMessage(ChatColor.RED + "There is no such category as '" + sourceName.toLowerCase() + "'.");
                return false;
            }
        }
        if (args.length == 3) {
            soundName = args[2];
            if (soundName.startsWith("minecraft:") || soundName.contains(".")) {
                sound = Sound.fromId(soundName);
            } else {
                sound = getSound(soundName);
            }
            if (sound == null) {
                sender.sendMessage(ChatColor.RED + "There is no such sound as '" + soundName + "'.");
                return false;
            }
        }
        Entity[] targets;
        if (args[0].startsWith("@") && sender instanceof Entity) {
            CommandTarget ct = new CommandTarget(args[0]);
            ct.getArguments().put("type", new CommandTarget.SelectorValue("PLAYER"));
            targets = ct.getMatched(((Entity) sender).getLocation());
        } else {
            Player targetPlayer = Bukkit.getPlayerExact(args[0]);
            targets = targetPlayer == null ? new Entity[0] : new Entity[]{targetPlayer};
        }

        if (targets.length == 0) {
            sender.sendMessage(ChatColor.RED + "There's no entity matching the target.");
        } else {
            for (Entity target : targets) {
                if (!(target instanceof Player)) {
                    continue; // Should never happen, but just to be sure
                }
                Player player = (Player) target;
                if (source == null && sound == null) {
                    player.stopAllSounds();
                    sender.sendMessage("Stopped all sounds for " + player.getName() + ".");
                } else if (sound == null) {
                    player.stopSound(source);
                    sender.sendMessage("Stopped all sounds from source '" + source.name().toLowerCase() + "' for " + player.getName() + ".");
                } else {
                    player.stopSound(sound, source);
                    sender.sendMessage("Stopped sound '" + sound.getId() + "' from source '" + source.name().toLowerCase() + "' for " + player.getName() + ".");
                }
            }
        }
        return false;
    }

    private Sound getSound(String name) {
        for (Sound sound : Sound.values()) {
            if (name.equalsIgnoreCase(sound.name())) {
                return sound;
            }
        }
        return null;
    }
}
