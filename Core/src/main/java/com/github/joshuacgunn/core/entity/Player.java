package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.gameplay.GameState;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Potion;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.mechanics.EntityStats;

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

    /**
     * Possible player character classes that determine initial stats and abilities.
     */
    public enum PlayerClass {
        /** Specializes in stealth and agility */
        ROGUE,
        /** Masters of arcane magic */
        WIZARD,
        /** Holy warrior combining combat and divine powers */
        PALADIN
    }


    private final PlayerClass playerClass;
    private String gameStateName;
    private String previousGameStateName;
    private GameState gameState;
    private GameState previousGameState;
    private int playerLevel;

    /**
     * Creates a new player with the specified name and UUID.
     * <p>
     * Initializes the player with maximum health and creates a new inventory.
     *
     * @param name The player's name
     * @param uuid The unique identifier for the player
     */
    public Player(String name, UUID uuid, PlayerClass playerClass, boolean isNew) {
        super(name, uuid);
        this.playerClass = playerClass;
        if (isNew) {
            generateStartingEquipment(this);
            this.entityStats = initializeStats();
            this.entityHp = MAX_HP;
            this.playerLevel = 0;
        }
    }

    /**
     * Gets the weapon the player currently has equipped.
     *
     * @return The currently equipped weapon, or null if no weapon is equipped
     */
    public Weapon getCurrentWeapon() {
        return this.currentWeapon;
    }

    public int getPlayerLevel() {
        return this.playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getGameStateName() {
        return this.gameStateName;
    }

    public void setGameStateName(String gameState) {
        this.gameStateName = gameState;
    }

    public String getPreviousGameStateName() {
        return previousGameStateName;
    }

    public void setPreviousGameStateName(String previousGameStateName) {
        this.previousGameStateName = previousGameStateName;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.gameStateName = gameState.getClass().getSimpleName();
    }

    public GameState getPreviousGameState() {
        return this.previousGameState;
    }

    public void setPreviousGameState(GameState previousGameState) {
        this.previousGameState = previousGameState;
        this.previousGameStateName = previousGameState.getClass().getSimpleName();
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

        float strengthBonus = entityStats.getStatValue(EntityStats.Stat.STRENGTH) * 0.1f;

        float classBonus = 0f;

        switch(playerClass) {
            case PALADIN:
                classBonus = entityStats.getStatValue(EntityStats.Stat.STRENGTH) * 0.03f;
                break;
            case WIZARD:
                classBonus = entityStats.getStatValue(EntityStats.Stat.INTELLIGENCE) * 0.05f;
                break;
            case ROGUE:
                classBonus = entityStats.getStatValue(EntityStats.Stat.DEXTERITY) * 0.05f;
                break;
        }
        return baseDamage * (1f + strengthBonus + classBonus);
    }

    public void usePotion(Potion potion) {
        switch (potion.getPotionType()) {
            case HEALING:
                System.out.println("Healing potion used!");
                break;
            case MANA:
                System.out.println("Mana restoration potion used!");
                break;
            case ALCOHOL:
                System.out.println("Alcohol used!");
                break;
        }
    }

    /**
     * Initializes the player's stats based on their chosen class.
     * The player's attributes such as dexterity, strength, charisma, luck, intelligence,
     * and vitality are set according to predefined values for their class type.
     * If the player's class is null or unrecognized, default stats are set to zero.
     *
     * @return A PlayerStats object containing the initialized attributes for the player.
     */
    private EntityStats initializeStats() {
        EntityStats stats = new EntityStats();

        switch(playerClass) {
            case ROGUE:
                stats.setStatValue(EntityStats.Stat.DEXTERITY, 12);
                stats.setStatValue(EntityStats.Stat.STRENGTH, 4);
                stats.setStatValue(EntityStats.Stat.CHARISMA, 2);
                stats.setStatValue(EntityStats.Stat.LUCK, 8);
                stats.setStatValue(EntityStats.Stat.INTELLIGENCE, 5);
                stats.setStatValue(EntityStats.Stat.VITALITY, 4);
                break;
            case WIZARD:
                stats.setStatValue(EntityStats.Stat.DEXTERITY, 4);
                stats.setStatValue(EntityStats.Stat.STRENGTH, 2);
                stats.setStatValue(EntityStats.Stat.CHARISMA, 6);
                stats.setStatValue(EntityStats.Stat.LUCK, 6);
                stats.setStatValue(EntityStats.Stat.INTELLIGENCE, 11);
                stats.setStatValue(EntityStats.Stat.VITALITY, 5);
                break;
            case PALADIN:
                stats.setStatValue(EntityStats.Stat.DEXTERITY, 1);
                stats.setStatValue(EntityStats.Stat.STRENGTH, 14);
                stats.setStatValue(EntityStats.Stat.CHARISMA, 9);
                stats.setStatValue(EntityStats.Stat.LUCK, 2);
                stats.setStatValue(EntityStats.Stat.INTELLIGENCE, 3);
                stats.setStatValue(EntityStats.Stat.VITALITY, 8);
                break;
            case null, default:
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
                this.entityStats.getStatValue(EntityStats.Stat.STRENGTH),
                this.entityStats.getStatValue(EntityStats.Stat.DEXTERITY),
                this.entityStats.getStatValue(EntityStats.Stat.VITALITY),
                this.entityStats.getStatValue(EntityStats.Stat.INTELLIGENCE),
                this.entityStats.getStatValue(EntityStats.Stat.LUCK),
                this.entityStats.getStatValue(EntityStats.Stat.CHARISMA),
                currentWeapon != null ? currentWeapon.getItemName() : "None"
        );
    }



    public static void generateStartingEquipment(Player player) {
        PlayerClass playerClass = player.getPlayerClass();
        Weapon weapon;
        Armor helmet;
        Armor chestplate;
        Armor leggings;
        Armor boots;
        weapon = new Weapon("Basic sword", UUID.randomUUID(), Item.ItemRarity.COMMON, true);
        helmet = new Armor(UUID.randomUUID(), Armor.ArmorSlot.HELMET, "Basic helmet", Item.ItemRarity.COMMON, true);
        chestplate = new Armor(UUID.randomUUID(), Armor.ArmorSlot.CHESTPLATE, "Basic chestplate", Item.ItemRarity.COMMON, true);
        leggings = new Armor(UUID.randomUUID(), Armor.ArmorSlot.LEGGINGS, "Basic leggings", Item.ItemRarity.COMMON, true);
        boots = new Armor(UUID.randomUUID(), Armor.ArmorSlot.BOOTS, "Basic boots", Item.ItemRarity.COMMON, true);
        player.equipArmor(helmet);
        player.equipArmor(chestplate);
        player.equipArmor(leggings);
        player.equipArmor(boots);
        player.setCurrentWeapon(weapon);
    }


}