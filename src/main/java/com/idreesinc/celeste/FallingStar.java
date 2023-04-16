package com.idreesinc.celeste;

import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FallingStar extends BukkitRunnable {

    private final CelesteRed celeste;
    protected final Location location;
    protected final Location dropLoc;
    protected final CelesteConfig config;
    protected final World world;
    private double y = 256;
    private boolean soundPlayed_1 = false;
    private boolean soundPlayed_2 = false;
    private boolean lootDropped = false;
    private int sparkTimer;

    public FallingStar(CelesteRed celeste, Location location) {
        this.celeste = celeste;
        this.location = location;
        this.world = location.getWorld();
        assert world != null;
        config = celeste.configManager.getConfigForWorld(world.getName());
        sparkTimer = config.fallingStarsSparkTime;
        dropLoc = new Location(location.getWorld(), location.getX(), world.getHighestBlockAt(location).getY() + 1, location.getZ());
    }
    
    protected void playSoundStart(float volume) {
        world.playSound(dropLoc, Sound.BLOCK_BELL_RESONATE, volume, 0.5f);
    }
    
    protected void playSoundDrop(float volume) {
        world.playSound(dropLoc, Sound.BLOCK_BELL_RESONATE, volume, 0.7f);
    }
    
    protected void spawnParticle(double y, double offsetY, double speed) {
        world.spawnParticle(Particle.FIREWORKS_SPARK, location.getX(), y, location.getZ(),
                0,
                0, offsetY, 0,
                speed, null, true);
    }
    
    protected void dropLoots() {
        // Note that both simple loot and loot tables will drop if both are configured because why not
        if (config.fallingStarSimpleLoot != null && config.fallingStarSimpleLoot.entries.size() > 0) {
            ItemStack drop = new ItemStack(Material.valueOf(config.fallingStarSimpleLoot.getRandom()), 1);
            world.dropItem(dropLoc, drop);
            if (config.debug) {
                celeste.getLogger().info("Spawned simple falling star loot");
            }
        }
        
        if (config.fallingStarsExperience > 0) {
            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(dropLoc, EntityType.EXPERIENCE_ORB);
            orb.setExperience(config.fallingStarsExperience);
            if (config.debug) {
                celeste.getLogger().info("Dropping experience orbs with value " + config.fallingStarsExperience);
            }
        }
    }

    public void run() {
        double step = 1;
        // Movement particles
        spawnParticle(y, new Random().nextDouble(), 0.2);
        spawnParticle(y+ new Random().nextDouble() * step, -1, 1);
        if (y % (step * 2) == 0) {
            world.spawnParticle(Particle.LAVA, location.getX(), y + new Random().nextDouble(),
                    location.getZ(),
                    0,  0, new Random().nextDouble(), 0,
                    0.2, null, true);
        }
        
        // Sound
        if (config.fallingStarsSoundEnabled && !soundPlayed_1 && y <= dropLoc.getY() + 115) {
            playSoundStart((float) config.fallingStarsVolume);
            soundPlayed_1 = true;
        }
        if (config.fallingStarsSoundEnabled && !soundPlayed_2 && y <= dropLoc.getY() + 5) {
            playSoundDrop((float) config.fallingStarsVolume);
            soundPlayed_2 = true;
        }
        
        // On the ground
        if (y <= dropLoc.getY()) {
            if (!lootDropped) {
                dropLoots();
                lootDropped = true;
            }
            if (y % (step * 5) == 0) {
                world.spawnParticle(Particle.LAVA, dropLoc,
                        0, 0, new Random().nextDouble(), 0,
                        1, null, true);
            }
            sparkTimer--;
            if (sparkTimer <= 0) {
                this.cancel();
            }
        }
        y -= step;
    }

}