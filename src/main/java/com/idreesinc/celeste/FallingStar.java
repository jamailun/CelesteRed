package com.idreesinc.celeste;

import com.idreesinc.celeste.config.CelesteConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
    private double y = 256;
    private boolean soundPlayed = false;
    private boolean lootDropped = false;
    private int sparkTimer;

    public FallingStar(CelesteRed celeste, Location location) {
        this.celeste = celeste;
        this.location = location;
        config = celeste.configManager.getConfigForWorld(location.getWorld().getName());
        sparkTimer = config.fallingStarsSparkTime;
        dropLoc = new Location(location.getWorld(), location.getX(),
                location.getWorld().getHighestBlockAt(location).getY() + 1, location.getZ());
    }
    
    protected Sound getSound() {
        return Sound.BLOCK_BELL_RESONATE;
    }
    
    protected Particle getParticle() {
        return Particle.FIREWORKS_SPARK;
    }
    
    protected void dropLoots() {
        // Note that both simple loot and loot tables will drop if both are configured because why not
        if (config.fallingStarSimpleLoot != null && config.fallingStarSimpleLoot.entries.size() > 0) {
            ItemStack drop = new ItemStack(Material.valueOf(config.fallingStarSimpleLoot.getRandom()), 1);
            location.getWorld().dropItem(dropLoc, drop);
            if (config.debug) {
                celeste.getLogger().info("Spawned simple falling star loot");
            }
        }
        
        if (config.fallingStarsExperience > 0) {
            ExperienceOrb orb = (ExperienceOrb) dropLoc.getWorld().spawnEntity(dropLoc, EntityType.EXPERIENCE_ORB);
            orb.setExperience(config.fallingStarsExperience);
            if (config.debug) {
                celeste.getLogger().info("Dropping experience orbs with value " + config.fallingStarsExperience);
            }
        }
    }

    public void run() {
        double step = 1;
        // Movement particles
        location.getWorld().spawnParticle(getParticle(), location.getX(), y, location.getZ(),
                0,  0,  new Random().nextDouble(), 0,
                0.2, null, true);
        location.getWorld().spawnParticle(getParticle(), location.getX(),
                y + new Random().nextDouble() * step,
                location.getZ(),
                0,  0, -1, 0,
                1, null, true);
        
        
        if (y % (step * 2) == 0) {
            location.getWorld().spawnParticle(Particle.LAVA, location.getX(), y + new Random().nextDouble(),
                    location.getZ(),
                    0,  0, new Random().nextDouble(), 0,
                    0.2, null, true);
        }
        
        // Sound
        if (config.fallingStarsSoundEnabled && !soundPlayed && y <= dropLoc.getY() + 75) {
            location.getWorld().playSound(dropLoc, getSound(), (float) config.fallingStarsVolume, 0.5f);
            soundPlayed = true;
        }
        
        // On the ground
        if (y <= dropLoc.getY()) {
            if (!lootDropped) {
                dropLoots();
                lootDropped = true;
            }
            if (y % (step * 5) == 0) {
                location.getWorld().spawnParticle(Particle.LAVA, dropLoc,
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