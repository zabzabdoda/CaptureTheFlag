package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Alchemist extends CTFClass{
	
	public Alchemist() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
	}
	
	
	
	private ArrayList<ItemStack> StartItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.COBBLESTONE,32));
		return items;
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[3] = setUnbreakable(Material.IRON_HELMET,5,EquipmentSlot.HEAD);
		items[0] = new ItemStack(Material.IRON_BOOTS);
		return items;
	}
	
	private ArrayList<PotionEffect> Effects() {
		ArrayList<PotionEffect> items = new ArrayList<PotionEffect>();
		items.add(new PotionEffect(PotionEffectType.SATURATION,1000000,100));
		return items;
	}
	
	private ArrayList<ItemStack> Items() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(setUnbreakable(Material.IRON_SWORD));
		items.add(potionBuilder("Strength Potion",Color.PURPLE,Material.POTION,1,PotionEffectType.INCREASE_DAMAGE,1,200));
		items.add(potionBuilder("Fire Resist Potion",Color.ORANGE,Material.POTION,1,PotionEffectType.FIRE_RESISTANCE,1,1200));
		items.add(potionBuilder("Speed Potion",Color.BLUE,Material.POTION,1,PotionEffectType.SPEED,1,300));
		return items;
	}



	@Override
	public int getMaxHealth() {
		return 25;
	}
	
	
}
