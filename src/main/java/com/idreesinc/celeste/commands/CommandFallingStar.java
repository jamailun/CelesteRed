package com.idreesinc.celeste.commands;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.CelestialSphere;
import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandFallingStar implements CommandExecutor {
    
    private final CelesteRed celeste;
    private final boolean red;

    public CommandFallingStar(CelesteRed celeste, boolean red) {
        this.celeste = celeste;
        this.red = red;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target;
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Error: Player not found.");
                return true;
            }
            CelestialSphere.createFallingStar(celeste, target, red, false);
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
                CelestialSphere.createFallingStar(celeste, target, red, false);
            } else {
                return false;
            }
        }
    
        CelesteConfig config =  celeste.configManager.getConfigForWorld(target.getWorld().getName());
        String message = red ? config.redFallingStarsMessage : config.fallingStarsMessage;
        if (message != null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        return true;
    }

}
