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
    private UUID currentWeaponUUID;
    private List<UUID> equippedArmorUUIDs = new ArrayList<>();
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

    public UUID getCurrentWeaponUUID() {
        return currentWeaponUUID;
    }

    public void setCurrentWeaponUUID(UUID uuid) {
        this.currentWeaponUUID = uuid;
    }

    public List<UUID> getEquippedArmorUUIDs() {
        return equippedArmorUUIDs;
    }

    public void setEquippedArmorUUIDs(List<UUID> uuids) {
        this.equippedArmorUUIDs = uuids;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}