package com.idreesinc.celeste.commands;

import com.idreesinc.celeste.CelesteRed;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Celeste v" + ChatColor.GOLD + celeste.getDescription().getVersion());
                sender.sendMessage(ChatColor.AQUA + "Shooting stars: " + (celeste.getConfig().getBoolean("shooting-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage(ChatColor.AQUA + "Falling stars: " + (celeste.getConfig().getBoolean("falling-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage(ChatColor.AQUA + "Red falling stars: " + (celeste.getConfig().getBoolean("red-falling-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage(ChatColor.AQUA + "Meteor showers: " + (celeste.getConfig().getBoolean("new-moon-meteor-shower") ? "Enabled" : "Disabled"));
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            }
            return true;
        }
        return false;
    }

}
