package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;

import java.util.UUID;

/**
 * Represents a player character in the game.
 * <p>
 * The Player class extends Entity and includes additional functionalities specific to
 * players such as inventory management, equipped weapon, and dungeon navigation.
 * Each player has a maximum health value, can equip weapons, and maintain an inventory
 * of items collected throughout gameplay.
 */
public class Player extends Entity {
    /** The maximum health points a player can have */
    public static final float MAX_HP = 100f;

    /** The dungeon the player is currently in */
    private Dungeon currentDungeon;

    /** The player's inventory containing all collected items */
    private final Inventory inventory;

    /**
     * Creates a new player with the specified name and UUID.
     * <p>
     * Initializes the player with maximum health and creates a new inventory.
     *
     * @param name The player's name
     * @param uuid The unique identifier for the player
     */
    public Player(String name, UUID uuid) {
        super(name, uuid);
        this.entityHp = MAX_HP;
        inventory = new Inventory(UUID.randomUUID(), this);
    }

    /**
     * Gets the dungeon the player is currently in.
     *
     * @return The current dungeon, or null if the player is not in a dungeon
     */
    public Dungeon getCurrentDungeon() {
        return currentDungeon;
    }

    /**
     * Sets the player's current dungeon.
     *
     * @param currentDungeon The dungeon to place the player in
     */
    public void setCurrentDungeon(Dungeon currentDungeon) {
        this.currentDungeon = currentDungeon;
    }

    /**
     * Gets the player's inventory.
     *
     * @return The player's inventory containing all collected items
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the weapon the player currently has equipped.
     *
     * @return The currently equipped weapon, or null if no weapon is equipped
     */
    public Weapon getCurrentWeapon() {
        return this.currentWeapon;
    }

    /**
     * Equips a weapon for the player to use.
     *
     * @param weapon The weapon to equip
     */
    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }
}