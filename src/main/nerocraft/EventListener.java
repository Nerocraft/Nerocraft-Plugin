// Copyright © Oren Iouchavaev (Neroren) - Nerocraft
package main.nerocraft;

import java.util.List;
import java.util.Random;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minion;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EventListener implements Listener {
    float randFloat;
    Random rand = new Random();
    Util util = new Util();
    Statistics stats = new Statistics();

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getPlayer().isInfected()) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are infected and cannot sleep.");
            event.setCancelled(true);
        } else if (event.getPlayer().getEnergy() >= 98) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "You are already well rested.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.GOLDEN_CARROT && player.isInfected()) {
            player.sendMessage(ChatColor.GREEN + "You have been cured from the infection.");
            DisguiseAPI.undisguiseToAll(player);
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer().isInfected()) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are infected and cannot fish.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (player.isInfected()) {
                player.sendMessage(ChatColor.RED + "You are infected and cannot fire a bow.");
                event.setCancelled(true);
            }
        }
    }

    // Temporary fix
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();
            Material itemtype = item.getType();
            Location loc = player.getLocation();

            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WOODEN_DOOR && player.isInfected()) {
                player.sendMessage(ChatColor.RED + "You are infected and cannot interact with doors.");
                event.setCancelled(true);
            }
            if (itemtype == Material.STICK) {
                Block block = player.getTargetBlock(null, 100);
                CreatureSpawner cs = (CreatureSpawner) block.getState();
                cs.setSpawnedType(EntityType.GOBLIN);
            }
            if (itemtype == Material.SLIME_BALL) {
                if (util.useMana(player, 500, 100)) {
                    util.decreaseStack(player, item);
                    List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            if (!(player.getVehicle() != null && player.getVehicle() == entity)) {
                                Vector vector = entity.getLocation().toVector().subtract(loc.toVector());
                                vector.normalize().multiply(2.5).add(new Vector(0D, 0.5D, 0D));
                                entity.setVelocity(vector);
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough mana to use this.");
                }
            }
            if (itemtype == Material.ENCHANTED_BOOK) {
                if (item.getDurability() == 1) {
                    if (util.useMana(player, 800, 300)) {
                        loc.getWorld().playSound(loc, Sound.PORTAL_TRAVEL, 1.0F, 1.0F);
                        for (int x = 0; x < 3; x++) {
                            Minion minion = (Minion) loc.getWorld().spawnEntity(loc, EntityType.MINION);
                            minion.setCustomName(player.getName());
                            minion.setCustomNameVisible(true);
                            minion.setOwnerUUID(player.getUniqueId().toString());
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have enough mana summon minions.");
                    }
                }
            }
            if (itemtype == Material.SUGAR) {
                util.decreaseStack(player, item);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1, true), true);
            }
            if (itemtype == Material.NETHER_STAR) {
                if (util.useMana(player, 700, 100)) {
                    player.getWorld().strikeLightning(player.getTargetBlock(null, 200).getLocation(), false, true);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough mana to strike lightning.");
                }
            }
            if (itemtype == Material.FIREBALL) {
                if (util.useMana(player, 250, 100)) {
                    util.decreaseStack(player, item);
                    Fireball ball = player.launchProjectile(Fireball.class);
                    if (item.getDurability() == 1) {
                        ball.setGuided(true);
                        ball.setYield(2);
                    } else {
                        player.setVelocity(loc.getDirection().multiply(-0.5).add(player.getVelocity()));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough mana to shoot the fireball.");
                }
            }
            if (itemtype == Material.GLOWSTONE_DUST && !((Entity) player).isOnGround()) {
                if (util.useMana(player, 600, 100)) {
                    util.decreaseStack(player, item);
                    if (player.getFireTicks() > 20) player.setFireTicks(20);
                    player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2.0F, 0.8F);
                    Vector playervel = new Vector(0, player.getVelocity().getY(), 0);
                    Vector vector = loc.getHorizontalDirection().multiply(3).add(playervel);
                    player.setVelocity(vector);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough mana to use this.");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.isInfected()) {
            player.getPlayer().sendMessage(ChatColor.RED + "You are infected and cannot interact with this " + event.getInventory().getType().toString().toLowerCase().replace("_", " ") + ".");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        if (event.getEntity().getKiller() != null && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
            Player killer = event.getEntity().getKiller();
            if (killer != null && !util.isNPC(killer)) {
                if (event.getEntity() instanceof Animals) {
                    util.changeBalance(killer, 1);
                    killer.giveExp(3);
                } else if (event.getEntity() instanceof Monster) {
                    killer.setMonsterKills(killer.getMonsterKills() + 1);
                    if (killer.getMonsterKills() % 10 == 0) {
                        killer.sendMessage(ChatColor.GREEN + "You have killed " + killer.getMonsterKills() + " monsters in this life. +10 EXP");
                        killer.giveExp(10);
                    }

                    if (event.getEntityType() == EntityType.GIANT) {
                        util.changeBalance(killer, 25);
                        killer.giveExp(25);
                    } else {
                        util.changeBalance(killer, 2);
                    }
                    if (event.getEntityType() == EntityType.ZOMBIE) {
                        Zombie zombie = (Zombie) event.getEntity();
                        if (!zombie.isBaby() && !zombie.isVillager()) killer.giveExp(5);
                        if (zombie.isVillager()) killer.giveExp(6);
                        if (zombie.isBaby()) killer.giveExp(7);
                    }
                    if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
                        PigZombie pz = (PigZombie) event.getEntity();
                        if (pz.isBaby()) killer.giveExp(4);
                        else killer.giveExp(3);
                    }
                    if (event.getEntityType() == EntityType.SKELETON) {
                        Skeleton skel = (Skeleton) event.getEntity();
                        if (skel.getSkeletonType() == SkeletonType.WITHER) killer.giveExp(10);
                        else killer.giveExp(6);
                    }
                } else if (event.getEntityType() == EntityType.PLAYER) {
                    Player victim = (Player) event.getEntity();
                    if (!util.isNPC(victim)) {
                        int amount = (int) util.changeBalance(victim, -20);
                        util.changeBalance(killer, amount);
                        if (amount > 0) {
                            killer.sendMessage(ChatColor.BLUE + "Looted " + ChatColor.GREEN + Integer.toString(amount) + " Credits" + ChatColor.BLUE + " from " + victim.getName() + ".");
                        } else {
                            killer.sendMessage(ChatColor.BLUE + "Found no credits on player's body.");
                        }
                    }
                }
            }
        } else if (event.getEntityType() == EntityType.PLAYER) {
            Player victim = (Player) event.getEntity();
            if (!util.isNPC(victim)) {
                util.changeBalance(victim, -20);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().setIngame(false);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() != null && event.getTarget().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getTarget();
            if (!player.isIngame() || (player.isInfected() && event.getReason() != TargetReason.TARGET_ATTACKED_ENTITY)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        stats.create(event.getPlayer());
        if (event.getPlayer().isInfected()) {
            util.disguise(event.getPlayer(), DisguiseType.ZOMBIE, true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();
        // Temporary fix
        if (victim.getType() == EntityType.ITEM_FRAME) {
            if (damager.getType() == EntityType.PLAYER) {
                if (!((Player) damager).isOp()) {
                    event.setCancelled(true);
                    return;
                }
            } else {
                event.setCancelled(true);
                return;
            }
        }
        if (damager.getType() == EntityType.PLAYER) {
            Player player = (Player) damager;
            if (!player.isIngame()) {
                event.setCancelled(true);
                return;
            }
            if (player.isInfected() && victim instanceof LivingEntity) {
                if (victim.getType() == EntityType.ZOMBIE) {
                    event.setCancelled(true);
                    return;
                } else if (victim.getType() == EntityType.PLAYER && ((Player) victim).isInfected()) {
                    event.setCancelled(true);
                    return;
                } else {
                    if (victim.getType() != EntityType.SKELETON) {
                        double finaldamage = event.getFinalDamage();
                        if (finaldamage > ((LivingEntity) victim).getHealth()) {
                            finaldamage = ((LivingEntity) victim).getHealth();
                        }
                        double finalhealth = player.getHealth() + (finaldamage / 8);
                        if (finalhealth > player.getMaxHealth()) {
                            finalhealth = player.getMaxHealth();
                        }
                        player.setHealth(finalhealth);
                    }
                }
            }
        } else if (damager.getType() == EntityType.ZOMBIE) {
            if (victim.getType() == EntityType.PLAYER && ((Player) victim).isInfected()) {
                event.setCancelled(true);
                return;
            }
        }
        if (victim.getType() == EntityType.PLAYER) {
            Player player = (Player) victim;
            if (!player.isIngame()) {
                event.setCancelled(true);
                return;
            }
            if (player.getHealth() - event.getDamage() <= 0.0D && !player.isInfected() && !util.isNPC(player) && (damager.getType() == EntityType.ZOMBIE || (damager.getType() == EntityType.PLAYER && ((Player) damager).isInfected()))) {
                event.setDamage(0);
                player.setInfected(true);
                player.setHealth(player.getMaxHealth());
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2, true), true);
                player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 2, 1);
                player.sendMessage(ChatColor.RED + "You have been infected and are now a zombie.");
                util.disguise(victim, DisguiseType.ZOMBIE, true);
            } else if (player.isInfected() && !DisguiseAPI.isDisguised(player)) {
                util.disguise(victim, DisguiseType.ZOMBIE, true);
            }
        }
        if (event.getCause() == DamageCause.PROJECTILE) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getType() == EntityType.ARROW && proj.getShooter() instanceof Player && proj.getLocation().getY() - victim.getLocation().getY() >= 1.5D) {
                if (victim.getType() == EntityType.ZOMBIE) event.setDamage(event.getDamage() * 100);
                if (victim.getType() == EntityType.SKELETON
                 || victim.getType() == EntityType.PLAYER
                 || victim.getType() == EntityType.PIG_ZOMBIE) event.setDamage(event.getDamage() * 2);
            }
        }
    }

    public void randFloat() {
        randFloat = rand.nextFloat();
    }
}