package com.github.joshuacgunn.core.item;

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

    /**
     * Creates a new weapon with the specified properties.
     *
     * @param itemName The name of the weapon
     * @param itemUUID The unique identifier for the weapon
     * @param damage The base damage value of the weapon
     * @param weaponDurability The durability value of the weapon
     */
    public Weapon(String itemName, UUID itemUUID, float damage, float weaponDurability) {
        super(itemName, itemUUID);
        this.weaponDamage = damage;
        this.weaponDurability = weaponDurability;
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
}