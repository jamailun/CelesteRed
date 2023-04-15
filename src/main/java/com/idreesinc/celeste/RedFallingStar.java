package com.idreesinc.celeste;

import com.idreesinc.celeste.config.MobConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RedFallingStar extends FallingStar {
    
    public RedFallingStar(CelesteRed celeste, Location location) {
        super(celeste, location);
    }
    
    @Override
    protected Sound getSound() {
        return Sound.BLOCK_BELL_RESONATE;
    }
    
    @Override
    protected Particle getParticle() {
        return Particle.FIREWORKS_SPARK;
    }

    @Override
    protected void dropLoots() {
        location.getWorld().playSound(dropLoc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) config.fallingStarsVolume, 0.5f);
        MobConfiguration mob = config.redFallingStarsMobs.getRandom();
        if(mob != null) {
            mob.spawn(dropLoc);
            location.getWorld().spawnParticle(
                    Particle.SMOKE_LARGE,
                    dropLoc.getX(), dropLoc.getY(), dropLoc.getZ(),
                    12,
                    1,1,1,
                    .8
            );
        }
        // fire
        if(config.redFallingFire.fireEnabled) {
            createFire(dropLoc, config.redFallingFire.fireRadiusMin, config.redFallingFire.fireRadiusMax, config.redFallingFire.fireChance);
        }
        
        // blocs around
    }
    
    private Collection<Block> blocksInSphere(@NotNull Location center, double radius) {
        Set<Block> blocks = new HashSet<>();
        World world = center.getWorld();
        assert world != null;
        double ds = radius * radius;
        
        for(double dx = -radius; dx <= radius; dx += 1) {
            for(double dy = -radius; dy <= radius; dy += 1) {
                for(double dz = -radius; dz <= radius; dz += 1) {
                    Vector pos = center.toVector().add(new Vector(dx, dy, dz));
                    if(pos.distanceSquared(center.toVector()) <= ds) {
                        blocks.add(pos.toLocation(world).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
    
    private static final Random RANDOM = new Random();
    
    private void createFire(Location center, double minRadius, double maxRadius, double percentage) {
        Collection<Block> allInSphere = blocksInSphere(center, maxRadius);
        allInSphere.stream()
                .filter(b -> b.getType() == Material.AIR || b.getType() == Material.CAVE_AIR || b.getType() == Material.VOID_AIR)
                .filter(b -> b.getLocation().toVector().distance(dropLoc.toVector()) >= minRadius)
                .forEach(b -> {
                    if(RANDOM.nextDouble() <= percentage)
                        b.setType(Material.FIRE);
                });
    }
    
}