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

//    @EventHandler
//    public void onCreatureSpawn(CreatureSpawnEvent event) {
//        if (event.isCancelled()) return;
//        if (event.getEntityType() == EntityType.ZOMBIE) {
//            Zombie zombie = (Zombie)event.getEntity();
//            EntityEquipment zombieInv = zombie.getEquipment();
            // Experiment - Turn Zombies in sky biome into Giants
//            if (zombie.getWorld().getBiome((int)zombie.getLocation().getX(), (int)zombie.getLocation().getZ()) == Biome.SKY) {
//                zombie.getWorld().spawnEntity(zombie.getLocation(), EntityType.GIANT);
//                zombie.remove();
//            } else if (rand.nextFloat() <= 0.01F && !zombie.isVillager() && !zombie.isBaby()) zombieInv.setHelmet(new ItemStack(Material.PUMPKIN));
//        }
//        if (event.getEntityType() == EntityType.SKELETON) {
//            Skeleton skel = (Skeleton)event.getEntity();
            /**			if (rand.nextFloat() <= 0.1F) skel.setSkeletonType(SkeletonType.WITHER); */
//            EntityEquipment inv = skel.getEquipment();
            /**			if (rand.nextFloat() >= 0.75F && skel.getSkeletonType() == SkeletonType.NORMAL) Inv.setHelmet(new ItemStack(Material.LEATHER_HELMET));
			if (rand.nextFloat() >= 0.5F && skel.getSkeletonType() == SkeletonType.NORMAL) Inv.setChestplate(new ItemStack(Material.BRONZE_CHESTPLATE));
			if (rand.nextFloat() >= 0.5F && skel.getSkeletonType() == SkeletonType.NORMAL) Inv.setLeggings(new ItemStack(Material.BRONZE_LEGGINGS));
			if (rand.nextFloat() >= 0.75F && skel.getSkeletonType() == SkeletonType.NORMAL) Inv.setBoots(new ItemStack(Material.BRONZE_BOOTS));*/
//            inv.setItemInHandDropChance((float) 0.05);
//            inv.setHelmetDropChance((float) 1);
//            inv.setChestplateDropChance((float) 0.5);
//            inv.setLeggingsDropChance((float) 0.5);
//            inv.setBootsDropChance((float) 1);
//        }
        // Nerocraft - Everything ported to main code, unneeded
        //		if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
        //			PigZombie pz = (PigZombie)event.getEntity();
        //			if (rand.nextFloat() <= 0.2F) {
        //				pz.setBaby(true);
        //				pz.setMaxHealth(15);
        //				pz.setHealth(15);
        //			}
        //			pz.setAngry(true);
        //		}
//    }
/*
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        Material drop = event.getItemDrop().getItemStack().getType();
        if (drop == Material.WOOD_PLATE
                || drop == Material.STONE_PLATE
                || drop == Material.CARROT
                || drop == Material.POTATO) event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.CLICK, 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.8F);
        else event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.25F);
    }
*/

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
//            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2, true), true); // Moved
//            player.setInfected(false); // Moved to internal server code for better priority
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

//    public List<Location> getSphere(Location centerBlock, int radius) {
//        List<Location> circleBlocks = new ArrayList<Location>();
//        int bX = centerBlock.getBlockX();
//        int bY = centerBlock.getBlockY();
//        int bZ = centerBlock.getBlockZ();
//
//        for (int x = bX - radius; x <= bX + radius; x++) {
//            for (int y = bY - radius; y <= bY + radius; y++) {
//                for (int z = bZ - radius; z <= bZ + radius; z++) {
//                    double distance = ((bX - x) * (bX - x)) + ((bY - y) * (bY - y)) + ((bZ - z) * (bZ - z));
//                    if (distance < (radius * radius)) {
//                        Location l = new Location(centerBlock.getWorld(), x, y, z);
//                        circleBlocks.add(l);
//                    }
//                }
//            }
//        }
//
//        return circleBlocks;
//    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }/* else if (!player.isIngame()) {
            event.setCancelled(true);
            return;
        }*/
