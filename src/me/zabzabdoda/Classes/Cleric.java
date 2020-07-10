package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cleric extends CTFClass{
	
	public Cleric() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[1] = setUnbreakable(Material.IRON_LEGGINGS);
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 10;
	}
	
	private ArrayList<ItemStack> StartItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.COBBLESTONE,32));
		return items;
	}
	
	private ArrayList<PotionEffect> Effects() {
		ArrayList<PotionEffect> items = new ArrayList<PotionEffect>();
		items.add(new PotionEffect(PotionEffectType.SATURATION,1000000,100));
		items.add(new PotionEffect(PotionEffectType.REGENERATION,1000000,1));
		return items;
	}
	
	private ArrayList<ItemStack> Items() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		items.add(setUnbreakable(Material.STONE_SWORD));
		items.add(potionBuilder("Health Potion",Color.RED,Material.SPLASH_POTION,10,PotionEffectType.HEAL,1,20));
		items.add(potionBuilder("Health Potion (Area)",Color.RED,Material.LINGERING_POTION,2,PotionEffectType.HEAL,1,250));
		return items;
	}
	
	
}
