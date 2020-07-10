package me.zabzabdoda.CaptureTheFlag;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class RelicSpawner extends BukkitRunnable{
	
	private ArrayList<ItemStack> relics;
	private Location loc;
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public RelicSpawner(Location loc) {
		relics = new ArrayList<ItemStack>();
		this.loc = loc;
		//bow
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta im = bow.getItemMeta();
		im.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
		im.addEnchant(Enchantment.ARROW_FIRE, 1, true);
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		Damageable c = (Damageable)im;
		c.setDamage(284);
		bow.setItemMeta(im);
		relics.add(bow);
		
		//sword
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		im = sword.getItemMeta();
		im.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
		im.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
		c = (Damageable)im;
		c.setDamage(1200);
		sword.setItemMeta(im);
		relics.add(sword);
		
		//chestplate
		ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		im = chestplate.getItemMeta();
		im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
		im.addEnchant(Enchantment.THORNS, 2, true);
		c = (Damageable)im;
		c.setDamage(400);
		chestplate.setItemMeta(im);
		relics.add(chestplate);
		
		//boots
		ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
		im = boots.getItemMeta();
		im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
		im.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"Speed",0.1,AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.FEET));
		boots.setItemMeta(im);
		relics.add(boots);
		
	}
	
	private ItemStack getRandomRelic() {
		Random rand = new Random();
		return relics.get(rand.nextInt(relics.size()));
	}
	
	public void forceSpawnRelic() {
		loc.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) loc.getBlock().getState();
		chest.getBlockInventory().addItem(getRandomRelic());
		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[Server] A " + ChatColor.GREEN + "Relic" + ChatColor.LIGHT_PURPLE + " has spawned");
	}
	
	@Override
	public void run() {
		loc.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) loc.getBlock().getState();
		ItemStack item = getRandomRelic();
		chest.getBlockInventory().addItem(item);
		if(item.getType().equals(Material.BOW)) {
			chest.getBlockInventory().addItem(new ItemStack(Material.ARROW));
		}
		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"[Server] A " + ChatColor.GREEN + "Relic" + ChatColor.LIGHT_PURPLE + " has spawned");

	}

}
