package me.zabzabdoda.CaptureTheFlag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class Arena {
	private String name;
	private ScoreBoard sb;
	private RelicSpawner rs;
	private Location redTeamSpawn,blueTeamSpawn,redFlagSpawn,blueFlagSpawn,relicSpawn;
	private Location startSign,redSign,blueSign,classSign,redFlagReturn,blueFlagReturn,lobby,point1,point2;
	private Material wall;
	private boolean started;
	
	public Arena(String name) {
		this.name = name;
		sb = new ScoreBoard();
		rs = new RelicSpawner(relicSpawn);
	}
	
	public void startRelicTimer(Plugin plugin) {
		rs.runTaskTimer(plugin, 12000, 6000);
	}
	
	public void setWallType(Material wall) {
		this.wall = wall;
	}
	
	public void started(boolean b) {
		started = b;
	}
	
	public boolean getStarted() {
		return started;
	}
	
	public void forceRelic() {
		rs.forceSpawnRelic();
	}
	
	public void stopRelics() {
		rs.cancel();
	}
	
	public void setRelic(Location loc) {
		relicSpawn = loc;
		rs.setLocation(loc);
	}
	
	public void setRedSpawn(Location loc) {
		redTeamSpawn = loc;
	}
	
	public void setRedFlag(Location loc) {
		redFlagSpawn = loc;
	}
	
	public void setBlueSpawn(Location loc) {
		blueTeamSpawn = loc;
	}
	
	public void setBlueFlag(Location loc) {
		blueFlagSpawn = loc;
	}
	
	public void setRedFlagReturn(Location loc) {
		redFlagReturn = loc;
	}
	
	public void setBlueFlagReturn(Location loc) {
		blueFlagReturn = loc;
	}
	
	public void setSign(Location loc) {
		startSign = loc;
	}
	
	public void setRedSign(Location loc) {
		redSign = loc;
	}
	
	public void setLobby(Location loc) {
		lobby = loc;
	}
	
	public void setBlueSign(Location loc) {
		blueSign = loc;
	}
	
	public String getName() {
		return name;
	}
	
	public void setClassSign(Location loc) {
		classSign = loc;
	}
	
	public ScoreBoard getScoreBoard() {
		return sb;
	}
	
	public void setPoint1(Location loc) {
		this.point1 = loc;
	}
	
	public void setPoint2(Location loc) {
		this.point2 = loc;
	}
	
	public Material getWallType() {
		return wall;
	}
	
	/**
	 * returns the locations associated with this arena
	 * in this order:
	 * redTeamSpawn
	 * blueTeamSpawn
	 * redTeamFlag
	 * blueTeamFlag
	 * startSign
	 * @return
	 */
	public Location[] getLocations() {
		return new Location[] {redTeamSpawn,blueTeamSpawn,redFlagSpawn,blueFlagSpawn,startSign,redSign,blueSign,classSign,redFlagReturn,blueFlagReturn,lobby,point1,point2,relicSpawn};
	}
}
