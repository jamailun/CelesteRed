package com.idreesinc.celeste;

import com.idreesinc.celeste.config.MobConfiguration;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

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
        
        // blocs around
    }
    
}