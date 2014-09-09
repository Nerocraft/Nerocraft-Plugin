package main.nerocraft;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Statistics {
    public HashMap<String, Scoreboard> Scoreboard = new HashMap<String, Scoreboard>();
    public HashMap<String, Score> expscore = new HashMap<String, Score>();
    public HashMap<String, Score> archeryscore = new HashMap<String, Score>();
    public HashMap<String, Score> killsscore = new HashMap<String, Score>();
    public HashMap<String, Score> deathsscore = new HashMap<String, Score>();
    public HashMap<String, Score> creditsscore = new HashMap<String, Score>();
    public HashMap<String, Score> manascore = new HashMap<String, Score>();
    public HashMap<String, Score> energyscore = new HashMap<String, Score>();
    public Score exp, archery, kills, deaths, credits, mana, energy;

    public void create(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective statistics = board.registerNewObjective("statistics", "dummy");

        statistics.setDisplaySlot(DisplaySlot.SIDEBAR);
        statistics.setDisplayName(ChatColor.BOLD + "Statistics");

        exp = statistics.getScore(ChatColor.GREEN + "EXP");
        archery = statistics.getScore(ChatColor.GOLD + "Archery");
        kills = statistics.getScore(ChatColor.AQUA + "Kills");
        deaths = statistics.getScore(ChatColor.RED + "Deaths");
        credits = statistics.getScore(ChatColor.YELLOW + "Credits");
        mana = statistics.getScore(ChatColor.BLUE + "Mana");
        energy = statistics.getScore(ChatColor.DARK_RED + "Energy");

        exp.setScore(0);
        archery.setScore(0);
        kills.setScore(0);
        deaths.setScore(0);
        credits.setScore(0);
        mana.setScore(0);
        energy.setScore(0);

        expscore.put(player.getName(), exp);
        archeryscore.put(player.getName(), archery);
        killsscore.put(player.getName(), kills);
        deathsscore.put(player.getName(), deaths);
        creditsscore.put(player.getName(), credits);
        manascore.put(player.getName(), mana);
        energyscore.put(player.getName(), energy);

        Scoreboard.put(player.getName(), board);
    }

    public void update(Player player){
        Score exp = expscore.get(player.getName());
        Score archery = archeryscore.get(player.getName());
        Score kills = killsscore.get(player.getName());
        Score deaths = deathsscore.get(player.getName());
        Score credits = creditsscore.get(player.getName());
        Score mana = manascore.get(player.getName());
        Score energy = energyscore.get(player.getName());

        exp.setScore(player.getTotalExperience());
        archery.setScore(player.getArcheryExp());
        kills.setScore(player.getStatistic(Statistic.PLAYER_KILLS));
        deaths.setScore(player.getStatistic(Statistic.DEATHS));
        credits.setScore((int) Main.economy.getBalance(player.getName()));
        mana.setScore(player.getMana());
        energy.setScore(Math.round(player.getEnergy()));
    }

    public void set(Player player){
        player.setScoreboard(Scoreboard.get(player.getName()));
    }
}
