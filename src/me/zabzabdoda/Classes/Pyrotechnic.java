package me.zabzabdoda.Classes;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Pyrotechnic extends CTFClass{
	
	public Pyrotechnic() {
		armor = getArmor();
		startItems = StartItems();
		items = Items();
		effects = Effects();
		offhand = offhand();
	}
	
	private ItemStack[] getArmor() {
		ItemStack[] items = new ItemStack[4];
		items[3] = setUnbreakable(Material.IRON_HELMET);
		return items;
	}
	
	@Override
	public int getMaxHealth() {
		return 20;
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
		items.add(setUnbreakable(Material.IRON_SWORD));
		items.add(setUnbreakable(Material.CROSSBOW));
		items.add(setUnbreakable(Material.LAVA_BUCKET));
		items.add(new ItemStack(Material.FLINT_AND_STEEL));

		return items;
	}
	
	private ItemStack offhand() {
		ItemStack fireWork = new ItemStack(Material.FIREWORK_ROCKET,16);
		FireworkMeta fm = (FireworkMeta) fireWork.getItemMeta();
		Random random = new Random();
		fm.addEffect(FireworkEffect.builder().trail(true).with(Type.BURST).withColor(Color.fromRGB(random.nextInt(255), random.nextInt(255),random.nextInt(255))).build());
		fireWork.setItemMeta(fm);
		return fireWork;
	}
	
}
