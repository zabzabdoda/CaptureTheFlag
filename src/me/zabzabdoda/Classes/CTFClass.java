package me.zabzabdoda.Classes;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class CTFClass {
	protected ArrayList<ItemStack> items;
	protected ItemStack[] armor;
	protected ArrayList<ItemStack> startItems;
	protected ArrayList<PotionEffect> effects;
	protected ItemStack offhand;
	
	public void giveAllItems(Player player,Color color){
		//player.getInventory().setArmorContents(armor);
		giveArmor(player);
		giveItems(player);
		giveEffects(player);
		giveStartItems(player);
		giveBasics(player,color);
		giveOffhand(player);
	}
	
	public void giveArmor(Player player) {
		player.getInventory().setArmorContents(armor);
	}
	
	public void giveOffhand(Player player) {
		player.getInventory().setItemInOffHand(offhand);
	}
	
	public void giveItems(Player player) {
		for(ItemStack item : items) {
			player.getInventory().addItem(item);
		}
		
	}
	
	public void giveStartItems(Player player) {
		for(ItemStack item : startItems) {
			player.getInventory().addItem(item);
		}
	}
	
	public void giveEffects(Player player) {
		for(PotionEffect pe : effects) {
			player.addPotionEffect(pe);
		}
	}
	
	public void giveBasics(Player player, Color color) {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta im = (LeatherArmorMeta) helmet.getItemMeta();
		im.setColor(color);
		im.setUnbreakable(true);
		helmet.setItemMeta(im);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		im = (LeatherArmorMeta) chestplate.getItemMeta();
		im.setColor(color);
		im.setUnbreakable(true);
		chestplate.setItemMeta(im);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		im = (LeatherArmorMeta) leggings.getItemMeta();
		im.setColor(color);
		im.setUnbreakable(true);
		leggings.setItemMeta(im);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		im = (LeatherArmorMeta) boots.getItemMeta();
		im.setColor(color);
		im.setUnbreakable(true);
		boots.setItemMeta(im);
		ItemStack[] armor = player.getInventory().getArmorContents(); 
		if(armor[3] == null) {
			armor[3] = helmet.clone();
		}
		if(armor[2] == null) {
			armor[2] = chestplate.clone();
		}
		if(armor[1] == null) {
			armor[1] = leggings.clone();
		}
		if(armor[0] == null) {
			armor[0] = boots.clone();
		}
		player.getInventory().setArmorContents(armor);
		player.getInventory().addItem(setUnbreakable(Material.WOODEN_PICKAXE));
		//player.getInventory().addItem(setUnbreakable(Material.WOODEN_AXE));
	}
	
	protected ItemStack setUnbreakable(Material mat) {
		ItemStack item = new ItemStack(mat);
		ItemMeta im = item.getItemMeta();
		im.setUnbreakable(true);
		item.setItemMeta(im);
		return item;
	}
	
	protected ItemStack setUnbreakable(Material mat, int health,EquipmentSlot slot) {
		ItemStack item = new ItemStack(mat);
		ItemMeta im = item.getItemMeta();
		im.setUnbreakable(true);
		im.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"Health",health,AttributeModifier.Operation.ADD_NUMBER,slot));
		item.setItemMeta(im);
		return item;
	}
	
	protected ItemStack potionBuilder(String name,Color color,Material mat,int amount, PotionEffectType eff, int time, int strength) {
		ItemStack is = new ItemStack(mat,amount);
		PotionMeta pm = (PotionMeta)is.getItemMeta();
		pm.setColor(color);
		pm.setDisplayName(ChatColor.BLUE+name);
		pm.addCustomEffect(new PotionEffect(eff,strength,time), true);
		is.setItemMeta(pm);
		return is;
	}
	
	public abstract int getMaxHealth();
	
}
