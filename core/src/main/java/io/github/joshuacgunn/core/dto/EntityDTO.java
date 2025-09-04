package io.github.joshuacgunn.core.dto;

import io.github.joshuacgunn.core.entity.Entity;
import io.github.joshuacgunn.core.entity.EntityStats;

import java.util.*;

/**
 * Base DTO class for transferring entity data.
 * It serves as the parent for all entity-specific DTOs.
 */
public class EntityDTO {
    private String entityType;
    private String entityName;
    private UUID entityUUID;
    private float entityHp;
    private boolean isAlive;
    private UUID currentLocationUUID;
    private String currentLocationType;
    public EntityStats entityStats;
    private float entityDefense;
    private UUID currentWeaponUUID;
    private List<UUID> equippedArmorUUIDs = new ArrayList<>();
    private InventoryDTO inventory;
    private Map<Entity.StatusEffect, Integer> statusEffects = new HashMap<>();


    public EntityDTO() {
        // Default constructor
    }

    public UUID getCurrentLocationUUID() {
        return currentLocationUUID;
    }

    public void setCurrentLocationUUID(UUID currentLocationUUID) {
        this.currentLocationUUID = currentLocationUUID;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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

    public Map<Entity.StatusEffect, Integer> getStatusEffects() {
        return statusEffects;
    }

    public void setStatusEffects(Map<Entity.StatusEffect, Integer> statusEffects) {
        this.statusEffects = statusEffects;
    }

    public int getEntityStatsValue(EntityStats.Stat stat) {
        switch (stat) {
            case LUCK:
                return this.entityStats.getStatValue(EntityStats.Stat.LUCK);
            case CHARISMA:
                return this.entityStats.getStatValue(EntityStats.Stat.CHARISMA);
            case VITALITY:
                return this.entityStats.getStatValue(EntityStats.Stat.VITALITY);
            case INTELLIGENCE:
                return this.entityStats.getStatValue(EntityStats.Stat.INTELLIGENCE);
            case STRENGTH:
                return this.entityStats.getStatValue(EntityStats.Stat.STRENGTH);
            case DEXTERITY:
                return this.entityStats.getStatValue(EntityStats.Stat.DEXTERITY);
            default:
                return 0;
        }
    }

    public EntityStats getEntityStats() {
        return this.entityStats;
    }

    public void setEntityStats(EntityStats entityStats) {
        this.entityStats = entityStats;
    }

    public void setEntityStatsValue(EntityStats.Stat stat, int value) {
        this.entityStats.setStatValue(stat, value);
    }

    public String getCurrentLocationType() {
        return currentLocationType;
    }

    public void setCurrentLocationType(String currentLocationType) {
        this.currentLocationType = currentLocationType;
    }
}
