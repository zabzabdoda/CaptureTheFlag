package me.zabzabdoda.CaptureTheFlag;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import me.zabzabdoda.Classes.*;

/**
 * Capture the flag plugin that allows user to setup
 * game on custom map and play with friends 
 * @author zabzabdoda
 *
 */
public class CaptureTheFlag extends JavaPlugin implements Listener {

	private ArrayList<CTFPlayer> players;
	private ArrayList<Arena> arenas;
	private static final String SERVER_TAG = ChatColor.LIGHT_PURPLE+"[Server]";

	public void onEnable() {
		System.out.println("[CaptureTheFlag] Starting CaptureTheFlag Plugin...");
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		players = new ArrayList<CTFPlayer>();
		arenas = new ArrayList<Arena>();
		//gets the arena folder in the plugin directory
		File Arenas = new File(this.getDataFolder() + File.separator + "Arenas");
		//creates the data folder if it doesn't exist
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		//creates the arena folder if it doesn't exist
		if (!Arenas.exists()) {
			Arenas.mkdir();
		}
		//converts all files in the arena folder into arena objects and adds those to an arraylist
		for(File f : Arenas.listFiles()) {
			addArena(f);
		}
	}

	public void onDisable() {
		System.out.println("[CaptureTheFlag] Ending CaptureTheFlag Plugin...");

	}

