package com.idreesinc.celeste.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireModuleConfig {
	
	public boolean enabled;
	public double radiusMin;
	public double radiusMax;
	public double chance;
	public double duration;
	
	public void buildFromConfigurationSection(@Nullable ConfigurationSection section) {
		if(section == null) {
			enabled = false;
			return;
		}
		enabled = section.getBoolean("enabled");
		radiusMin = section.getDouble("radius-min");
		radiusMax = section.getDouble("radius-max");
		chance = section.getDouble("chance");
		duration = section.getDouble("duration");
	}
	
	public void buildFromConfigurationSectionWithGlobal(@Nullable ConfigurationSection section, @NotNull FireModuleConfig global) {
		if(section != null) {
			enabled = section.getBoolean("enabled", global.enabled);
			radiusMin = section.getDouble("radius-min", global.radiusMin);
			radiusMax = section.getDouble("radius-max", global.radiusMax);
			chance = section.getDouble("chance", global.chance);
			duration = section.getDouble("duration", global.duration);
		} else {
			enabled = global.enabled;
			radiusMin = global.radiusMin;
			radiusMax = global.radiusMax;
			chance = global.chance;
			duration = global.duration;
		}
	}
}
