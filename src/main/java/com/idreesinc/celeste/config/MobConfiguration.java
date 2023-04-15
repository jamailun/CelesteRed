package com.idreesinc.celeste.config;

import com.idreesinc.celeste.CelesteRed;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import java.util.HashMap;
import java.util.Map;

public class MobConfiguration {
	
	private EntityType type;
	private final Map<EquipmentSlot, ItemConfiguration> equipment = new HashMap<>();
	private final double health;
	private final String name;
	
	public MobConfiguration(ConfigurationSection section) {
		String typeStr = section.getString("type", "VALUE_NOT_SET").toUpperCase();
		try {
			type = EntityType.valueOf(typeStr);
		} catch(IllegalArgumentException ignored) {
			CelesteRed.logError("Invalid mob configuration section. Unknown type : '" + typeStr + "'.");
			type = EntityType.WITHER_SKELETON;
		}
		health = section.getDouble("health", 20);
		name = section.getString("name", null);
		
		ConfigurationSection equipment = section.getConfigurationSection("equipment");
		if(equipment == null)
			return;
		
		for(String key : equipment.getKeys(false)) {
			EquipmentSlot es;
			try {
				es = EquipmentSlot.valueOf(key.toUpperCase());
			} catch(IllegalArgumentException ignored) {
				CelesteRed.logError("Invalid mob configuration section. Unknown equipment slot : '" + key.toUpperCase() + "'.");
				continue;
			}
			ItemConfiguration item = new ItemConfiguration(equipment.getConfigurationSection(key));
			this.equipment.put(es, item);
		}
	}
	
	public Entity spawn(Location location) {
		Entity e = location.getWorld().spawnEntity(location, type);
		if(name != null)
			e.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		if(e instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) e;
			le.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
			le.setHealth(health);
			
			EntityEquipment ee = le.getEquipment();
			for(EquipmentSlot slot : equipment.keySet()) {
				System.out.println(le + "->" + ee + " : +" + slot + " : " + equipment.get(slot));
				applyEquipment(ee, slot, equipment.get(slot));
			}
		}
		return e;
	}
	
	@Override
	public String toString() {
		return "MobConfiguration{\"" + name + "\", type=" + type +
				", health=" + health +
				", equipment=" + equipment + "}";
	}
	
	private void applyEquipment(EntityEquipment ee, EquipmentSlot slot, ItemConfiguration item) {
		switch (slot) {
			case HEAD:
				ee.setHelmet(item.create(), true);
				ee.setHelmetDropChance(item.isDroppable() ? 0.1f : 0);
				break;
			case CHEST:
				ee.setChestplate(item.create(), true);
				ee.setChestplateDropChance(item.isDroppable() ? 0.1f : 0);
				break;
			case LEGS:
				ee.setLeggings(item.create(), true);
				ee.setLeggingsDropChance(item.isDroppable() ? 0.1f : 0);
				break;
			case FEET:
				ee.setBoots(item.create(), true);
				ee.setBootsDropChance(item.isDroppable() ? 0.1f : 0);
				break;
			case HAND:
				ee.setItemInMainHand(item.create(), true);
				ee.setItemInMainHandDropChance(item.isDroppable() ? 0.1f : 0);
				break;
			case OFF_HAND:
				ee.setItemInOffHand(item.create(), true);
				ee.setItemInOffHandDropChance(item.isDroppable() ? 0.1f : 0);
				break;
		}
	}
	
}
