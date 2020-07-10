package me.zabzabdoda.Classes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rogue extends CTFClass{
	
	public Rogue() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 15;
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
		ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
		ItemMeta im = sword.getItemMeta();
		im.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
		//im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("GENERIC_ATTACK_DAMAGE",5, AttributeModifier.Operation.ADD_NUMBER));
		sword.setItemMeta(im);
		items.add(sword);
		
		items.add(new ItemStack(Material.ENDER_PEARL));
		items.add(potionBuilder("Invisibility Potion",Color.GRAY,Material.POTION,1,PotionEffectType.INVISIBILITY,1,1000));
		return items;
	}
	
	
}
