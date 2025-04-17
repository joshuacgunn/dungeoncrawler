package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.systems.PlayerStats;

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

    public enum PlayerClass {
        ROGUE,
        WIZARD,
        PALADIN;
    }

    private PlayerStats playerStats;

    private final PlayerClass playerClass;

    /**
     * Creates a new player with the specified name and UUID.
     * <p>
     * Initializes the player with maximum health and creates a new inventory.
     *
     * @param name The player's name
     * @param uuid The unique identifier for the player
     */
    public Player(String name, UUID uuid, PlayerClass playerClass) {
        super(name, uuid);
        this.entityHp = MAX_HP;
        this.playerClass = playerClass;
        this.playerStats = initializeStats();
    }

    /**
     * Gets the weapon the player currently has equipped.
     *
     * @return The currently equipped weapon, or null if no weapon is equipped
     */
    public Weapon getCurrentWeapon() {
        return this.currentWeapon;
    }

    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public PlayerClass getPlayerClass() {
        return this.playerClass;
    }

    /**
     * Equips a weapon for the player to use.
     *
     * @param weapon The weapon to equip
     */
    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    public void equipWeapon(Weapon weapon) {
        if (currentWeapon != null) {
            this.currentWeapon = null;
        }
        this.currentWeapon = weapon;
    }

    public void unEquipWeapon() {
        if (!(this.currentWeapon == null)) {
            this.currentWeapon = null;
        }

    }

    public float calculateWeaponDamage() {
        float baseDamage = currentWeapon.getWeaponDamage();

        float strengthBonus = playerStats.getStrength() * 0.1f;

        float classBonus = 0f;

        switch(playerClass) {
            case PALADIN:
                classBonus = playerStats.getStrength() * 0.03f;
                break;
            case WIZARD:
                classBonus = playerStats.getIntelligence() * 0.05f;
                break;
            case ROGUE:
                classBonus = playerStats.getDexterity() * 0.05f;
                break;
        }
        return baseDamage * (1f + strengthBonus + classBonus);
    }

    private PlayerStats initializeStats() {
        PlayerStats stats = new PlayerStats();

        switch(playerClass) {
            case ROGUE:
                stats.setDexterity(12);
                stats.setStrength(4);
                stats.setCharisma(2);
                stats.setLuck(8);
                stats.setIntelligence(5);
                stats.setVitality(4);
                break;
            case WIZARD:
                stats.setDexterity(4);
                stats.setStrength(2);
                stats.setCharisma(6);
                stats.setLuck(3);
                stats.setIntelligence(11);
                stats.setVitality(6);
                break;
            case PALADIN:
                stats.setDexterity(1);
                stats.setStrength(14);
                stats.setCharisma(9);
                stats.setLuck(2);
                stats.setIntelligence(3);
                stats.setVitality(8);
                break;
            case null, default:
                stats.setDexterity(0);
                stats.setStrength(0);
                stats.setCharisma(0);
                stats.setLuck(0);
                stats.setIntelligence(0);
                break;
        }
        return stats;
    }

    public String getPlayerStatsString() {
        return String.format(
                "Player Stats for %s (%s):\n" +
                        "HP: %.1f/%.1f\n" +
                        "Class: %s\n" +
                        "Strength: %d\n" +
                        "Dexterity: %d\n" +
                        "Vitality: %d\n" +
                        "Intelligence: %d\n" +
                        "Luck: %d\n" +
                        "Charisma: %d\n" +
                        "Current Weapon: %s",
                getEntityName(), getEntityUUID(),
                entityHp, MAX_HP,
                playerClass,
                this.playerStats.getStrength(),
                this.playerStats.getDexterity(),
                this.playerStats.getVitality(),
                this.playerStats.getIntelligence(),
                this.playerStats.getLuck(),
                this.playerStats.getCharisma(),
                currentWeapon != null ? currentWeapon.getItemName() : "None"
        );
    }


}