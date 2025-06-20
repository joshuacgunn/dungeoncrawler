package io.game.core.entity;

import io.game.core.item.Armor;
import io.game.core.item.Weapon;
import io.game.core.item.Item;

import java.util.Random;
import java.util.UUID;

/**
 * Represents an enemy entity in the game with type-specific attributes.
 */
public class Enemy extends Entity {

    private static final Random rand = new Random();

    /**
     * Enum representing different types of enemies with their specific attributes.
     */
    /**
     * Enum representing different types of enemies with their specific attributes.
     */
    public enum EnemyType {
        KOBOLD("Kobold", 30.0f, 40.0f, 0, 1, Item.ItemRarity.COMMON, Item.ItemRarity.COMMON),
        GOBLIN("Goblin", 45.0f, 55.0f, 1, 2, Item.ItemRarity.COMMON, Item.ItemRarity.COMMON),
        ORC("Orc", 50.0f, 70.0f, 1, 3, Item.ItemRarity.UNCOMMON, Item.ItemRarity.UNCOMMON),
        OGRE("Ogre", 70.0f, 80.0f, 2, 3, Item.ItemRarity.UNCOMMON, Item.ItemRarity.UNCOMMON),
        TROLL("Troll", 80.0f, 100.0f, 2, 4, Item.ItemRarity.RARE, Item.ItemRarity.RARE),
        WRAITH("Wraith", 90.0f, 120.0f, 3, 4, Item.ItemRarity.EPIC, Item.ItemRarity.EPIC),
        DRAGON_PRIEST("Dragon Priest", 120.0f, 150.0f, 4, 4, Item.ItemRarity.LEGENDARY, Item.ItemRarity.LEGENDARY),
        BOSS("Boss", 0, 0, 0, 0, Item.ItemRarity.COMMON, Item.ItemRarity.COMMON); // Placeholder information, boss will be generated separately upon creation

        private final String name;
        private final float minHp;
        private final float maxHp;
        private final int minArmorPieces;
        private final int maxArmorPieces;
        private final Item.ItemRarity armorRarity;
        private final Item.ItemRarity weaponRarity;

        EnemyType(String name, float minHp, float maxHp,
                  int minArmorPieces, int maxArmorPieces, Item.ItemRarity armorRarity,
                  Item.ItemRarity weaponRarity) {
            this.name = name;
            this.minHp = minHp;
            this.maxHp = maxHp;
            this.minArmorPieces = minArmorPieces;
            this.maxArmorPieces = maxArmorPieces;
            this.armorRarity = armorRarity;
            this.weaponRarity = weaponRarity;
        }

        public String getName() {
            return name;
        }

        public Item.ItemRarity getWeaponRarity() {
            return weaponRarity;
        }

        public Item.ItemRarity getArmorRarity() {
            return armorRarity;
        }
    }

    private final EnemyType type;



    /**
     * Constructs a new enemy of the specified type.
     *
     * @param type The type of enemy to create
     * @param uuid The unique identifier for the enemy
     * @param newEnemy Whether this is a newly generated enemy
     */
    public Enemy(EnemyType type, UUID uuid, boolean newEnemy) {
        super(type.name, uuid);
        this.type = type;

        // Set HP based on enemy type
        this.entityHp = rand.nextFloat(type.minHp, type.maxHp);

        if (newEnemy) {
            generateEquipment();
            if (type == EnemyType.BOSS) {
                generateBoss();
            }
        }
    }

    /**
     * Gets the type of this enemy.
     *
     * @return The enemy type
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Generates appropriate equipment for this enemy based on its type.
     */
    private void generateEquipment() {
        // Generate armor pieces
        int armorPieces = rand.nextInt(type.minArmorPieces, type.maxArmorPieces + 1);

        for (int i = 0; i < armorPieces; i++) {
            Armor.generateArmor(type.getArmorRarity(), this.getInventory(), true);
        }

        // Generate weapon
        Weapon.generateWeapon(type.getWeaponRarity(), this.getInventory());
    }

    private void generateBoss() {

    }
}