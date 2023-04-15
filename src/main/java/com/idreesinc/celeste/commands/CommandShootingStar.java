package com.idreesinc.celeste.commands;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.CelestialSphere;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandShootingStar implements CommandExecutor {

    private final CelesteRed celeste;

    public CommandShootingStar(CelesteRed celeste) {
        this.celeste = celeste;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target;
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Error: Player not found.");
                return true;
            }
            CelestialSphere.createShootingStar(celeste, target, false);
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
                CelestialSphere.createShootingStar(celeste, target, false);
            } else {
                return false;
            }
        }
        String message = celeste.configManager.getConfigForWorld(target.getWorld().getName()).shootingStarsMessage;
        if (message != null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        return true;
    }

}
