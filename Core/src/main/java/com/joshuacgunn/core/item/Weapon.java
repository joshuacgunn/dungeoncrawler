package com.joshuacgunn.core.item;

import com.joshuacgunn.core.container.Container;
import com.joshuacgunn.core.container.Inventory;
import com.joshuacgunn.core.entity.Entity;

import java.util.Random;
import java.util.UUID;

/**
 * Represents a weapon item in the game.
 * <p>
 * The Weapon class extends the base Item class and adds weapon-specific properties
 * such as damage and durability. Weapons can be equipped by entities for combat.
 */
public class Weapon extends Item {
    /** The amount of damage this weapon deals */
    private float weaponDamage;

    /** The durability of this weapon, representing how long it can be used before breaking */
    private float weaponDurability;

    private float armorPenetration;

    private final WeaponQuality weaponQuality;

    public enum WeaponQuality {
        RUSTED(0.2f, 0.0f, 0.4f),
        DULL(0.5f, 0.1f, 1.0f),
        JAGGED(0.9f, 0.2f, 1.4f),
        TEMPERED(1.4f, 0.3f, 1.9f),
        RAZOR(1.8f, 0.4f, 2.4f),
        EXQUISITE(2.5f, 0.5f, 3.0f);

        public final float damage;
        public final float armorPen;
        public final float durabilityMult;

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

    public WeaponQuality getWeaponQuality() {
        return this.weaponQuality;
    }

    public void setWeaponDamage(float damage) {
        this.weaponDamage = damage;
    }

    public void setWeaponDurability(float durability) {
        this.weaponDurability = durability;
    }

    public float getArmorPenetration() {
        return armorPenetration;
    }

    public void setArmorPenetration(float armorPenetration) {
        this.armorPenetration = armorPenetration;
    }

    public static Weapon generateWeapon(int minQuality, int maxQuality, Container container) {
        Random rand = new Random();

        WeaponQuality quality = WeaponQuality.values()[rand.nextInt(minQuality, maxQuality+1)];

        Weapon generatedWeapon = new Weapon("Weapon", UUID.randomUUID(), quality);
        if (container instanceof Inventory inventory) {
            Entity entity = inventory.getOwner();
            generatedWeapon.setItemName(entity.getEntityName() + "s " + quality.name().toLowerCase() + " Weapon");
            entity.addItem(generatedWeapon);
        } else {
            generatedWeapon.setItemName("A " + quality.name() + " Weapon");
            container.addItem(generatedWeapon);
        }
        return generatedWeapon;
    }

    public void updateAttributes() {
        this.weaponDamage = weaponQuality.damage;
        this.armorPenetration = weaponQuality.armorPen;
    }
}