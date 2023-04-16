package com.idreesinc.celeste.commands;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCeleste implements CommandExecutor, TabCompleter {
    
    private static final List<String> ARGS = Arrays.asList("reload", "info", "set");
    private static final List<String> ARGS_SET = Arrays.asList("shooting_stars", "falling_stars", "red_falling_stars");
    private static final List<String> ARGS_ON_OFF = Arrays.asList("enable", "disable");
    
    private final CelesteRed celeste;

    public CommandCeleste(CelesteRed celeste) {
        this.celeste = celeste;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 0) {
            return false;
        }
        
        // reload
        if (args[0].equals("reload")) {
            if (sender.hasPermission("celeste.reload")) {
                celeste.reload();
                sender.sendMessage(ChatColor.GREEN + "Celeste has been reloaded");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            }
            return true;
        }
        
        // info
        if (args[0].equals("info")) {
            if (sender.hasPermission("celeste.info")) {
                CelesteConfig config = celeste.configManager.getConfigForWorld(sender instanceof Player ? ((Player)sender).getWorld().getName() : "");
                sender.sendMessage("§6§lCeleste§c§lRed§6§l version " + ChatColor.GOLD + celeste.getDescription().getVersion());
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
        
        if(args[0].equals("set")) {
            if (!sender.hasPermission("celeste.set")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                return true;
            }
            if(args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Syntax : /"+label+" set [module] [enable/disable]");
                return true;
            }
            if(!ARGS_SET.contains(args[1])) {
                sender.sendMessage(ChatColor.RED + "Unknown module : '" + args[1] + "'");
                return true;
            }
            if(!ARGS_ON_OFF.contains(args[2])) {
                sender.sendMessage(ChatColor.RED + "Unknown value : '" + args[2] + "'. Allowed values: [enable, disable]");
                return true;
            }
    
            sender.sendMessage(ChatColor.GRAY + "todo");
            
            return true;
        }
        
        
        return false;
    }
    
    private String getEnableString(boolean config) {
        return config ?
                ChatColor.GREEN + "enabled"
                : ChatColor.RED + "disabled";
    }
    
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            return ARGS.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        if(args.length == 2 && args[0].equals("set")) {
            return ARGS_SET.stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        }
        if(args.length == 3 && args[0].equals("set") && ARGS_SET.contains(args[1])) {
            return ARGS_ON_OFF.stream().filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
