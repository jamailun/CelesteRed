package com.idreesinc.celeste.config;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.utilities.WeightedRandomBag;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class CelesteConfig {
    public boolean newMoonMeteorShower;
    public int beginSpawningStarsTime;
    public int endSpawningStarsTime;
    public boolean debug;

    public boolean shootingStarsEnabled;
    public double shootingStarsPerMinute;
    public double shootingStarsPerMinuteMeteorShower;
    public int shootingStarsMinHeight;
    public int shootingStarsMaxHeight;
    public String shootingStarsMessage;
    
    public boolean fallingStarsEnabled;
    public double fallingStarsPerMinute;
    public double fallingStarsPerMinuteMeteorShower;
    public int fallingStarsRadius;
    public boolean fallingStarsSoundEnabled;
    public double fallingStarsVolume;
    public int fallingStarsSparkTime;
    public int fallingStarsExperience;
    public WeightedRandomBag<String> fallingStarSimpleLoot;
    public String fallingStarsMessage;
    
    public boolean redFallingStarsEnabled;
    public double redFallingStarsPerMinute;
    public double redFallingStarsPerMinuteMeteorShower;
    public int redFallingStarsRadius;
    public WeightedRandomBag<MobConfiguration> redFallingStarsMobs;
    public String redFallingStarsMessage;

    public CelesteConfig(ConfigurationSection section) {
        // Used to build the global config
        buildFromConfigurationSection(section);
    }

    public CelesteConfig(ConfigurationSection section, CelesteConfig globalConfig) {
        // Used to build per-world configs
        buildFromConfigurationSectionWithGlobal(section, globalConfig);
    }

    private void buildFromConfigurationSection(ConfigurationSection section) {
        ConfigurationSection general = section.getConfigurationSection("general");
        ConfigurationSection shooting = section.getConfigurationSection("shooting-stars");
        ConfigurationSection falling = section.getConfigurationSection("falling-stars");
        ConfigurationSection redFalling = section.getConfigurationSection("red-falling-stars");
        if(general == null || shooting == null || falling == null || redFalling == null) {
            CelesteRed.logError("Invalid config.");
            throw new RuntimeException("Bad configuration for celeste.");
        }
        // Genegral configuration
        newMoonMeteorShower = general.getBoolean("new-moon-meteor-shower");
        beginSpawningStarsTime = general.getInt("begin-spawning-stars-time");
        endSpawningStarsTime = general.getInt("end-spawning-stars-time");
        debug = general.getBoolean("debug");

        // Shooting star
        shootingStarsEnabled = shooting.getBoolean("enabled");
        shootingStarsPerMinute = shooting.getDouble("per-minute");
        shootingStarsPerMinuteMeteorShower = shooting.getDouble("per-minute-during-meteor-showers");
        shootingStarsMinHeight = shooting.getInt("min-height");
        shootingStarsMaxHeight = shooting.getInt("max-height");
        shootingStarsMessage = shooting.getString("summon-text");

        // Falling stars
        fallingStarsEnabled = falling.getBoolean("enabled");
        fallingStarsPerMinute = falling.getDouble("per-minute");
        fallingStarsPerMinuteMeteorShower = falling.getDouble("per-minute-during-meteor-showers");
        fallingStarsRadius = falling.getInt("radius");
        fallingStarsSoundEnabled = falling.getBoolean("sound-enabled");
        fallingStarsVolume = falling.getDouble("volume");
        fallingStarsSparkTime = falling.getInt("spark-time");
        fallingStarsExperience = falling.getInt("experience");
        fallingStarSimpleLoot = calculateSimpleLoot(falling.getConfigurationSection("loots"));
        fallingStarsMessage = falling.getString("summon-text");
        
        // Red falling stars
        redFallingStarsEnabled = redFalling.getBoolean("enabled");
        redFallingStarsPerMinute = redFalling.getDouble("per-minute");
        redFallingStarsPerMinuteMeteorShower= redFalling.getDouble("per-minute-during-meteor-showers");
        redFallingStarsRadius = redFalling.getInt("radius");
        redFallingStarsMobs = calculateMobs(redFalling.getConfigurationSection("mobs"));
        redFallingStarsMessage = redFalling.getString("summon-text");
    }

    private void buildFromConfigurationSectionWithGlobal(ConfigurationSection section, CelesteConfig globalConfig) {
        ConfigurationSection general = section.getConfigurationSection("general");
        ConfigurationSection shooting = section.getConfigurationSection("shooting-stars");
        ConfigurationSection falling = section.getConfigurationSection("falling-stars");
        ConfigurationSection redFalling = section.getConfigurationSection("red-falling-stars");
        
        if(general != null) {
            newMoonMeteorShower = general.getBoolean("new-moon-meteor-shower", globalConfig.newMoonMeteorShower);
            beginSpawningStarsTime = general.getInt("begin-spawning-stars-time", globalConfig.beginSpawningStarsTime);
            endSpawningStarsTime = general.getInt("end-spawning-stars-time", globalConfig.endSpawningStarsTime);
        } else {
            newMoonMeteorShower = globalConfig.newMoonMeteorShower;
            beginSpawningStarsTime = globalConfig.beginSpawningStarsTime;
            endSpawningStarsTime = globalConfig.endSpawningStarsTime;
        }
        
        if(shooting != null) {
            shootingStarsEnabled = shooting.getBoolean("enabled", globalConfig.shootingStarsEnabled);
            shootingStarsPerMinute = shooting.getDouble("per-minute", globalConfig.shootingStarsPerMinute);
            shootingStarsPerMinuteMeteorShower = shooting.getDouble("per-minute-during-meteor-showers", globalConfig.shootingStarsPerMinuteMeteorShower);
            shootingStarsMinHeight = shooting.getInt("min-height", globalConfig.shootingStarsMinHeight);
            shootingStarsMaxHeight = shooting.getInt("max-height", globalConfig.shootingStarsMaxHeight);
        } else {
            shootingStarsEnabled = globalConfig.shootingStarsEnabled;
            shootingStarsPerMinute = globalConfig.shootingStarsPerMinute;
            shootingStarsPerMinuteMeteorShower = globalConfig.shootingStarsPerMinuteMeteorShower;
            shootingStarsMinHeight = globalConfig.shootingStarsMinHeight;
            shootingStarsMaxHeight = globalConfig.shootingStarsMaxHeight;
        }
        
        if(falling != null) {
            fallingStarsEnabled = falling.getBoolean("enabled", globalConfig.fallingStarsEnabled);
            fallingStarsPerMinute = falling.getDouble("per-minute", globalConfig.fallingStarsPerMinute);
            fallingStarsPerMinuteMeteorShower = falling.getDouble("per-minute-during-meteor-showers", globalConfig.fallingStarsPerMinuteMeteorShower);
            fallingStarsRadius = falling.getInt("radius", globalConfig.fallingStarsRadius);
            fallingStarsSoundEnabled = falling.getBoolean("sound-enabled", globalConfig.fallingStarsSoundEnabled);
            fallingStarsVolume = falling.getDouble("volume", globalConfig.fallingStarsVolume);
            fallingStarsSparkTime = falling.getInt("spark-time", globalConfig.fallingStarsSparkTime);
            fallingStarsExperience = falling.getInt("experience", globalConfig.fallingStarsExperience);
            ConfigurationSection loots = falling.getConfigurationSection("loots");
            if(loots != null) {
                fallingStarSimpleLoot = calculateSimpleLoot(loots);
            } else {
                fallingStarSimpleLoot = globalConfig.fallingStarSimpleLoot;
            }
        } else {
            fallingStarsEnabled = globalConfig.fallingStarsEnabled;
            fallingStarsPerMinute = globalConfig.fallingStarsPerMinute;
            fallingStarsPerMinuteMeteorShower = globalConfig.fallingStarsPerMinuteMeteorShower;
            fallingStarsRadius = globalConfig.fallingStarsRadius;
            fallingStarsSoundEnabled = globalConfig.fallingStarsSoundEnabled;
            fallingStarsVolume = globalConfig.fallingStarsVolume;
            fallingStarsSparkTime = globalConfig.fallingStarsSparkTime;
            fallingStarsExperience = globalConfig.fallingStarsExperience;
            fallingStarSimpleLoot = globalConfig.fallingStarSimpleLoot;
        }

        if(redFalling != null) {
            redFallingStarsEnabled = redFalling.getBoolean("enabled", globalConfig.redFallingStarsEnabled);
            redFallingStarsPerMinute = redFalling.getDouble("per-minute", globalConfig.redFallingStarsPerMinute);
            redFallingStarsPerMinuteMeteorShower = redFalling.getDouble("per-minute-during-meteor-showers", globalConfig.redFallingStarsPerMinuteMeteorShower);
            redFallingStarsRadius = redFalling.getInt("radius", globalConfig.redFallingStarsRadius);
            redFallingStarsMessage = redFalling.getString("summon-text", globalConfig.redFallingStarsMessage);
            ConfigurationSection mobs = redFalling.getConfigurationSection("mobs");
            if(mobs != null) {
                redFallingStarsMobs = calculateMobs(mobs);
            } else {
                redFallingStarsMobs = globalConfig.redFallingStarsMobs;
            }
        } else {
            redFallingStarsEnabled = globalConfig.redFallingStarsEnabled;
            redFallingStarsPerMinute = globalConfig.redFallingStarsPerMinute;
            redFallingStarsPerMinuteMeteorShower = globalConfig.redFallingStarsPerMinuteMeteorShower;
            redFallingStarsRadius = globalConfig.redFallingStarsRadius;
            redFallingStarsMessage = globalConfig.redFallingStarsMessage;
            redFallingStarsMobs = globalConfig.redFallingStarsMobs;
        }
    }

    public WeightedRandomBag<String> calculateSimpleLoot(@Nullable ConfigurationSection loot) {
        WeightedRandomBag<String> fallingStarDrops = new WeightedRandomBag<>();
        if(loot == null)
            return fallingStarDrops;
        for (String key : loot.getKeys(false)) {
            try {
                Material.valueOf(key.toUpperCase());
                fallingStarDrops.addEntry(key.toUpperCase(), loot.getDouble(key));
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Item with name " + key.toUpperCase() + " does not exist, skipping");
            }
        }
        return fallingStarDrops;
    }
    
    public WeightedRandomBag<MobConfiguration> calculateMobs(@Nullable ConfigurationSection mobs) {
        WeightedRandomBag<MobConfiguration> redMobs = new WeightedRandomBag<>();
        if(mobs == null)
            return redMobs;
        
        for(String mobId : mobs.getKeys(false)) {
            ConfigurationSection mc = mobs.getConfigurationSection(mobId);
            if(mc != null) {
                MobConfiguration mob = new MobConfiguration(mc);
                redMobs.addEntry(mob, mc.getDouble("chance", 1.0 / mobs.getKeys(false).size()));
            }
        }
        return redMobs;
    }
}
