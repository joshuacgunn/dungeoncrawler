package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Weapon;

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

    /** The player's inventory containing all collected items */

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
    }

    /**
     * Gets the player's inventory.
     *
     * @return The player's inventory containing all collected items
     */

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