package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;

import java.util.*;

/**
 * Master entity class for which each sub-entity (player, npc, monster, etc.) will inherit
 * @author github.com/joshuacgunn
 * @version 1.0
 */

public abstract class Entity {
/**
     * The name of the entity.
     */
    protected String entityName;

    /**
     * The unique identifier (UUID) of the entity.
     */
    protected UUID entityUUID;

    /**
     * The current health points (HP) of the entity.
     * This will be set in subclasses, but all entities need this field.
     */
    protected float entityHp;

    /**
     * The current weapon equipped by the entity.
     */
    protected Weapon currentWeapon;

    /**
     * Indicates whether the entity is dead.
     */
    protected boolean isDead;

    /**
     * The total defense value of the entity.
     * This is initialized to 0 and can be increased by equipping armor.
     */
    protected float entityDefense = 0;

    /**
     * The name of the helmet armor piece equipped by the entity.
     */
    protected String helmet;

    /**
     * The name of the chestplate armor piece equipped by the entity.
     */
    protected String chestplate;

    /**
     * The name of the leggings armor piece equipped by the entity.
     */
    protected String leggings;

    /**
     * The name of the boots armor piece equipped by the entity.
     */
    protected String boots;

    /**
     * A map of all armor pieces equipped by the entity.
     * The key is the armor slot (e.g., "HELMET", "CHESTPLATE"), and the value is the Armor object.
     */
    protected Map<String, Armor> armors = new HashMap<>();

    /**
     * A static map of all entities in the game.
     * The key is the entity's UUID, and the value is the Entity object.
     */
    public static Map<UUID, Entity> entityMap = new HashMap<>();

    /**
     * @param name Sets entity name
     * @param uuid Unique identifier to be used to identify objects in registries
     */
    public Entity(String name, UUID uuid) {
        this.entityName = name;
        this.entityUUID = uuid;
        entityMap.put(uuid, this);
    }

    /**
     * @return A string containing the entity's name
     */
    public String getEntityName() {
        return this.entityName;
    }

    /**
     * @return A unique identifier (UUID) of the entity
     */
    public UUID getEntityUUID() {
        return this.entityUUID;
    }

    /**
     * @return A float value containing the entity's current entityHp
     */
    public float getEntityHp() {
        return this.entityHp;
    }

    /**
     * @return A list of all active entity's
     */
    public static List<Entity> getEntities() {
        return entityMap.values().stream().toList();
    }

    /**
     *
     * @param entityClass The type of entity you want to get a list of (eg, EntityPlayer.class)
     * @return A list of all active entity's of entityClass type
     */
    public static <T extends Entity> List<T> getEntitiesByType(Class<T> entityClass) {
        return entityMap.values().stream().filter(entityClass::isInstance).map(entityClass::cast).toList();
    }

/**
     * Gets the current weapon equipped by the entity.
     *
     * @return The current weapon equipped by the entity.
     */
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * Sets the current weapon equipped by the entity.
     *
     * @param weapon The weapon to be equipped by the entity.
     */
    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    /**
     * Reduces the entity's health points (HP) by the specified damage amount.
     * If the damage exceeds the current HP, the entity is marked as dead.
     *
     * @param damage The amount of damage to be taken by the entity.
     */
    public void takeDamage(float damage) {
        if (damage > this.entityHp) {
            this.entityHp = 0;
            this.isDead = true;
        } else {
            this.entityHp -= damage;
        }
    }

    /**
     * Equips the specified armor to the entity.
     * If the armor slot is already occupied, a message is printed.
     * The entity's defense value is increased by the armor's defense value.
     *
     * @param armor The armor to be equipped by the entity.
     */
    public void equipArmor(Armor armor) {
        if (armors.containsKey(armor.getArmorSlot())) {
            System.out.println("You already have an armor equipped in slot " + armor.getArmorSlot());
        } else {
            armors.put(armor.getArmorSlot(), armor);
            entityDefense += armor.getArmorDefense();
            if (armor.getArmorSlot().equals("HELMET")) {
                helmet = armor.getArmorName();
            } else if (armor.getArmorSlot().equals("CHESTPLATE")) {
                chestplate = armor.getArmorName();
            } else if (armor.getArmorSlot().equals("LEGGINGS")) {
                leggings = armor.getArmorName();
            } else if (armor.getArmorSlot().equals("BOOTS")) {
                boots = armor.getArmorName();
            }
        }
    }

    /**
     * Removes the specified armor from the entity.
     * If the armor is not equipped, a message is printed.
     * The entity's defense value is decreased by the armor's defense value.
     *
     * @param armor The armor to be removed from the entity.
     */
    public void deEquipArmor(Armor armor) {
        if (!armors.containsValue(armor)) {
            System.out.println("You don't have that equipped!");
        } else {
            armors.remove(armor.getArmorSlot());
            entityDefense -= armor.getArmorDefense();
            if (armor.getArmorSlot().equals("HELMET")) {
                helmet = null;
            } else if (armor.getArmorSlot().equals("CHESTPLATE")) {
                chestplate = null;
            } else if (armor.getArmorSlot().equals("LEGGINGS")) {
                leggings = null;
            } else if (armor.getArmorSlot().equals("BOOTS")) {
                boots = null;
            }
        }
    }

    /**
     * Gets a list of all armor pieces equipped by the entity.
     *
     * @return A list of all armor pieces equipped by the entity, or null if no armor is equipped.
     */
    public ArrayList<Item> getArmors() {
        if (armors.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(armors.values());
        }
    }
}
