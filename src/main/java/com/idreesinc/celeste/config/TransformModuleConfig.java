package com.idreesinc.celeste.config;

import com.idreesinc.celeste.CelesteRed;
import com.idreesinc.celeste.utilities.WeightedRandomBag;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransformModuleConfig {
	
	public boolean enabled;
	public double radiusMax;
	public double chance;
	public double duration;
	public WeightedRandomBag<Material> possibilities;
	
	public void buildFromConfigurationSection(@Nullable ConfigurationSection section) {
		if(section == null) {
			enabled = false;
			return;
		}
		enabled = section.getBoolean("enabled");
		radiusMax = section.getDouble("radius-max");
		chance = section.getDouble("chance");
		duration = section.getDouble("duration");
		possibilities = calculatePossibilities(section.getConfigurationSection("blocks"));
	}
	
	public void buildFromConfigurationSectionWithGlobal(@Nullable ConfigurationSection section, @NotNull TransformModuleConfig global) {
		if(section != null) {
			enabled = section.getBoolean("enabled", global.enabled);
			radiusMax = section.getDouble("radius-max", global.radiusMax);
			chance = section.getDouble("chance", global.chance);
			duration = section.getDouble("duration", global.duration);
			if(section.isSet("blocks")) {
				possibilities = calculatePossibilities(section.getConfigurationSection("blocks"));
			} else {
				possibilities = global.possibilities;
			}
		} else {
			enabled = global.enabled;
			radiusMax = global.radiusMax;
			chance = global.chance;
			duration = global.duration;
			possibilities = global.possibilities;
		}
	}
	
	private WeightedRandomBag<Material> calculatePossibilities(@Nullable ConfigurationSection possibilities) {
		WeightedRandomBag<Material> materials = new WeightedRandomBag<>();
		if(possibilities == null)
			return materials;
		for (String key : possibilities.getKeys(false)) {
			try {
				Material mat = Material.valueOf(key.toUpperCase());
				materials.addEntry(mat, possibilities.getDouble(key));
			} catch (IllegalArgumentException e) {
				CelesteRed.logError("config error in red falling possibilities : material '" + key.toUpperCase() + "' does not exist.");
			}
		}
		return materials;
	}
	
}
