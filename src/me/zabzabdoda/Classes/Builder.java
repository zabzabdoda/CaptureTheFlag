package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Builder extends CTFClass{
	
	public Builder() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[0] = setUnbreakable(Material.IRON_BOOTS);
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 20;
	}
	
	private ArrayList<ItemStack> StartItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.COBBLESTONE,64));
		items.add(new ItemStack(Material.COBBLESTONE,64));
		items.add(new ItemStack(Material.COBBLESTONE,32));
		items.add(new ItemStack(Material.OAK_PLANKS,64));
		items.add(new ItemStack(Material.OAK_PLANKS,64));
		items.add(new ItemStack(Material.OAK_PLANKS,32));
		items.add(new ItemStack(Material.WATER_BUCKET));
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
		items.add(setUnbreakable(Material.IRON_PICKAXE));
		return items;
	}
	
	
}
