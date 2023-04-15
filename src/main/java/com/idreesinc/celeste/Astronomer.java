package com.idreesinc.celeste;

import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class Astronomer extends BukkitRunnable {

    private final CelesteRed celeste;

    public Astronomer(CelesteRed celeste) {
        this.celeste = celeste;
    }

    public void run() {
        if (celeste.getServer().getOnlinePlayers().size() == 0) {
            return;
        }
        List<World> worlds = celeste.getServer().getWorlds();
        for (World world : worlds) {
            CelesteConfig config = celeste.configManager.getConfigForWorld(world.getName());
            if (!celeste.configManager.doesWorldHaveOverrides(world.getName())
                    && !world.getEnvironment().equals(World.Environment.NORMAL)) {
                // Ensure that Celeste only runs on normal worlds unless override is specified in config
                continue;
            }
            if (world.getPlayers().isEmpty()) {
                continue;
            }
            if (!(world.getTime() >= config.beginSpawningStarsTime && world.getTime() <= config.endSpawningStarsTime)) {
                continue;
            }

            double shootingStarChance;
            double fallingStarChance;
            double redFallingStarChance;
            if (config.newMoonMeteorShower && (world.getFullTime() / 24000) % 8 == 4) {
                shootingStarChance = config.shootingStarsPerMinuteMeteorShower / 120d;
                fallingStarChance = config.fallingStarsPerMinuteMeteorShower / 120d;
                redFallingStarChance = config.redFallingStarsPerMinuteMeteorShower / 120d;
            } else {
                shootingStarChance = config.shootingStarsPerMinute / 120d;
                fallingStarChance = config.fallingStarsPerMinute / 120d;
                redFallingStarChance = config.redFallingStarsPerMinute / 120d;
            }

            if (config.shootingStarsEnabled && new Random().nextDouble() <= shootingStarChance && ! world.hasStorm()) {
                CelestialSphere.createShootingStar(
                        celeste,
                        world.getPlayers().get(new Random().nextInt(world.getPlayers().size()))
                );
            }
            if (config.fallingStarsEnabled && new Random().nextDouble() <=  fallingStarChance && ! world.hasStorm()) {
                CelestialSphere.createFallingStar(
                        celeste,
                        world.getPlayers().get(new Random().nextInt(world.getPlayers().size())),
                        false,
                        false
                );
            }
            if (config.redFallingStarsEnabled && new Random().nextDouble() <=  redFallingStarChance) {
                CelestialSphere.createFallingStar(
                        celeste,
                        world.getPlayers().get(new Random().nextInt(world.getPlayers().size())),
                        true,
                        false
                );
            }
        }
    }
}