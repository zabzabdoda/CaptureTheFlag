package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archer extends CTFClass{
	
	public Archer() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
	}
	
	@Override
	public int getMaxHealth() {
		return 20;
	}

	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[3] = setUnbreakable(Material.IRON_HELMET);
		items[1] = setUnbreakable(Material.IRON_LEGGINGS);
		return items;
	}
	
	private ArrayList<ItemStack> StartItems() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.COBBLESTONE,32));
		return items;
	}
	
	private ArrayList<PotionEffect> Effects() {
		ArrayList<PotionEffect> items = new ArrayList<PotionEffect>();
		items.add(new PotionEffect(PotionEffectType.SATURATION,1000000,100));
		return items;
	}
	
	private ArrayList<ItemStack> Items() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(setUnbreakable(Material.STONE_SWORD));
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta im = bow.getItemMeta();
		im.setUnbreakable(true);
		//im.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
		bow.setItemMeta(im);
		items.add(bow);
		items.add(new ItemStack(Material.ARROW,32));
		return items;
	}
	
	
}
