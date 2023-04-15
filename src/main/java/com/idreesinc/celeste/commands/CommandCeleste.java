package com.idreesinc.celeste.commands;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandCeleste implements CommandExecutor {

    private final CelesteRed celeste;

    public CommandCeleste(CelesteRed celeste) {
        this.celeste = celeste;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("celeste.reload")) {
                celeste.reload();
                sender.sendMessage(ChatColor.GREEN + "Celeste has been reloaded");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            if (sender.hasPermission("celeste.info")) {
                CelesteConfig config = celeste.configManager.getConfigForWorld(sender instanceof Player ? ((Player)sender).getWorld().getName() : "");
                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Celeste§c§lRed§6§l version " + ChatColor.GOLD + celeste.getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "- Shooting stars: " + getEnableString(config.shootingStarsEnabled));
                sender.sendMessage(ChatColor.GRAY + "- Falling stars: " + getEnableString(config.fallingStarsEnabled));
                sender.sendMessage(ChatColor.GRAY + "- Red falling stars: " + getEnableString(config.redFallingStarsEnabled));
                if(config.redFallingStarsEnabled) {
                    sender.sendMessage(ChatColor.GRAY + "  - Fire module: " + getEnableString(config.redFallingFire.enabled));
                    sender.sendMessage(ChatColor.GRAY + "  - Transform module: " + getEnableString(config.redFallingTransform.enabled));
                }
                sender.sendMessage(ChatColor.GRAY + "- Meteor showers: " + getEnableString(config.newMoonMeteorShower));
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            }
            return true;
        }
        return false;
    }
    
    private String getEnableString(boolean config) {
        return config ?
                ChatColor.GREEN + "enabled"
                : ChatColor.RED + "disabled";
    }

}
