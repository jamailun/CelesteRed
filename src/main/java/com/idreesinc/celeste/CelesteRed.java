package com.idreesinc.celeste;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.idreesinc.celeste.commands.CommandCeleste;
import com.idreesinc.celeste.commands.CommandFallingStar;
import com.idreesinc.celeste.commands.CommandShootingStar;
import com.idreesinc.celeste.config.CelesteConfigManager;

import java.util.Objects;

public class CelesteRed extends JavaPlugin {

    public CelesteConfigManager configManager = new CelesteConfigManager(this);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
    
        configManager.processConfigs();

        Objects.requireNonNull(this.getCommand("celeste")).setExecutor(new CommandCeleste(this));
        Objects.requireNonNull(this.getCommand("shootingstar")).setExecutor(new CommandShootingStar(this));
        Objects.requireNonNull(this.getCommand("fallingstar")).setExecutor(new CommandFallingStar(this, false));
        Objects.requireNonNull(this.getCommand("redfallingstar")).setExecutor(new CommandFallingStar(this, true));

        BukkitRunnable stargazingTask = new Astronomer(this);
        stargazingTask.runTaskTimer(this, 0, 10);
    }

    public void reload() {
        reloadConfig();
        configManager.processConfigs();
    }
    
    public static void logError(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Celeste" + ChatColor.DARK_RED + "Red" + ChatColor.RED + "] " + s);
    }

}
