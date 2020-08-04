package me.zabzabdoda.CaptureTheFlag;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.zabzabdoda.Classes.CTFClass;

public class CTFPlayer {
	
	private CTFClass playerClass;
	private Player player;
	private Inventory inv;
	private int respawnTime = 10;
	private Arena a;
	private String team;
	
	public CTFPlayer(Player player) {
		this.player = player;
	}
	
	public CTFPlayer(Player player, CTFClass playerClass) {
		this.player = player;
		this.playerClass = playerClass;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}
	
	public Arena getArena() {
		return a;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setArena(Arena a) {
		this.a = a;
	}
	
	public Player toPlayer() {
		return player;
	}
	
	public int getTime() {
		return respawnTime;
	}
	
	public void addTimer(int time) {
		respawnTime += time;
	}
	
	public void resetTimer() {
		respawnTime = 20;
	}
	
	public void setClass(CTFClass c) {
		playerClass = c;
	}
	
	//items to be given at the start of the game
	public void getAllItems(Color color) {
		if(playerClass != null) {
			playerClass.giveAllItems(player, color);
		}
	}
	
	//items to be given on respawn
	public void getRespawnItems(Color color) {
		if(playerClass != null) {
			playerClass.giveArmor(player);
			playerClass.giveItems(player);
			playerClass.giveEffects(player);
			playerClass.giveBasics(player, color);
			playerClass.giveOffhand(player);
		}
	}
	
	public int getMaxHealth() {
		return playerClass.getMaxHealth();
	}
	
}
