package com.idreesinc.celeste.config;

import com.idreesinc.celeste.CelesteRed;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfiguration {
	
	private Material type;
	private final Map<Enchantment, Integer> enchantements = new HashMap<>();
	private final boolean droppable;
	
	public ItemConfiguration(@Nullable ConfigurationSection section) {
		if(section == null)
			throw new IllegalStateException("null section");
		String typeStr = section.getString("type", "UNSET_ITEM_TYPE").toUpperCase();
		try {
			type = Material.valueOf(typeStr);
		} catch(IllegalArgumentException ignored) {
			CelesteRed.logError("Invalid item configuration section. Unknown type : '" + typeStr + "'.");
			type = Material.STONE;
		}
		droppable = section.getBoolean("droppable", true);
		
		List<Map<?,?>> lmap = section.getMapList("enchantements");
		try {
			for(Map<?,?> map : lmap) {
				String enchName = (String) map.get("name");
				int enchLevel = (int) map.get("level");
				enchantements.put(getEnchantment(enchName),  enchLevel);
			}
		} catch(Exception e) {
			CelesteRed.logError("Could not read enchantments in config : " + e.getMessage());
		}
	}
	
	public ItemStack create() {
		ItemStack item = new ItemStack(type);
		enchantements.forEach(item::addUnsafeEnchantment);
		return item;
	}
	
	@Override
	public String toString() {
		return "ItemConfiguration{"+type+", droppable="+droppable+", enchantments="+enchantements+"}";
	}
	
	public boolean isDroppable() {
		return droppable;
	}
	
	private static Enchantment getEnchantment(String name) {
		switch(name.toLowerCase()) {
			case "power":
				return Enchantment.ARROW_DAMAGE;
			case "punch":
				return Enchantment.ARROW_KNOCKBACK;
			case "fire":
				return Enchantment.ARROW_FIRE;
			case "infinity":
				return Enchantment.ARROW_INFINITE;
			case "multishot":
				return Enchantment.MULTISHOT;
			case "piercing":
				return Enchantment.PIERCING;
			case "protection":
				return Enchantment.PROTECTION_ENVIRONMENTAL;
			case "fire_protection":
				return Enchantment.PROTECTION_FIRE;
			case "feather_falling":
			case "fall_protection":
				return Enchantment.PROTECTION_FALL;
			case "blast_protection":
			case "protection_explostions":
				return Enchantment.PROTECTION_EXPLOSIONS;
			case "projectile_protection":
			case "projectiles_protection":
			case "arrow_protection":
				return Enchantment.PROTECTION_PROJECTILE;
			case "channeling":
				return Enchantment.CHANNELING;
			case "thorns":
				return Enchantment.THORNS;
				
				
			case "sharpness":
				return Enchantment.DAMAGE_ALL;
			case "smite":
				return Enchantment.DAMAGE_UNDEAD;
			case "banning_of_arthropods":
				return Enchantment.DAMAGE_ARTHROPODS;
			case "fire_aspect":
				return Enchantment.FIRE_ASPECT;
			case "sweeping_edge":
				return Enchantment.SWEEPING_EDGE;
			
			case "efficiency":
			case "dig_speed":
				return Enchantment.DIG_SPEED;
			case "unbreaking":
			case "unbreak":
				return Enchantment.DURABILITY;
			case "silk_touch":
				return Enchantment.SILK_TOUCH;
				
			case "luck":
				return Enchantment.LUCK;
			case "lure":
				return Enchantment.LURE;
			case "mending":
				return Enchantment.MENDING;
			case "loyalty":
				return Enchantment.LOYALTY;
			case "respiration":
				return Enchantment.OXYGEN;
			case "riptide":
				return Enchantment.RIPTIDE;
			case "depth_strider":
				return Enchantment.DEPTH_STRIDER;
			case "frost_walker":
				return Enchantment.FROST_WALKER;
			case "impaling":
				return Enchantment.IMPALING;
			
		}
		CelesteRed.logError("Uknown echantment name : '" + name + "'.");
		return Enchantment.DURABILITY;
	}
	
}
