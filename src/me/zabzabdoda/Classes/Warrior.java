package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warrior extends CTFClass{
	
	public Warrior() {
		armor = getArmor();
		startItems = warriorStartItems();
		items = warriorItems();
		effects = warriorEffects();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[2] = setUnbreakable(Material.IRON_CHESTPLATE,5,EquipmentSlot.CHEST);
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 25;
	}
	
	private ArrayList<ItemStack> warriorStartItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.COBBLESTONE,32));
		return items;
	}
	
	private ArrayList<PotionEffect> warriorEffects() {
		ArrayList<PotionEffect> items = new ArrayList<PotionEffect>();
		items.add(new PotionEffect(PotionEffectType.SATURATION,1000000,100));
		return items;
	}
	
	private ArrayList<ItemStack> warriorItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(setUnbreakable(Material.IRON_AXE));

		return items;
	}
	
	
}