//
//        Material item = player.getItemInHand().getType();
//        Block block = event.getBlock();
//        Material blocktype = block.getType();
//        if (blocktype == Material.CROPS || blocktype == Material.POTATO || blocktype == Material.CARROT) {
//            if (item == Material.WOOD_HOE || item == Material.STONE_HOE || item == Material.IRON_HOE || item == Material.GOLD_HOE) {
//                if (block.getData() == CropState.RIPE.getData()) {
//                    if (!player.isInfected()) {
//                        return;
//                    } else {
//                        player.sendMessage(ChatColor.RED + "You are infected and cannot cut down crops.");
//                    }
//                } else {
//                    player.sendMessage("Not fully grown");
//                }
//            }
//        }
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

//            player.sendMessage(Double.toString(Math.round(Math.log10(player.getArcheryExp()))));
//            player.sendMessage(Double.toString(Math.floor(Math.log10(player.getArcheryExp()))));
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
//                    player.launchProjectile(Fireball.class);
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
/*
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.isCancelled()) return;
        if (event.getItem().getItemStack().getType() == Material.WOOD_PLATE) {	// Mine v1
            event.setCancelled(true);
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.getItem().remove();
            Player player = event.getPlayer();
            int amount = event.getItem().getItemStack().getAmount();
            if (amount > 3) amount = 3;
            player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), amount, false, false);
        }
        if (event.getItem().getItemStack().getType() == Material.STONE_PLATE) {	// Mine v2
            event.setCancelled(true);
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.getItem().remove();
            Player player = event.getPlayer();
            int amount = event.getItem().getItemStack().getAmount();
            if (amount > 3) amount = 3;
            player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), amount, false, false);
            player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), amount + 2, false, false);
        }
        if (event.getItem().getItemStack().getType() == Material.CARROT) {	// Caltrops v1
            event.setCancelled(true);
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.getItem().remove();
            Player player = event.getPlayer();
            int amount = event.getItem().getItemStack().getAmount();
            int amplifier = 0;
            if (amount == 1) amplifier = 0;
            else if (amount == 3) amplifier = 1;
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 0, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300 * (amplifier + 1), amplifier, false));
        }
        if (event.getItem().getItemStack().getType() == Material.POTATO) {	// Caltrops v2
            event.setCancelled(true);
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.getItem().remove();
            Player player = event.getPlayer();
            int amount = event.getItem().getItemStack().getAmount();
            int amplifier = 0;
            if (amount == 1) amplifier = 0;
            else if (amount == 2) amplifier = 1;
            else if (amount == 3) amplifier = 2;
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 0, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300 * (amplifier + 1), amplifier + 1, false));
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {	//Prevent despawning of items
        Material item = event.getEntity().getItemStack().getType();
        if (item == Material.STONE_PLATE) event.setCancelled(true);
        if (item == Material.CARROT) event.setCancelled(true);
        if (item == Material.POTATO) event.setCancelled(true);
    }
*/
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
//        Location loc = event.getEntity().getLocation();
//        World world = loc.getWorld();
//        Zombie zombie = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
//        zombie.setBaby(false);
//        zombie.setVillager(false);
//        EntityEquipment Inv = zombie.getEquipment();
//        Inv.setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
//        ItemStack helmet = Inv.getHelmet();
//        SkullMeta meta = (SkullMeta)helmet.getItemMeta();
//        meta.setOwner(event.getEntity().getName());
//        helmet.setItemMeta(meta);
//        Inv.setHelmet(helmet);
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
        //		Bukkit.getPlayer("Neroren").sendMessage(String.valueOf(proj.getLocation().getY()) + "\n" + String.valueOf(victim.getLocation().getY()) + "\n" + String.valueOf(proj.getLocation().getY() - event.getEntity().getLocation().getY()));
    }

    /*  @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event) {
		if (!event.isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.IRONGOLEM_THROW, 10, 0);
			event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(0,0.75,0)));
			event.setCancelled(true);
		}
	}*/

    public void randFloat() {
        randFloat = rand.nextFloat();
    }
}