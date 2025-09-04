package io.github.joshuacgunn.core.item;

import io.github.joshuacgunn.core.container.Container;
import io.github.joshuacgunn.core.container.Inventory;
import io.github.joshuacgunn.core.entity.Entity;
import io.github.joshuacgunn.core.entity.NPC;

import java.util.Random;
import java.util.UUID;

/**
 * Represents a weapon used for combat in the game world.
 *
 * Weapons have various attributes including damage, durability, and armor penetration.
 * These attributes are determined by the weapon's quality level, which ranges from
 * RUSTED (lowest) to HEAVENLY (highest).
 */
public class Weapon extends Item {
    /** Base damage this weapon inflicts in combat */
    private float weaponDamage;

    /** Current durability points before the weapon breaks */
    private float weaponDurability;

    /** Percentage of target's armor that this weapon bypasses (0.0 to 1.0) */
    private float armorPenetration;

    /** Quality tier of this weapon, affecting its base stats */
    private WeaponQuality weaponQuality;

    private WeaponMaterial weaponMaterial;

    /**
     * Enum representing different materials that weapons can be made of, each
     * with a specific base damage multiplier. The base damage multiplier
     * affects the overall damage output of a weapon crafted with the respective
     * material.
     */
    public enum WeaponMaterial {
        BRONZE(0.5f),
        IRON(1.0f),
        STEEL(1.2f),
        OBSIDIAN(1.5f),
        MITHRIL(1.8f),
        CELESTIUM(2.2f),
        DEMONITE(2.5f);

        public final float baseDamageMultiplier;

        WeaponMaterial(float baseDamageMultiplier) {
            this.baseDamageMultiplier = baseDamageMultiplier;
        }
    }

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
        HEAVENLY(37f, 0.55f, 3.5f),
        HELLMADE(41f, 0.6f, 4.0f);

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
     */
    public Weapon(String itemName, UUID itemUUID, ItemRarity itemRarity, boolean isNew) {
        super(itemName, itemUUID);
        this.itemRarity = itemRarity;
        this.isEquippable = true;
        if (isNew) {
            this.weaponDurability = 100f;
            updateAttributes();
        }
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

    public WeaponMaterial getWeaponMaterial() {
        return weaponMaterial;
    }

    public void setWeaponQuality(WeaponQuality quality) {
        this.weaponQuality = quality;
    }

    public void setWeaponMaterial(WeaponMaterial material) {
        this.weaponMaterial = material;
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

    public void updateQualityMaterial() {
        Random random = new Random();
        float extraQualityChance = random.nextFloat();

        switch (this.itemRarity) {
            case COMMON:
                this.itemValue = random.nextFloat(10f, 15f);
                if (extraQualityChance < .9f) {
                    this.weaponQuality = WeaponQuality.values()[random.nextInt(0, 2)];
                } else {
                    this.weaponQuality = WeaponQuality.JAGGED;
                }
                this.weaponMaterial = WeaponMaterial.BRONZE;
                break;
            case UNCOMMON:
                this.itemValue = random.nextFloat(20f, 30f);
                if (extraQualityChance < .15f) {
                    this.weaponQuality = WeaponQuality.DULL;
                } else if (extraQualityChance < .9f) {
                    this.weaponQuality = WeaponQuality.JAGGED;
                } else {
                    this.weaponQuality = WeaponQuality.TEMPERED;
                }
                this.weaponMaterial = WeaponMaterial.IRON;
                break;
            case RARE:
                this.itemValue = random.nextFloat(35f, 45f);
                if (extraQualityChance < .9f) {
                    this.weaponQuality = WeaponQuality.values()[random.nextInt(2, 4)];
                } else {
                    this.weaponQuality = WeaponQuality.RAZOR;
                }
                this.weaponMaterial = WeaponMaterial.STEEL;
                break;
            case EPIC:
                this.itemValue = random.nextFloat(65f, 80f);
                if (extraQualityChance < .9f) {
                    this.weaponQuality = WeaponQuality.values()[4];
                } else {
                    this.weaponQuality = WeaponQuality.MASTERFUL;
                }
                this.weaponMaterial = WeaponMaterial.OBSIDIAN;
                break;
            case LEGENDARY:
                this.itemValue = random.nextFloat(125f, 145f);
                if (extraQualityChance < .9f) {
                    this.weaponQuality = WeaponQuality.values()[5];
                } else {
                    this.weaponQuality = WeaponQuality.EXQUISITE;
                }
                this.weaponMaterial = WeaponMaterial.MITHRIL;
                break;
            case MYTHICAL:
                this.itemValue = random.nextFloat(200f, 225f);
                this.weaponQuality = WeaponQuality.HEAVENLY;
                this.weaponMaterial = WeaponMaterial.CELESTIUM;
                break;
            case DEMONIC:
                this.itemValue = random.nextFloat(300f, 350f);
                this.weaponQuality = WeaponQuality.HELLMADE;
                this.weaponMaterial = WeaponMaterial.DEMONITE;
                break;
        }
    }

    /**
     * Creates a random weapon with quality between specified bounds and adds it to a container.
     *
     * @param rarity Rarity of the item to generate
     * @param container Container to add the weapon to
     * @return The generated weapon
     */
    public static Weapon generateWeapon(ItemRarity rarity, Container container) {

        Weapon generatedWeapon = new Weapon("Weapon", UUID.randomUUID(), rarity, true);

        if (container instanceof Inventory inventory) {
            Entity entity = inventory.getOwner();
            if (!(entity instanceof NPC)) {
                generatedWeapon.setItemName(entity.getEntityName() + "s " + generatedWeapon.getWeaponQuality().name().toLowerCase() + " " + generatedWeapon.getWeaponMaterial().name().toLowerCase() + " sword");
                entity.addItem(generatedWeapon);
                entity.setCurrentWeapon(generatedWeapon);
            } else {
                generatedWeapon.setItemName(generatedWeapon.getWeaponQuality().name().toLowerCase() + " " + generatedWeapon.getWeaponMaterial().name().toLowerCase() + " sword");
                entity.addItem(generatedWeapon);
            }
        } else {
            generatedWeapon.setItemName(generatedWeapon.getWeaponQuality().name().toLowerCase() + " " + generatedWeapon.getWeaponMaterial().name().toLowerCase() + " sword");
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
        updateQualityMaterial();

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
