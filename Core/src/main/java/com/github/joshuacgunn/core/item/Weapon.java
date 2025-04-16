package com.github.joshuacgunn.core.item;

import com.github.joshuacgunn.core.container.Container;
import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.entity.Entity;

import java.util.Random;
import java.util.UUID;

/**
 * Represents a weapon item in the game.
 * <p>
 * The Weapon class extends the base Item class and adds weapon-specific properties
 * such as damage, durability, and armor penetration. Weapons can be equipped by entities
 * for combat and have varying qualities that affect their combat effectiveness.
 * <p>
 * Weapons have quality levels that determine their base damage, armor penetration capabilities,
 * and durability multipliers. Higher quality weapons generally deal more damage and have better
 * armor penetration.
 */
public class Weapon extends Item {
    /** The amount of damage this weapon deals */
    private float weaponDamage;

    /** The durability of this weapon, representing how long it can be used before breaking */
    private float weaponDurability;

    /** How much of the target's armor this weapon can bypass, as a percentage */
    private float armorPenetration;

    /** The quality level of this weapon, affecting its stats */
    private final WeaponQuality weaponQuality;

    /**
     * Defines the possible quality levels for weapons and their associated stat multipliers.
     * Each quality level has different values for damage, armor penetration, and durability.
     */
    public enum WeaponQuality {
        RUSTED(12f, 0.0f, 0.4f),      // ~14.4 damage (12.2-16.6)
        DULL(16f, 0.1f, 1.0f),        // ~24 damage (20.4-27.6)
        JAGGED(20f, 0.2f, 1.4f),      // ~38 damage (32.3-43.7)
        TEMPERED(24f, 0.3f, 1.9f),    // ~52.8 damage (44.9-60.7)
        RAZOR(28f, 0.4f, 2.4f),       // ~78.4 damage (66.6-90.2)
        MASTERFUL(30f, 0.45f, 2.7f),    // ~95 damage
        EXQUISITE(32f, 0.5f, 3.0f),     // ~112 damage (95.2-128.8)
        LEGENDARY(36f, 0.55f, 3.5f);    // ~140 damage

        public final float damage;

        public final float armorPen;

        public final float durabilityMult;

        /**
         * Creates a new weapon quality level with the specified stat values.
         *
         * @param damage Base damage multiplier for this quality
         * @param armorPen Armor penetration value for this quality
         * @param durabilityMult Durability multiplier for this quality
         */
        WeaponQuality(float damage, float armorPen, float durabilityMult) {
            this.damage = damage;
            this.armorPen = armorPen;
            this.durabilityMult = durabilityMult;
        }
    }

    /**
     * Creates a new weapon with the specified properties.
     *
     * @param itemName The name of the weapon
     * @param itemUUID The unique identifier for the weapon
     * @param quality The quality of the weapon, used for damage, armor penetration, and durability
     */
    public Weapon(String itemName, UUID itemUUID, WeaponQuality quality) {
        super(itemName, itemUUID);
        this.weaponQuality = quality;
        this.weaponDurability = 100f;
        updateAttributes();
    }

    /**
     * Gets the base damage value of this weapon.
     *
     * @return The weapon's damage value
     */
    public float getWeaponDamage() {
        return weaponDamage;
    }

    /**
     * Gets the current durability of this weapon.
     *
     * @return The weapon's durability value
     */
    public float getWeaponDurability() {
        return weaponDurability;
    }

    /**
     * Gets the quality level of this weapon.
     *
     * @return The weapon's quality enum value
     */
    public WeaponQuality getWeaponQuality() {
        return this.weaponQuality;
    }

    /**
     * Sets the damage value for this weapon.
     *
     * @param damage The new damage value to set
     */
    public void setWeaponDamage(float damage) {
        this.weaponDamage = damage;
    }

    /**
     * Sets the durability for this weapon.
     *
     * @param durability The new durability value to set
     */
    public void setWeaponDurability(float durability) {
        this.weaponDurability = durability;
    }

    /**
     * Gets the armor penetration value of this weapon.
     *
     * @return The weapon's armor penetration value
     */
    public float getArmorPenetration() {
        return armorPenetration;
    }

    /**
     * Sets the armor penetration value for this weapon.
     *
     * @param armorPenetration The new armor penetration value to set
     */
    public void setArmorPenetration(float armorPenetration) {
        this.armorPenetration = armorPenetration;
    }

    /**
     * Generates a weapon with random quality between the specified minimum and maximum quality levels.
     * <p>
     * This static factory method creates a new Weapon instance with a random quality level
     * selected from the WeaponQuality enum values. The quality is randomly chosen between
     * the minQuality (inclusive) and maxQuality (inclusive) indices of the WeaponQuality enum.
     * <p>
     * The generated weapon is automatically added to the provided container. If the container
     * is an Inventory, the weapon name will include the owner's name (e.g., "Goblin's dull Weapon").
     * Otherwise, the weapon name will use a generic prefix (e.g., "A DULL Weapon").
     *
     * @param minQuality The minimum quality level index (inclusive) from WeaponQuality enum
     * @param maxQuality The maximum quality level index (inclusive) from WeaponQuality enum
     * @param container The container to which the generated weapon will be added
     * @return The newly generated Weapon instance with random quality
     */
    public static Weapon generateWeapon(int minQuality, int maxQuality, Container container) {
        Random rand = new Random();

        WeaponQuality quality = WeaponQuality.values()[rand.nextInt(minQuality, maxQuality+1)];

        Weapon generatedWeapon = new Weapon("Weapon", UUID.randomUUID(), quality);
        if (container instanceof Inventory inventory) {
            Entity entity = inventory.getOwner();
            generatedWeapon.setItemName(entity.getEntityName() + "s " + quality.name().toLowerCase() + " quality sword");
            entity.addItem(generatedWeapon);
            entity.setCurrentWeapon(generatedWeapon);
        } else {
            generatedWeapon.setItemName("A " + quality.name() + " quality sword");
            container.addItem(generatedWeapon);
        }
        return generatedWeapon;
    }

    /**
     * Updates the weapon's attributes based on its quality level.
     * Sets the weapon's damage and armor penetration values according to
     * the corresponding values in its WeaponQuality.
     */
    /**
     * Updates the weapon's attributes based on its quality level.
     * Calculates weapon damage and armor penetration with a balanced approach
     * that includes base values, quality multipliers and some randomness.
     */
    public void updateAttributes() {
        Random rand = new Random();

        // Base damage from quality
        float baseDamage = weaponQuality.damage;

        // Add variation (±15%)
        float variationFactor = 0.85f + (rand.nextFloat() * 0.3f);

        // Apply weapon-type and scaling multiplier (missing previously)
        float qualityMultiplier = 1.0f + (0.2f * weaponQuality.ordinal());

        this.weaponDamage = Math.round(baseDamage * variationFactor * qualityMultiplier);

        // Calculate armor penetration
        float baseArmorPen = weaponQuality.armorPen;
        float penVariation = rand.nextFloat() * 0.05f;

        if (weaponQuality.ordinal() > WeaponQuality.TEMPERED.ordinal()) {
            this.armorPenetration = Math.min(baseArmorPen + penVariation, 0.6f);
        } else {
            this.armorPenetration = Math.max(baseArmorPen - penVariation, 0f);
        }

        this.weaponDurability = 30f * weaponQuality.durabilityMult;
    }
}