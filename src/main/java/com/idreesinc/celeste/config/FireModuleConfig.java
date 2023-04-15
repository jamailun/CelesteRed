package com.idreesinc.celeste.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireModuleConfig {
	
	public boolean fireEnabled;
	public double fireRadiusMin;
	public double fireRadiusMax;
	public double fireChance;
	
	public void loadFromConfigurationSection(@Nullable ConfigurationSection section) {
		if(section == null) {
			fireEnabled = false;
			return;
		}
		fireEnabled = section.getBoolean("enabled");
		fireRadiusMin = section.getDouble("radius-min");
		fireRadiusMax = section.getDouble("radius-max");
		fireChance = section.getDouble("chance");
		
	}
	
	public void buildFromConfigurationSectionWithGlobal(@Nullable ConfigurationSection section, @NotNull FireModuleConfig global) {
		if(section != null) {
			fireEnabled = section.getBoolean("enabled", global.fireEnabled);
			fireRadiusMin = section.getDouble("radius-min", global.fireRadiusMin);
			fireRadiusMax = section.getDouble("radius-max", global.fireRadiusMax);
			fireChance = section.getDouble("chance", global.fireChance);
		} else {
			fireEnabled = global.fireEnabled;
			fireRadiusMin = global.fireRadiusMin;
			fireRadiusMax = global.fireRadiusMax;
			fireChance = global.fireChance;
		}
	}
}
