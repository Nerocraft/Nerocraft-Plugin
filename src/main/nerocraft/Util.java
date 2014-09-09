package main.nerocraft;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {
    /**
     * Disguise a player as another player.
     *
     * @param player the player to disguise
     * @param name the playername to disguise as
     */
    public void disguiseAsPlayer(Player player, String name) {
        PlayerDisguise disguise = new PlayerDisguise(name);
//        PlayerWatcher watcher = (PlayerWatcher) disguise.getWatcher();
//        watcher.setSkin(name);
        disguise.setKeepDisguiseOnPlayerDeath(true);
        DisguiseAPI.disguiseToAll(player, disguise);
    }

    /**
     * Change a player's balance by a specified amount. Can be positive to
     * increase and negative to decrease.
     *
     * @param player the player
     * @param amount the amount
     * @return the exact amount that was given (positive) or taken
     * (negative) from the player
     */
    public double changeBalance(Player player, int amount) {
        String playerName = player.getName();
        double damount = amount;
        if (damount > 0) {
            Main.economy.depositPlayer(playerName, damount);
        } else {
            double absamount = -damount;
            if (Main.economy.has(playerName, absamount)) {
                Main.economy.withdrawPlayer(playerName, absamount);
                return damount;
            } else {
                Main.economy.withdrawPlayer(playerName, Main.economy.getBalance(playerName));
                return Main.economy.getBalance(playerName);
            }
        }
        return 0;
    }

    /**
     * Decrease an itemstack by one.
     *
     * @param player the player to decrease the itemstack from
     * @param item the itemstack
     */
    public void decreaseStack(Player player, ItemStack item) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.setItemInHand(null);
            }
        }
    }

    /**
     * Disguise a living entity with another entity.
     *
     * @param entity the entity to disguise
     * @param type the DisguiseType to disguise to
     * @param showName if the living entity is a player, should the name be
     * shown onto the disguised entity?
     */
    public void disguise(Entity entity, DisguiseType type, boolean showName) {
        MobDisguise disguise = new MobDisguise(type);
        if (showName) {
            FlagWatcher watcher = disguise.getWatcher();
            ((LivingWatcher) watcher).setCustomName(((Player) entity).getName());
            ((LivingWatcher) watcher).setCustomNameVisible(true);
        }
        DisguiseAPI.disguiseToAll(entity, disguise);
    }

    /**
     * Uses the specified amount of mana for the player if they are not in
     * creative mode.
     *
     * @param player player to take mana from
     * @param mana exact amount of mana
     * @param cooldown mana cooldown to apply in ticks, default is 100
     * @return true if successful, otherwise false
     */
    public boolean useMana(Player player, int mana, int cooldown) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        } else if (player.getMana() >= mana) {
            player.setMana(player.getMana() - mana);
            if (cooldown > player.getManaCooldown()) {
                player.setManaCooldown(cooldown);
            }
            return true;
        }
        return false;
    }

    /**
     * Gets whether or not this player is an NPC
     *
     * @param player the player
     * @return true if player is an NPC, otherwise false
     */
    public boolean isNPC(Player player) {
        return player.hasMetadata("NPC");
    }
}
