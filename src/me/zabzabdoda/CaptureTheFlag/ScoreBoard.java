package me.zabzabdoda.CaptureTheFlag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreBoard {

	private Scoreboard board;
	private ArrayList<Player> redTeam;
	private ArrayList<Player> blueTeam;
	private Objective sidebar;

	
    public ScoreBoard() {
    	redTeam = new ArrayList<Player>();
    	blueTeam = new ArrayList<Player>();
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("teams", "dummy", "teams");
        
        sidebar = board.registerNewObjective("CTF", "dummy",ChatColor.GREEN + "Capture The Flag");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        teamSetup(obj);
        setupSideBar(sidebar);
    }
    
    public void setupSideBar(Objective obj) {
    	Team blueTeam = board.registerNewTeam("BlueTeam");
    	blueTeam.addEntry(ChatColor.BLUE + "");
        Score blueScore = obj.getScore(ChatColor.BLUE + "");
        blueScore.setScore(0);
        
    	Team redTeam = board.registerNewTeam("RedTeam");
    	redTeam.addEntry(ChatColor.RED + "");
        Score redScore = obj.getScore(ChatColor.RED + "");
        redScore.setScore(0);
        
    	board.getTeam("BlueTeam").setPrefix(ChatColor.BLUE + "Blue Team: ");
    	board.getTeam("RedTeam").setPrefix(ChatColor.RED + "Red Team: ");

    }
    
    public void joinScoreBoard(Player player) {
    	player.setScoreboard(board);
    }
    
    public void teamSetup(Objective obj) {
        Team redTeam = board.registerNewTeam("Red");
        redTeam.setColor(ChatColor.RED);
        redTeam.setPrefix("[Red] ");
        Score onlineName = obj.getScore(ChatColor.GRAY + "Red Team");
        redTeam.setAllowFriendlyFire(false);
        onlineName.setScore(0);
        
        Team blueTeam = board.registerNewTeam("Blue");
        blueTeam.setColor(ChatColor.DARK_BLUE);
        blueTeam.setPrefix("[Blue] ");
        Score blueScore = obj.getScore(ChatColor.GRAY + "Blue Team");
        blueTeam.setAllowFriendlyFire(false);
        blueScore.setScore(0);
       
    }
    
    public void addBluePoint(int point) {
    	Score s = sidebar.getScore(ChatColor.BLUE + "");
    	s.setScore(point + s.getScore());
    }
    
    public void addRedPoint(int point) {
    	Score s = sidebar.getScore(ChatColor.RED + "");
    	s.setScore(point + s.getScore());
    }
    
    public int getRedPoints() {
    	Score s = sidebar.getScore(ChatColor.RED + "");
    	return s.getScore();
    }
    
    public int getBluePoints() {
    	Score s = sidebar.getScore(ChatColor.BLUE + "");
    	return s.getScore();
    }
    
    public void resetScores() {
    	Score s1 = sidebar.getScore(ChatColor.BLUE + "");
    	Score s2 = sidebar.getScore(ChatColor.RED + "");
    	s1.setScore(0);
    	s2.setScore(0);
    }
    
    public void joinRed(Player player) {
    	player.setScoreboard(board);
    	board.getTeam("Red").addPlayer(player);
    	redTeam.add(player);
    	blueTeam.remove(player);
    }
    
    public void joinBlue(Player player) {
    	player.setScoreboard(board);
    	board.getTeam("Blue").addPlayer(player);
    	blueTeam.add(player);
    	redTeam.remove(player);
    }
 
    public void leaveTeam(Player player, Team team) {
    	Scoreboard board = player.getScoreboard();
    	if(team != null) {
    		team.removePlayer(player);
    	}
    }
    
    public boolean getifonred(Player player) {
    	for(Player p : redTeam) {
    		if(player.equals(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean getifonblue(Player player) {
    	for(Player p : blueTeam) {
    		if(player.equals(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
}
