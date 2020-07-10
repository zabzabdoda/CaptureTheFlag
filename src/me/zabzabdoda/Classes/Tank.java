package me.zabzabdoda.Classes;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tank extends CTFClass{
	
	public Tank() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
		offhand = offhand();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[2] = setUnbreakable(Material.IRON_CHESTPLATE,20,EquipmentSlot.CHEST);
		ItemStack item = setUnbreakable(Material.IRON_LEGGINGS);
		ItemMeta im = item.getItemMeta();
		im.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"Health",1,AttributeModifier.Operation.ADD_NUMBER));
		item.setItemMeta(im);
		items[1] = item;
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 40;
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
		items.add(setUnbreakable(Material.WOODEN_SWORD));
		
		return items;
	}
	
	private ItemStack offhand() {
		return setUnbreakable(Material.SHIELD);
	}
	
	
}
