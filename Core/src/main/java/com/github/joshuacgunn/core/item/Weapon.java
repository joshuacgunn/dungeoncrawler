package com.github.joshuacgunn.core.item;

import com.github.joshuacgunn.core.container.Container;
import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.entity.Entity;

import java.util.Random;
import java.util.UUID;

/**
 * Represents a weapon used for combat in the game world.
 *
 * Weapons have various attributes including damage, durability, and armor penetration.
 * These attributes are determined by the weapon's quality level, which ranges from
 * RUSTED (lowest) to LEGENDARY (highest).
 */
public class Weapon extends Item {
    /** Base damage this weapon inflicts in combat */
    private float weaponDamage;

    /** Current durability points before the weapon breaks */
    private float weaponDurability;

    /** Percentage of target's armor that this weapon bypasses (0.0 to 1.0) */
    private float armorPenetration;

    /** Quality tier of this weapon, affecting its base stats */
    private final WeaponQuality weaponQuality;

    /**
     * Defines weapon quality tiers and their corresponding base attributes.
     * Higher quality weapons have progressively better damage, armor penetration,
     * and durability values.
     */
    public enum WeaponQuality {
        RUSTED(7f, 0.0f, 0.4f),
        DULL(14f, 0.1f, 1.0f),
        JAGGED(17f, 0.2f, 1.4f),
        TEMPERED(22f, 0.3f, 1.9f),
        RAZOR(25f, 0.4f, 2.4f),
        MASTERFUL(29f, 0.45f, 2.7f),
        EXQUISITE(33f, 0.5f, 3.0f),
        LEGENDARY(37f, 0.55f, 3.5f);

        /** Base damage value for this quality tier */
        public final float damage;

        /** Base armor penetration for this quality tier */
        public final float armorPen;

        /** Durability multiplier for this quality tier */
        public final float durabilityMult;

        /**
         * Initializes a weapon quality tier with specific attribute values.
         *
         * @param damage Base damage for weapons of this quality
         * @param armorPen Base armor penetration (0.0 to 1.0)
         * @param durabilityMult Multiplier applied to base durability
         */
        WeaponQuality(float damage, float armorPen, float durabilityMult) {
            this.damage = damage;
            this.armorPen = armorPen;
            this.durabilityMult = durabilityMult;
        }
    }

    /**
     * Creates a new weapon with specified name, ID, and quality.
     *
     * @param itemName Name of the weapon
     * @param itemUUID Unique identifier for this weapon instance
     * @param quality Quality tier affecting weapon stats
     */
    public Weapon(String itemName, UUID itemUUID, WeaponQuality quality) {
        super(itemName, itemUUID);
        this.weaponQuality = quality;
        this.weaponDurability = 100f;
        updateAttributes();
    }

    /**
     * Returns the weapon's current damage value.
     *
     * @return Calculated damage value
     */
    public float getWeaponDamage() {
        return weaponDamage;
    }

    /**
     * Returns the weapon's current durability.
     *
     * @return Current durability points
     */
    public float getWeaponDurability() {
        return weaponDurability;
    }

    /**
     * Returns this weapon's quality tier.
     *
     * @return The WeaponQuality enum value
     */
    public WeaponQuality getWeaponQuality() {
        return this.weaponQuality;
    }

    /**
     * Sets the weapon's damage to a specific value.
     *
     * @param damage New damage value
     */
    public void setWeaponDamage(float damage) {
        this.weaponDamage = damage;
    }

    /**
     * Sets the weapon's durability to a specific value.
     *
     * @param durability New durability value
     */
    public void setWeaponDurability(float durability) {
        this.weaponDurability = durability;
    }

    /**
     * Returns the weapon's armor penetration rate.
     *
     * @return Armor penetration as a decimal (0.0 to 1.0)
     */
    public float getArmorPenetration() {
        return armorPenetration;
    }

    /**
     * Sets the weapon's armor penetration rate.
     *
     * @param armorPenetration New armor penetration value (0.0 to 1.0)
     */
    public void setArmorPenetration(float armorPenetration) {
        this.armorPenetration = armorPenetration;
    }

    /**
     * Creates a random weapon with quality between specified bounds and adds it to a container.
     *
     * @param minQuality Minimum quality index (inclusive)
     * @param maxQuality Maximum quality index (inclusive)
     * @param container Container to add the weapon to
     * @return The generated weapon
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
     * Calculates and updates weapon statistics based on quality tier.
     *
     * This method determines final weapon damage by applying:
     * - Base damage from the weapon quality
     * - Random variation (±15%)
     * - Quality tier scaling multiplier
     *
     * It also calculates armor penetration with quality-appropriate
     * randomized values and sets durability based on quality multipliers.
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