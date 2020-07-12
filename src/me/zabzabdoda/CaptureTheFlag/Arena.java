package me.zabzabdoda.CaptureTheFlag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * Represents an arena that has different properties such as a name, scoreboard, and
 * all the different positions like flag locations and spawn locations
 * @author zabzabdoda
 *
 */
public class Arena {
	private String name;
	private ScoreBoard sb;
	private RelicSpawner rs;
	private Location redTeamSpawn,blueTeamSpawn,redFlagSpawn,blueFlagSpawn,relicSpawn;
	private Location startSign,redSign,blueSign,classSign,redFlagReturn,blueFlagReturn,lobby,point1,point2;
	private Material wall;
	private boolean started;
	
	/**
	 * creates a new arena with a name
	 * @param name the name of the arena
	 */
	public Arena(String name) {
		this.name = name;
		//initializes a new scoreboard
		sb = new ScoreBoard();
		//makes a relic spawner with the position being relicSpawn
		rs = new RelicSpawner(relicSpawn);
	}
	
	
	/**
	 * runs a bukkitrunnable that spawns a relic every 5 minutes
	 * @param plugin the instance of the main plugin
	 */
	public void startRelicTimer(Plugin plugin) {
		rs.runTaskTimer(plugin, 12000, 6000);
	}
	
	/**
	 * sets the material that the wall inbetween the two teams is made of
	 * @param wall the material that the wall is made of
	 */
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
	 * @return an array of locations
	 */
	public Location[] getLocations() {
		return new Location[] {redTeamSpawn,blueTeamSpawn,redFlagSpawn,blueFlagSpawn,startSign,redSign,blueSign,classSign,redFlagReturn,blueFlagReturn,lobby,point1,point2,relicSpawn};
	}
}
