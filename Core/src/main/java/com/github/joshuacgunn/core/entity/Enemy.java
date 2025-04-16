package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;

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
    public enum EnemyType {
        KOBOLD("Kobold", 30.0f, 40.0f, 0, 2, 0, 1, 0, 1),
        GOBLIN("Goblin", 45.0f, 55.0f, 1, 3, 0, 1, 1, 2),
        ORC("Orc", 50.0f, 70.0f, 2, 3, 1, 3, 2, 3),
        OGRE("Ogre", 70.0f, 80.0f, 2, 4, 2, 3, 2, 4),
        TROLL("Troll", 80.0f, 100.0f, 2, 4, 3, 4, 3, 4),
        WRAITH("Wraith", 90.0f, 120.0f, 3, 4, 4, 5, 4, 5);

        private final String name;
        private final float minHp;
        private final float maxHp;
        private final int minArmorPieces;
        private final int maxArmorPieces;
        private final int minArmorQuality;
        private final int maxArmorQuality;
        private final int minWeaponQuality;
        private final int maxWeaponQuality;

        EnemyType(String name, float minHp, float maxHp,
                  int minArmorPieces, int maxArmorPieces,
                  int minArmorQuality, int maxArmorQuality,
                  int minWeaponQuality, int maxWeaponQuality) {
            this.name = name;
            this.minHp = minHp;
            this.maxHp = maxHp;
            this.minArmorPieces = minArmorPieces;
            this.maxArmorPieces = maxArmorPieces;
            this.minArmorQuality = minArmorQuality;
            this.maxArmorQuality = maxArmorQuality;
            this.minWeaponQuality = minWeaponQuality;
            this.maxWeaponQuality = maxWeaponQuality;
        }

        public String getName() {
            return name;
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
            Armor.generateArmor(type.minArmorQuality, type.maxArmorQuality, this.getInventory());
        }

        // Generate weapon
        Weapon.generateWeapon(type.minWeaponQuality, type.maxWeaponQuality, this.getInventory());
    }

    /**
     * Factory method to create an enemy of the specified type.
     *
     * @param type The type of enemy to create
     * @param uuid The unique identifier for the enemy
     * @param newEnemy Whether this is a newly generated enemy
     * @return A new enemy of the specified type
     */
    public static Enemy createEnemy(EnemyType type, UUID uuid, boolean newEnemy) {
        return new Enemy(type, uuid, newEnemy);
    }
}