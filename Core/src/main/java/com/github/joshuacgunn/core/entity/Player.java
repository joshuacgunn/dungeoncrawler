package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.gameplay.GameState;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.mechanics.PlayerStats;

import java.util.UUID;
import java.util.Scanner;

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


    private PlayerStats playerStats;
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
            this.playerStats = initializeStats();
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

    /**
     * Initializes the player's stats based on their chosen class.
     * The player's attributes such as dexterity, strength, charisma, luck, intelligence,
     * and vitality are set according to predefined values for their class type.
     * If the player's class is null or unrecognized, default stats are set to zero.
     *
     * @return A PlayerStats object containing the initialized attributes for the player.
     */
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

    /**
     * Creates a new player character with user input for name and class selection.
     * This method handles the initial player creation process including:
     * - Getting the player's name
     * - Class selection from available options
     * - Generating a unique identifier
     * - Setting up initial player stats based on chosen class
     *
     * @return A newly created Player instance with the chosen attributes
     */
    public static Player createPlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        System.out.println("What class would you like to play as?");
        System.out.println("1. Rogue");
        System.out.println("2. Wizard");
        System.out.println("3. Paladin");
        int playerClass = scanner.nextInt();
        scanner.nextLine();
        UUID uuid = UUID.randomUUID();
        PlayerClass playerClassEnum = PlayerClass.values()[playerClass - 1];
        Player player = new Player(name, uuid, playerClassEnum, true);
        System.out.println(player.getPlayerStatsString());
        return player;
    }


    public static void generateStartingEquipment(Player player) {
        PlayerClass playerClass = player.getPlayerClass();
        Weapon weapon;
        Armor helmet;
        Armor chestplate;
        Armor leggings;
        Armor boots;
        switch(playerClass) {
            case ROGUE:
                weapon = new Weapon("Rogue's basic sword", UUID.randomUUID(), Weapon.WeaponQuality.JAGGED, Weapon.WeaponMaterial.IRON);
                helmet = new Armor(UUID.randomUUID(), Armor.ArmorSlot.HELMET, "Rogue's basic helmet", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                chestplate = new Armor(UUID.randomUUID(), Armor.ArmorSlot.CHESTPLATE, "Rogue's basic chestplate", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                leggings = new Armor(UUID.randomUUID(), Armor.ArmorSlot.LEGGINGS, "Rogue's basic leggings", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                boots = new Armor(UUID.randomUUID(), Armor.ArmorSlot.BOOTS, "Rogue's basic boots", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                break;
            case WIZARD:
                weapon = new Weapon("Wizard's basic sword", UUID.randomUUID(), Weapon.WeaponQuality.JAGGED, Weapon.WeaponMaterial.IRON);
                helmet = new Armor(UUID.randomUUID(), Armor.ArmorSlot.HELMET, "Wizard's basic helmet", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                chestplate = new Armor(UUID.randomUUID(), Armor.ArmorSlot.CHESTPLATE, "Wizard's basic chestplate", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                leggings = new Armor(UUID.randomUUID(), Armor.ArmorSlot.LEGGINGS, "Wizard's basic leggings", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                boots = new Armor(UUID.randomUUID(), Armor.ArmorSlot.BOOTS, "Wizard's basic boots", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                break;
            case PALADIN:
                weapon = new Weapon("Paladin's basic sword", UUID.randomUUID(), Weapon.WeaponQuality.JAGGED, Weapon.WeaponMaterial.IRON);
                helmet = new Armor(UUID.randomUUID(), Armor.ArmorSlot.HELMET, "Paladin's basic helmet", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                chestplate = new Armor(UUID.randomUUID(), Armor.ArmorSlot.CHESTPLATE, "Paladin's basic chestplate", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                leggings = new Armor(UUID.randomUUID(), Armor.ArmorSlot.LEGGINGS, "Paladin's basic leggings", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                boots = new Armor(UUID.randomUUID(), Armor.ArmorSlot.BOOTS, "Paladin's basic boots", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                break;
            case null, default:
                weapon = Weapon.generateWeapon(0, 5, player.getInventory());
                helmet = new Armor(UUID.randomUUID(), Armor.ArmorSlot.HELMET, "Basic helmet", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                chestplate = new Armor(UUID.randomUUID(), Armor.ArmorSlot.CHESTPLATE, "Basic chestplate", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                leggings = new Armor(UUID.randomUUID(), Armor.ArmorSlot.LEGGINGS, "Basic leggings", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                boots = new Armor(UUID.randomUUID(), Armor.ArmorSlot.BOOTS, "Basic boots", Armor.ArmorQuality.WORN, Armor.ArmorMaterial.LEATHER);
                break;
        }
        player.equipArmor(helmet);
        player.equipArmor(chestplate);
        player.equipArmor(leggings);
        player.equipArmor(boots);
        player.setCurrentWeapon(weapon);
    }
}