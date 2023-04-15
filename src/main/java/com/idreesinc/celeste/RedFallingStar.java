package com.idreesinc.celeste;

import com.idreesinc.celeste.config.MobConfiguration;
import com.idreesinc.celeste.utilities.WeightedRandomBag;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RedFallingStar extends FallingStar {
    
    public RedFallingStar(CelesteRed celeste, Location location) {
        super(celeste, location);
    }
    
    private final static Sound[] SOUNDS_BEFORE = new Sound[] {
            Sound.AMBIENT_BASALT_DELTAS_MOOD,
            Sound.AMBIENT_CRIMSON_FOREST_MOOD,
            Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD
    };
    
    private final static Sound[] SOUNDS_ARRIVAL = new Sound[] {
            Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM,
            Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM,
            Sound.ENTITY_ALLAY_DEATH,
            Sound.ENTITY_ALLAY_ITEM_TAKEN
    };
    
    @Override
    protected void playSoundStart(float volume) {
        Sound sound = SOUNDS_BEFORE[RANDOM.nextInt(SOUNDS_BEFORE.length)];
        world.playSound(dropLoc, sound, volume, 0.55f);
    }
    
    @Override
    protected void playSoundDrop(float volume) {
        Sound sound = SOUNDS_ARRIVAL[RANDOM.nextInt(SOUNDS_ARRIVAL.length)];
        world.playSound(dropLoc, sound, volume, 0.55f);
    }
    
    @Override
    protected void spawnParticle(double y, double offsetY, double speed) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 15, 15), 1);
        world.spawnParticle(Particle.REDSTONE, location.getX(), y, location.getZ(), 0, 0, 0, 0, dust);
    }

    @Override
    protected void dropLoots() {
        world.playSound(dropLoc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) config.fallingStarsVolume, 0.5f);
        MobConfiguration mob = config.redFallingStarsMobs.getRandom();
        if(mob != null) {
            mob.spawn(dropLoc);
            world.spawnParticle(
                    Particle.SMOKE_LARGE,
                    dropLoc.getX(), dropLoc.getY(), dropLoc.getZ(),
                    12,
                    1,1,1,
                    .8
            );
        }
        Collection<Block> sphere = null;
        
        // fire
        if(config.redFallingFire.enabled) {
            sphere = blocksInSphere(dropLoc, config.redFallingFire.radiusMax);
            createFire(sphere, config.redFallingFire.radiusMin, config.redFallingFire.chance);
        }
        
        // blocks around
        if(config.redFallingTransform.enabled) {
            if(!config.redFallingFire.enabled || config.redFallingFire.radiusMax != config.redFallingTransform.radiusMax)
                sphere = blocksInSphere(dropLoc, config.redFallingTransform.radiusMax);
            transformBlocks(sphere, config.redFallingTransform.chance, config.redFallingTransform.possibilities);
        }
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
    
    private void createFire(Collection<Block> allInSphere, final double minRadius, final double chance) {
        allInSphere.stream()
                .filter(b -> b.getType().isAir())
                .filter(b -> b.getLocation().distance(dropLoc) >= minRadius)
                .forEach(b -> {
                    if(RANDOM.nextDouble() <= chance)
                        b.setType(Material.FIRE);
                });
    }
    
    @SuppressWarnings("deprecated")
    private void transformBlocks(Collection<Block> allInSphere, double chance, WeightedRandomBag<Material> possibilities) {
        Set<Player> players = world.getPlayers().stream().filter(p -> p.getLocation().distance(dropLoc) < 100).collect(Collectors.toSet());
        Map<Location, BlockData> transformations = new HashMap<>();
        allInSphere.stream()
                .filter(b -> b.getType().isOccluding() && !b.getType().isInteractable() && !b.getType().isAir())
                .forEach(b -> {
                    if(RANDOM.nextDouble() <= chance) {
                        transformations.put(b.getLocation(), CelesteRed.createBlockData(possibilities.getRandom()));
                    }
                });
        transformations.forEach((loc, data) -> {
            players.forEach(player -> {
                player.sendBlockChange(loc, data);
            });
        });
    
        if(config.debug) {
            System.out.println("transform duration = " + config.redFallingTransform.duration + ". Ticks = " + ((long) (20d * config.redFallingTransform.duration)));
        }
        
        CelesteRed.runLater(() -> {
            Collection<BlockState> dbs = transformations.keySet().stream()
                            .map(loc -> loc.getBlock().getState())
                            .collect(Collectors.toSet());
            players.stream()
                    .filter(Entity::isValid)
                    .forEach(player -> {
                        transformations.forEach((loc, type) -> {
                            player.sendBlockChanges(dbs, true);
                        });
                    });
        }, (long) (20d * config.redFallingTransform.duration));
    }
    
}