	public void printList(Player player, String name) {
		Arena a = findArena(name);
		for (Location l : a.getLocations()) {
			player.sendMessage(l.toString());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("ctf")) {
				if(player.hasPermission("ctf.commands")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("help")) {
							player.sendMessage(getHelpMenu());
							return true;
						}
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("start")) {
							startGame(args[1]);
							player.sendMessage("Starting game.");
							return true;
						} else if (args[0].equalsIgnoreCase("list")) {
							printList(player, args[1]);
							return true;
						} else if (args[0].equalsIgnoreCase("save")) {
							makeNewArena(args[1]);
							player.sendMessage("Arena saved as: " + args[1]);
							return true;
						} else if (args[0].equalsIgnoreCase("create")) {
							arenas.add(new Arena(args[1]));
							player.sendMessage("Arena created as: " + args[1]);
							return true;
						} else if (args[0].equalsIgnoreCase("health")) {
							Player p = Bukkit.getPlayer(args[1]);
							AttributeInstance ai = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
							ai.addModifier(new AttributeModifier("GENERIC_MAX_HEALTH",-50,AttributeModifier.Operation.ADD_NUMBER));
							p.setHealth(20);
							player.sendMessage("Reset " + p.getDisplayName() + "'s health to: " + p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
							return true;
						} else if (args[0].equalsIgnoreCase("relic")) {
							Arena a = findArena(args[1]);
							a.forceRelic();
							player.sendMessage("Relic spawned in: " + args[1]);
							return true;
						}
					} else if (args.length == 3) {
						if (args[0].equalsIgnoreCase("join")) {
							Arena a = findArena(args[2]);
							if (args[1].equalsIgnoreCase("Red")) {
								a.getScoreBoard().joinRed(player);
								player.sendMessage(ChatColor.RED + "Joined Red Team.");
							} else if (args[1].equalsIgnoreCase("Blue")) {
								a.getScoreBoard().joinBlue(player);
								player.sendMessage(ChatColor.BLUE + "Joined Blue Team.");
							}
							return true;
						} else if (args[0].equalsIgnoreCase("leave")) {
							Arena a = findArena(args[2]);
							Team team = player.getScoreboard().getPlayerTeam(player);
							if (team != null) {
								a.getScoreBoard().leaveTeam(player, team);
								return true;
							}
						}
					}else if(args.length == 6) {//ctf set lobby Sand 10 0 10
						if (args[0].equalsIgnoreCase("set")) {
							if (args[1].equalsIgnoreCase("lobby")) {
								int x = Integer.parseInt(args[3]);
								int y = Integer.parseInt(args[4]);
								int z = Integer.parseInt(args[5]);
								findArena(args[2]).setLobby(new Location(player.getWorld(), x, y, z));
								player.sendMessage("Lobby set");
								return true;
							}else if (args[1].equalsIgnoreCase("point1")) {
								int x = Integer.parseInt(args[3]);
								int y = Integer.parseInt(args[4]);
								int z = Integer.parseInt(args[5]);
								findArena(args[2]).setPoint1(new Location(player.getWorld(), x, y, z));
								player.sendMessage("point1 set");
								return true;
							}else if (args[1].equalsIgnoreCase("point2")) {
								int x = Integer.parseInt(args[3]);
								int y = Integer.parseInt(args[4]);
								int z = Integer.parseInt(args[5]);
								findArena(args[2]).setPoint2(new Location(player.getWorld(), x, y, z));
								player.sendMessage("point2 set");
								return true;
							}else if (args[1].equalsIgnoreCase("relic")) {
								int x = Integer.parseInt(args[3]);
								int y = Integer.parseInt(args[4]);
								int z = Integer.parseInt(args[5]);
								findArena(args[2]).setRelic(new Location(player.getWorld(), x, y, z));
								player.sendMessage("relic set");
								return true;
							}
						}
					} else if (args.length == 7) {
						if (args[0].equalsIgnoreCase("set")) {
							if (args[1].equalsIgnoreCase("spawn")) {
								int x = Integer.parseInt(args[4]);
								int y = Integer.parseInt(args[5]);
								int z = Integer.parseInt(args[6]);
								if (args[3].equalsIgnoreCase("red")) {
									findArena(args[2]).setRedSpawn(new Location(player.getWorld(), x, y, z));
								} else if (args[3].equalsIgnoreCase("blue")) {
									findArena(args[2]).setBlueSpawn(new Location(player.getWorld(), x, y, z));
								}
								player.sendMessage("New team spawn added.");
								return true;
							} else if (args[1].equalsIgnoreCase("flag")) {
								int x = Integer.parseInt(args[4]);
								int y = Integer.parseInt(args[5]);
								int z = Integer.parseInt(args[6]);
								if (args[3].equalsIgnoreCase("red")) {
									findArena(args[2]).setRedFlag(new Location(player.getWorld(), x, y, z));
								} else if (args[3].equalsIgnoreCase("blue")) {
									findArena(args[2]).setBlueFlag(new Location(player.getWorld(), x, y, z));
								}
								player.sendMessage("New flag spawn added.");
								return true;
							} else if (args[1].equalsIgnoreCase("return")) {
								int x = Integer.parseInt(args[4]);
								int y = Integer.parseInt(args[5]);
								int z = Integer.parseInt(args[6]);
								if (args[3].equalsIgnoreCase("red")) {
									findArena(args[2]).setRedFlagReturn(new Location(player.getWorld(), x, y, z));
								} else if (args[3].equalsIgnoreCase("blue")) {
									findArena(args[2]).setBlueFlagReturn(new Location(player.getWorld(), x, y, z));
								}
								player.sendMessage("New flag return added.");
								return true;
								
							}
						}
					}
				}else {
					sender.sendMessage(ChatColor.RED+"You don't have permission to use commands");
					return true;
				}
			}
		}
		sender.sendMessage(ChatColor.RED+"Incorrect usage, type /ctf help for help");
		return true;
	}

	//finds an arena by its name
	public Arena findArena(String name) {
		for (Arena a : arenas) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	
	//adds a new arena by loading the file in a properties object
	//then reads all the data about the arena and adds it to its fields
	public void addArena(File f) {
		Properties prop = new Properties();
		Arena a = new Arena(f.getName().substring(0,f.getName().indexOf(".")));
		try {
			FileReader fr = new FileReader(f);
			prop.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String world;
		String x;
		String y;
		String z;
		Location loc;
		world = prop.getProperty("RedSpawnWorld");
		x = prop.getProperty("RedSpawnX");
		y = prop.getProperty("RedSpawnY");
		z = prop.getProperty("RedSpawnZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setRedSpawn(loc);
		
		world = prop.getProperty("BlueSpawnWorld");
		x = prop.getProperty("BlueSpawnX");
		y = prop.getProperty("BlueSpawnY");
		z = prop.getProperty("BlueSpawnZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setBlueSpawn(loc);
		
		world = prop.getProperty("RedFlagWorld");
		x = prop.getProperty("RedFlagX");
		y = prop.getProperty("RedFlagY");
		z = prop.getProperty("RedFlagZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setRedFlag(loc);
		
		world = prop.getProperty("BlueFlagWorld");
		x = prop.getProperty("BlueFlagX");
		y = prop.getProperty("BlueFlagY");
		z = prop.getProperty("BlueFlagZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setBlueFlag(loc);
		
		world = prop.getProperty("StartSignWorld");
		x = prop.getProperty("StartSignX");
		y = prop.getProperty("StartSignY");
		z = prop.getProperty("StartSignZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setSign(loc);
		
		world = prop.getProperty("RedSignWorld");
		x = prop.getProperty("RedSignX");
		y = prop.getProperty("RedSignY");
		z = prop.getProperty("RedSignZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setRedSign(loc);
		
		world = prop.getProperty("BlueSignWorld");
		x = prop.getProperty("BlueSignX");
		y = prop.getProperty("BlueSignY");
		z = prop.getProperty("BlueSignZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setBlueSign(loc);
		
		world = prop.getProperty("ClassSignWorld");
		x = prop.getProperty("ClassSignX");
		y = prop.getProperty("ClassSignY");
		z = prop.getProperty("ClassSignZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setClassSign(loc);
		
		world = prop.getProperty("RedTeamReturnWorld");
		x = prop.getProperty("RedTeamReturnX");
		y = prop.getProperty("RedTeamReturnY");
		z = prop.getProperty("RedTeamReturnZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setRedFlagReturn(loc);
		
		world = prop.getProperty("BlueTeamReturnWorld");
		x = prop.getProperty("BlueTeamReturnX");
		y = prop.getProperty("BlueTeamReturnY");
		z = prop.getProperty("BlueTeamReturnZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setBlueFlagReturn(loc);
		
		world = prop.getProperty("LobbyWorld");
		x = prop.getProperty("LobbyX");
		y = prop.getProperty("LobbyY");
		z = prop.getProperty("LobbyZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setLobby(loc);
		
		world = prop.getProperty("Point1World");
		x = prop.getProperty("Point1X");
		y = prop.getProperty("Point1Y");
		z = prop.getProperty("Point1Z");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setPoint1(loc);
		
		world = prop.getProperty("Point2World");
		x = prop.getProperty("Point2X");
		y = prop.getProperty("Point2Y");
		z = prop.getProperty("Point2Z");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setPoint2(loc);
		
		world = prop.getProperty("RelicWorld");
		x = prop.getProperty("RelicX");
		y = prop.getProperty("RelicY");
		z = prop.getProperty("RelicZ");
		loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
		a.setRelic(loc);
		
		String smat = prop.getProperty("WallBlock");
		Material mat = Material.matchMaterial(smat);
		if(mat != null) {
			a.setWallType(mat);
		}else {
			a.setWallType(Material.BARRIER);
		}
		arenas.add(a);
	}

	
	@EventHandler
	public void breakblock(BlockBreakEvent e) {
		CTFPlayer p = getCTFPlayer(e.getPlayer());
		Arena a = p.getArena();
		if(p != null && a != null) {

		if(p.getTeam().equals("red")) {
			if(e.getBlock().getState().getType().equals(Material.RED_BANNER)) {
				//return
				if(!e.getBlock().getLocation().equals(a.getLocations()[2])) {
					Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + " has returned the " + ChatColor.RED + "Red Flag");
					e.getBlock().setType(Material.AIR);
					a.getLocations()[2].getBlock().setType(Material.RED_BANNER);
				}
				e.setCancelled(true);
			}else if(e.getBlock().getState().getType().equals(Material.BLUE_BANNER)) {
				//pick up
				Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + " has picked up the " + ChatColor.BLUE + "Blue Flag");
				e.getBlock().setType(Material.AIR);
				ItemStack flag = new ItemStack(Material.BLUE_BANNER);
				ItemMeta im = flag.getItemMeta();
				im.setDisplayName(ChatColor.BLUE+"Blue Flag");
				flag.setItemMeta(im);
				e.getPlayer().getInventory().addItem(flag);
				e.setCancelled(true);
			}
		}else if(p.getTeam().equals("blue")) {
			if(e.getBlock().getState().getType().equals(Material.BLUE_BANNER)) {
				//return
				if(!e.getBlock().getLocation().equals(a.getLocations()[3])) {
					Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + " has returned the " + ChatColor.BLUE + "Blue Flag");
					e.getBlock().setType(Material.AIR);
					a.getLocations()[3].getBlock().setType(Material.BLUE_BANNER);
				}
				e.setCancelled(true);
			}else if(e.getBlock().getState().getType().equals(Material.RED_BANNER)) {
				//pick up
				Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + " has picked up the " + ChatColor.RED + "Red Flag");
				e.getBlock().setType(Material.AIR);
				ItemStack flag = new ItemStack(Material.RED_BANNER);
				ItemMeta im = flag.getItemMeta();
				im.setDisplayName(ChatColor.RED+"Red Flag");
				flag.setItemMeta(im);
				e.getPlayer().getInventory().addItem(flag);
				e.setCancelled(true);
			}
			
			
			
		}
		}
	}
	
	public void makeNewArena(String name) {
		Properties prop = new Properties();
		Arena a = findArena(name);
		if(a != null) {
			Location[] locs = a.getLocations();
	
			prop.put("RedSpawnWorld", locs[0].getWorld().getName());
			prop.put("RedSpawnX", locs[0].getBlockX() + "");
			prop.put("RedSpawnY", locs[0].getBlockY() + "");
			prop.put("RedSpawnZ", locs[0].getBlockZ() + "");
			prop.put("BlueSpawnWorld", locs[1].getWorld().getName());
			prop.put("BlueSpawnX", locs[1].getBlockX() + "");
			prop.put("BlueSpawnY", locs[1].getBlockY() + "");
			prop.put("BlueSpawnZ", locs[1].getBlockZ() + "");
	
			prop.put("RedFlagWorld", locs[2].getWorld().getName());
			prop.put("RedFlagX", locs[2].getBlockX() + "");
			prop.put("RedFlagY", locs[2].getBlockY() + "");
			prop.put("RedFlagZ", locs[2].getBlockZ() + "");
			prop.put("BlueFlagWorld", locs[3].getWorld().getName());
			prop.put("BlueFlagX", locs[3].getBlockX() + "");
			prop.put("BlueFlagY", locs[3].getBlockY() + "");
			prop.put("BlueFlagZ", locs[3].getBlockZ() + "");
	
			prop.put("StartSignWorld", locs[4].getWorld().getName());
			prop.put("StartSignX", locs[4].getBlockX() + "");
			prop.put("StartSignY", locs[4].getBlockY() + "");
			prop.put("StartSignZ", locs[4].getBlockZ() + "");
			
			prop.put("RedSignWorld", locs[5].getWorld().getName());
			prop.put("RedSignX", locs[5].getBlockX() + "");
			prop.put("RedSignY", locs[5].getBlockY() + "");
			prop.put("RedSignZ", locs[5].getBlockZ() + "");
			
			prop.put("BlueSignWorld", locs[6].getWorld().getName());
			prop.put("BlueSignX", locs[6].getBlockX() + "");
			prop.put("BlueSignY", locs[6].getBlockY() + "");
			prop.put("BlueSignZ", locs[6].getBlockZ() + "");
			
			prop.put("ClassSignWorld", locs[7].getWorld().getName());
			prop.put("ClassSignX", locs[7].getBlockX() + "");
			prop.put("ClassSignY", locs[7].getBlockY() + "");
			prop.put("ClassSignZ", locs[7].getBlockZ() + "");
			
			prop.put("RedTeamReturnWorld", locs[8].getWorld().getName());
			prop.put("RedTeamReturnX", locs[8].getBlockX() + "");
			prop.put("RedTeamReturnY", locs[8].getBlockY() + "");
			prop.put("RedTeamReturnZ", locs[8].getBlockZ() + "");
			
			prop.put("BlueTeamReturnWorld", locs[9].getWorld().getName());
			prop.put("BlueTeamReturnX", locs[9].getBlockX() + "");
			prop.put("BlueTeamReturnY", locs[9].getBlockY() + "");
			prop.put("BlueTeamReturnZ", locs[9].getBlockZ() + "");
			
			prop.put("LobbyWorld", locs[10].getWorld().getName());
			prop.put("LobbyX", locs[10].getBlockX() + "");
			prop.put("LobbyY", locs[10].getBlockY() + "");
			prop.put("LobbyZ", locs[10].getBlockZ() + "");
			
			prop.put("Point1World", locs[11].getWorld().getName());
			prop.put("Point1X", locs[11].getBlockX() + "");
			prop.put("Point1Y", locs[11].getBlockY() + "");
			prop.put("Point1Z", locs[11].getBlockZ() + "");
			
			prop.put("Point2World", locs[12].getWorld().getName());
			prop.put("Point2X", locs[12].getBlockX() + "");
			prop.put("Point2Y", locs[12].getBlockY() + "");
			prop.put("Point2Z", locs[12].getBlockZ() + "");
			
			prop.put("WallBlock", a.getWallType() + "");
			
			prop.put("RelicWorld", locs[13].getWorld().getName());
			prop.put("RelicX", locs[13].getBlockX() + "");
			prop.put("RelicY", locs[13].getBlockY() + "");
			prop.put("RelicZ", locs[13].getBlockZ() + "");
			
			try {
				File config = new File(this.getDataFolder() + File.separator + "Arenas" + File.separator + name + ".yml");
				config.createNewFile();
				FileWriter fw = new FileWriter(config);
				prop.store(fw, "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void startGame(String name) {
		Arena a = findArena(name);
		if(a != null) {
			for(CTFPlayer player : players) {
				if(player.getArena().equals(a)) {
					if(player.getTeam().equals("red")) {
						player.toPlayer().teleport(a.getLocations()[0]);
						player.getAllItems(Color.RED);
						player.toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEAL,100,100));

					}else if(player.getTeam().equals("blue")) {
						player.toPlayer().teleport(a.getLocations()[1]);
						player.getAllItems(Color.BLUE);
						player.toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEAL,100,100));

					}
				}
			}
			a.started(true);
			a.getLocations()[2].getBlock().setType(Material.RED_BANNER);
			a.getLocations()[3].getBlock().setType(Material.BLUE_BANNER);
			Bukkit.broadcastMessage(SERVER_TAG + " Starting Game On Arena: " + ChatColor.GREEN + a.getName());
			wallTimer(a);
			a.startRelicTimer(this);
		}
	}
	
	@EventHandler
	public void craft(CraftItemEvent e) {
		if(!e.getWhoClicked().hasPermission("ctf.craft") && e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) {
			e.setCancelled(true);
			e.getWhoClicked().sendMessage(ChatColor.RED + "You can't craft");
		}
	}

	
	public void wallTimer(Arena a) {
		Location l1 = a.getLocations()[11];
		Location l2 = a.getLocations()[12];
		int x1 = Math.min(l1.getBlockX(),l2.getBlockX());
		int y1 = Math.min(l1.getBlockY(),l2.getBlockY());
		int z1 = Math.min(l1.getBlockZ(),l2.getBlockZ());
		
		int x2 = Math.max(l1.getBlockX(),l2.getBlockX());
		int y2 = Math.max(l1.getBlockY(),l2.getBlockY());
		int z2 = Math.max(l1.getBlockZ(),l2.getBlockZ());
		Bukkit.broadcastMessage(SERVER_TAG+" Starting in 5 minutes!");
		Bukkit.broadcastMessage(SERVER_TAG+" Start building your defenses");
		new BukkitRunnable() {
			int counter = 300;
			@Override
			public void run() {
				if(counter == 120) { //2 min
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 2 minutes!");
				}else if(counter == 60) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 1 minute!");
				}else if(counter == 5) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 5 seconds!");
				}else if(counter == 4) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 4 seconds!");
				}else if(counter == 3) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 3 seconds!");
				}else if(counter == 2) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 2 seconds!");
				}else if(counter == 1) {
					Bukkit.broadcastMessage(SERVER_TAG+" Starting in 1 second!");
				}else if(counter == 0) {
					Bukkit.broadcastMessage(SERVER_TAG+" Game Started!");
					for(int i = x1; i <= x2; i++) {
						for(int j = y1; j <= y2; j++) {
							for(int f = z1; f <= z2; f++) {
								Block b = l1.getWorld().getBlockAt(new Location(l1.getWorld(),i,j,f));
								if(b.getType().equals(a.getWallType())) {
									b.setType(Material.AIR);
								}
							}
						}
					}
					cancel();
				}
				counter--;
			}
			
		}.runTaskTimer(this, 0, 20);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if (b.getBlockData().getMaterial().equals(Material.OAK_WALL_SIGN)) {
				Sign sign = (Sign) b.getState();
				if (sign.getLine(0).equals("-!start!-")) {
					String name = sign.getLine(1);
					findArena(sign.getLine(1)).setSign(sign.getLocation());
					sign.setLine(0, "-----------");
					sign.setLine(1, ChatColor.GREEN + "[Start Game]");
					sign.setLine(2, name);
					sign.setLine(3, "-----------");
					sign.update();
				}else if (sign.getLine(0).equals("-!red!-")) {
					String name = sign.getLine(1);
					findArena(name).setRedSign(sign.getLocation());
					sign.setLine(0, "-----------");
					sign.setLine(1, ChatColor.RED + "[Join Red]");
					sign.setLine(2, name);
					sign.setLine(3, "-----------");
					sign.update();
				}else if (sign.getLine(0).equals("-!blue!-")) {
					String name = sign.getLine(1);
					findArena(name).setBlueSign(sign.getLocation());
					sign.setLine(0, "-----------");
					sign.setLine(1, ChatColor.BLUE + "[Join Blue]");
					sign.setLine(2, name);
					sign.setLine(3, "-----------");
					sign.update();
				}else if (sign.getLine(0).equals("-!class!-")) {
					String name = sign.getLine(1);
					findArena(name).setClassSign(sign.getLocation());
					sign.setLine(0, "-----------");
					sign.setLine(1, ChatColor.GREEN + "[Pick Class]");
					sign.setLine(2, name);
					sign.setLine(3, "-----------");
					sign.update();
				}else if(sign.getLine(1).equals(ChatColor.BLUE + "[Join Blue]")) {
					if(e.getPlayer().hasPermission("ctf.jointeam")) {
						String name = sign.getLine(2);
						Arena a = findArena(name);
						CTFPlayer p = getCTFPlayer(e.getPlayer());
						a.getScoreBoard().joinBlue(e.getPlayer());
						p.setTeam("blue");
						p.setArena(a);
					}
				}else if(sign.getLine(1).equals(ChatColor.RED + "[Join Red]")) {
					if(e.getPlayer().hasPermission("ctf.jointeam")) {
						String name = sign.getLine(2);
						Arena a = findArena(name);
						CTFPlayer p = getCTFPlayer(e.getPlayer());
						a.getScoreBoard().joinRed(e.getPlayer());
						p.setTeam("red");
						p.setArena(a);
					}
				}else if(sign.getLine(1).equals(ChatColor.GREEN + "[Start Game]")) {
					if(e.getPlayer().hasPermission("ctf.start")) {
						String name = sign.getLine(2);
						startGame(name);
					}else {
						e.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to start the game");
					}
				}else if(sign.getLine(1).equals(ChatColor.GREEN + "[Pick Class]")) {
					String name = sign.getLine(2);
					Player player = e.getPlayer();
					Arena a = findArena(name);
					CTFPlayer cp = getCTFPlayer(player);
					cp.setArena(a);
					if(cp != null) {
						openClassMenu(cp);
					}
				}
			}
		}
	}
	
	public void openClassMenu(CTFPlayer player) {
		Inventory inv = Bukkit.createInventory(player.toPlayer(), 9);
		inv.addItem(itemName(ChatColor.GREEN+"Warrior",Material.IRON_AXE));
		inv.addItem(itemName(ChatColor.GREEN+"Tank",Material.SHIELD));
		inv.addItem(itemName(ChatColor.GREEN+"Archer",Material.BOW));
		inv.addItem(itemName(ChatColor.GREEN+"Alchemist",Material.SPLASH_POTION));
		inv.addItem(itemName(ChatColor.GREEN+"Pyrotechnic",Material.LAVA_BUCKET));
		inv.addItem(itemName(ChatColor.GREEN+"Builder",Material.COBBLESTONE));
		inv.addItem(itemName(ChatColor.GREEN+"Cleric",Material.POTION));
		inv.addItem(itemName(ChatColor.GREEN+"Rogue",Material.GOLDEN_SWORD));
		inv.addItem(itemName(ChatColor.RED+"Close",Material.BARRIER));
		player.setInventory(inv);
		player.toPlayer().openInventory(inv);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		int slot = e.getSlot();
		Player p = (Player) e.getWhoClicked();
		CTFPlayer pl = getCTFPlayer(p);
		if(pl != null) {
			if(e.getInventory().equals(pl.getInventory())) {
				if(slot == 0) {//warrior
					pl.setClass(new Warrior());
				}else if(slot == 1) {//tank
					pl.setClass(new Tank());
				}else if(slot == 2) {//archer
					pl.setClass(new Archer());
				}else if(slot == 3) {//Alchemist
					pl.setClass(new Alchemist());
				}else if(slot == 4) {//Pyrotechnic
					pl.setClass(new Pyrotechnic());
				}else if(slot == 5) {//Builder
					pl.setClass(new Builder());
				}else if(slot == 6) {//Cleric
					pl.setClass(new Cleric());
				}else if(slot == 7) {//Rogue
					pl.setClass(new Rogue());
				}else if(slot == 8) {
					p.closeInventory();
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent e){
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			CTFPlayer p = getCTFPlayer(player);
			if(e.getDamage() >= player.getHealth() && p != null && p.getArena() != null && p.getArena().getStarted()) {
				e.setDamage(0);
				respawn(p);
				if(e.getCause() == DamageCause.ENTITY_ATTACK) {
					EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
					String killerName = event.getDamager().getCustomName();
					String deadPerson = event.getEntity().getCustomName();
					if(event.getDamager() instanceof Player) {
						Player pD = (Player)(event.getDamager());
						killerName = pD.getDisplayName();
					}
					if(event.getEntity() instanceof Player) {
						Player pD = (Player)(event.getEntity());
						deadPerson = pD.getDisplayName();
					}
					Bukkit.broadcastMessage(deadPerson + " has been killed by " + killerName);
				}
			}
		}
	}
	
	public void respawn(CTFPlayer player) {
		player.toPlayer().setGameMode(GameMode.SPECTATOR);
		if(player.toPlayer().getInventory().contains(Material.RED_BANNER)) {
			player.toPlayer().getLocation().getBlock().setType(Material.RED_BANNER);
			Bukkit.broadcastMessage(player.toPlayer().getDisplayName()+" has dropped the" + ChatColor.RED + " Red Flag");
		}else if(player.toPlayer().getInventory().contains(Material.BLUE_BANNER)) {
			player.toPlayer().getLocation().getBlock().setType(Material.BLUE_BANNER);
		}
		player.toPlayer().getInventory().clear();
		new BukkitRunnable() {
			int currentTime = player.getTime();
			@Override
			public void run() {
				player.toPlayer().sendTitle("", "Respawn in " + currentTime,5,10,5);
				if(currentTime == 0) {
					player.addTimer(5);
					Arena a = player.getArena();
					if(player.getTeam().equals("red")) {
						player.toPlayer().teleport(a.getLocations()[0]);
						player.getRespawnItems(Color.RED);
					}else {
						player.toPlayer().teleport(a.getLocations()[1]);
						player.getRespawnItems(Color.BLUE);
					}
					
					player.toPlayer().setGameMode(GameMode.SURVIVAL);
					player.toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEAL,100,100));
					cancel();
				}
				currentTime--;
			}
		}.runTaskTimer(this, 0, 20);
	}
	
	public ItemStack itemName(String name, Material mat) {
		ItemStack item = new ItemStack(mat);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		item.setItemMeta(im);
		return item;
	}
	
	public CTFPlayer getCTFPlayer(Player player) {
		for(CTFPlayer p : players) {
			if(player.equals(p.toPlayer())) {
				return p;
			}
		}
		return null;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		CTFPlayer player = getCTFPlayer(e.getPlayer());
		if(player != null) {
			Arena a = player.getArena();
			if(a != null) {
				if(e.getBlockPlaced().getLocation().equals(a.getLocations()[9])) {
					if(e.getBlockPlaced().getType().equals(Material.RED_BANNER)) {
						//score for blue
						a.getScoreBoard().addBluePoint(1);
						a.getLocations()[9].getBlock().setType(Material.AIR);
						a.getLocations()[2].getBlock().setType(Material.RED_BANNER);
						Bukkit.broadcastMessage(SERVER_TAG+ ChatColor.BLUE + " Blue" + ChatColor.LIGHT_PURPLE + " Team scored a point");
					}else {
						player.toPlayer().sendMessage(ChatColor.RED+"You can't place a block here");
						e.setCancelled(true);
					}
				}else if(e.getBlockPlaced().getLocation().equals(a.getLocations()[8])) {
					if(e.getBlockPlaced().getType().equals(Material.BLUE_BANNER)) {
						//score for red
						a.getScoreBoard().addRedPoint(1);
						a.getLocations()[8].getBlock().setType(Material.AIR);
						a.getLocations()[3].getBlock().setType(Material.BLUE_BANNER);
						Bukkit.broadcastMessage(SERVER_TAG+ ChatColor.RED + " Red" + ChatColor.LIGHT_PURPLE + " Team scored a point");
					}else {
						player.toPlayer().sendMessage(ChatColor.RED+"You can't place a block here");
						e.setCancelled(true);
					}
				}
				if(a.getScoreBoard().getBluePoints() >= 5) {
					//blue win
					Bukkit.broadcastMessage(SERVER_TAG+" "+ChatColor.BLUE+"Blue"+ ChatColor.LIGHT_PURPLE+" Team Wins!!!");
					endGame(a);
				}else if(a.getScoreBoard().getRedPoints() >= 5) {
					//red win
					Bukkit.broadcastMessage(SERVER_TAG+" "+ChatColor.RED+"Red"+ ChatColor.LIGHT_PURPLE+" Team Wins!!!");
					endGame(a);
				}
			}
		}
	}
	
	public void endGame(Arena a) {
		a.stopRelics();
		a.getScoreBoard().resetScores();
		a.started(false);
		for(CTFPlayer player : players) {
			if(player.getArena().equals(a)) {
				player.toPlayer().teleport(a.getLocations()[10]);
				player.toPlayer().getInventory().clear();
				player.toPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers();
				for(PotionEffect pe : player.toPlayer().getActivePotionEffects()){
					player.toPlayer().removePotionEffect(pe.getType());
				}
				player.resetTimer();
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e) {
		if(e.getItemDrop().getItemStack().getType().equals(Material.RED_BANNER)||e.getItemDrop().getItemStack().getType().equals(Material.BLUE_BANNER)) {
			e.getPlayer().sendMessage(ChatColor.RED+"You can't drop the flag");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		players.add(new CTFPlayer(player));
	}

	public String getHelpMenu() {
		StringBuilder help = new StringBuilder();
		help.append(ChatColor.GREEN + "Help Menu:\n");
		help.append(ChatColor.BLUE + "/ctf help\n");
		help.append(ChatColor.BLUE + "/ctf start <arena>\n");
		help.append(ChatColor.BLUE + "/ctf list <arena>\n");
		help.append(ChatColor.BLUE + "/ctf save <arena>\n");
		help.append(ChatColor.BLUE + "/ctf create <arena>\n");
		help.append(ChatColor.BLUE + "/ctf health <player>\n");
		help.append(ChatColor.BLUE + "/ctf relic <arena>\n");
		help.append(ChatColor.BLUE + "/ctf join <red|blue> <arena>\n");
		help.append(ChatColor.BLUE + "/ctf leave <red|blue> <arena>\n");
		help.append(ChatColor.BLUE + "/ctf set lobby <arena> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set point1 <arena> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set point2 <arena> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set relic <arena> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set spawn <arena> <red|blue> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set flag <arena> <red|blue> <x> <y> <z>\n");
		help.append(ChatColor.BLUE + "/ctf set return <arena> <red|blue> <x> <y> <z>");
		return help.toString();
	}

}
