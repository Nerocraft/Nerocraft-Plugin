package main.nerocraft;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
    public static Economy economy = null;
    Random rand = new Random();
    Util util = new Util();
    Statistics stats = new Statistics();

    public void setupScoreboard() {
        new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!util.isNPC(player)) {
                        if (stats.Scoreboard.get(player.getName()) != null) {
                            stats.update(player);
                            stats.set(player);
                        } else {
                            stats.create(player);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 10L);
    }

    @Override
    public void onEnable() {
        if (setupEconomy()) {
            getLogger().info("Linked with Economy");
        } else {
            getLogger().warning("Failed to link with Economy");
        }
        WorldCreator world = new WorldCreator("spawn");
        world.generateStructures(false);
        Bukkit.getServer().createWorld(world);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        setupScoreboard();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("restoremana") && sender.isOp()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setMana(1000);
            }
        }
        if (cmd.getName().equalsIgnoreCase("restoreenergy") && sender.isOp()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setEnergy(100.0F);
            }
        }
        if (cmd.getName().equalsIgnoreCase("setingame") && sender.isOp()) {
            if (sender instanceof Player && args.length == 1) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("true")) {
                    player.setIngame(true);
                } else if (args[0].equalsIgnoreCase("false")) {
                    player.setIngame(false);
                }
            } else if (args.length == 2) {
                Player player = getServer().getPlayer(args[1]);
                if (player != null) {
                    if (args[0].equalsIgnoreCase("true")) {
                        player.setIngame(true);
                    } else if (args[0].equalsIgnoreCase("false")) {
                        player.setIngame(false);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is not currently online.");
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("start")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isIngame() || player.getGameMode() == GameMode.CREATIVE) {
                    Server server = Bukkit.getServer();
                    player.getInventory().clear();
                    player.setGameMode(GameMode.ADVENTURE);
                    server.dispatchCommand(server.getConsoleSender(), "warp spawn_" + (rand.nextInt(9) + 1) + " " + player.getName());
                    server.dispatchCommand(server.getConsoleSender(), "kit kit_" + (rand.nextInt(3) + 1) + " " + player.getName());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3, true), true);
                    player.setHealth(player.getMaxHealth());
                    player.setSaturation(10.0F);
                    player.setExhaustion(0.0F);
                    player.setFoodLevel(20);
                    player.setEnergy(100.0F);
                    player.setIngame(true);
                    player.setInfected(false);
                } else {
                    player.sendMessage(ChatColor.RED + "You are already ingame.");
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("explosive") && sender.isOp()) {
            if (args.length == 1) {
                Player player = getServer().getPlayer(args[0]);
                if (player != null) {
                    player.setExplosive(!player.isExplosive());
                    player.sendMessage("You are " + (player.isExplosive() ? "now explosive." : "no longer explosive."));
                    if (sender instanceof Player) {
                        if (((Player) sender).getName() != player.getName()) {
                            sender.sendMessage(player.getName() + " is " + (player.isExplosive() ? "now explosive." : "no longer explosive."));
                        }
                    } else {
                        sender.sendMessage(player.getName() + " is " + (player.isExplosive() ? "now explosive." : "no longer explosive."));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is not currently online.");
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setExplosive(!player.isExplosive());
                player.sendMessage("You are " + (player.isExplosive() ? "now explosive." : "no longer explosive."));
            }
        }
        return true; 
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}