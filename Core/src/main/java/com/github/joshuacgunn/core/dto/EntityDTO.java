package com.github.joshuacgunn.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Base DTO class for transferring entity data.
 * It serves as the parent for all entity-specific DTOs.
 */
public class EntityDTO implements Serializable {
    private String entityName;
    private UUID entityUUID;
    private float entityHp;
    private boolean isDead;
    private float entityDefense;
    private WeaponDTO currentWeapon;
    private List<ArmorDTO> equippedArmors;
    private String entityType;
    private InventoryDTO inventory;
    private UUID currentDungeonUUID;

    public EntityDTO() {
        // Default constructor
    }

    public UUID getCurrentDungeonUUID() {
        return currentDungeonUUID;
    }

    public void setCurrentDungeonUUID(UUID currentDungeonUUID) {
        this.currentDungeonUUID = currentDungeonUUID;
    }

    public InventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

    public void setEntityUUID(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public float getEntityHp() {
        return entityHp;
    }

    public void setEntityHp(float entityHp) {
        this.entityHp = entityHp;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getEntityDefense() {
        return entityDefense;
    }

    public void setEntityDefense(float entityDefense) {
        this.entityDefense = entityDefense;
    }

    public WeaponDTO getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(WeaponDTO currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public List<ArmorDTO> getEquippedArmors() {
        return equippedArmors;
    }

    public void setEquippedArmors(List<ArmorDTO> equippedArmors) {
        this.equippedArmors = equippedArmors != null ? new ArrayList<>(equippedArmors) : null;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}