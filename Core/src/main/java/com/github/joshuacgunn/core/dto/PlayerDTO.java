package com.github.joshuacgunn.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * PlayerDTO is a Data Transfer Object for transferring player data.
 * It implements Serializable for object serialization.
 */
public class PlayerDTO implements Serializable {
    private String entityName;
    private UUID entityUUID;
    private float entityHp;
    private DungeonDTO currentDungeon;
    private InventoryDTO inventory;
    private WeaponDTO currentWeapon;
    private ArrayList<ArmorDTO> armors;

    public PlayerDTO() { }

    public void setCurrentWeapon(WeaponDTO weaponDTO) {
        this.currentWeapon = weaponDTO;
    }

    public WeaponDTO getCurrentWeapon() {
        return this.currentWeapon;
    }

    public InventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

    public DungeonDTO getCurrentDungeon() {
        return currentDungeon;
    }

    public void setCurrentDungeon(DungeonDTO currentDungeon) {
        this.currentDungeon = currentDungeon;
    }

    /**
     * Gets the entity name
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

    /**
     * Get the entity's hp
     * @return current entity hp
     */
    public float getEntityHp() {
        return entityHp;
    }

    /**
     * @param name Set entity name, for DTO -> Entity object
     */
    public void setEntityName(String name) {
        this.entityName = name;
    }

    /**
     * @param uuid Set entity UUID, for DTO -> Entity object
     */
    public void setEntityUUID(UUID uuid) {
        this.entityUUID = uuid;
    }

    /**
     * Sets entity hp
     * @param hp what value to set entity's hp to
     */
    public void setEntityHp(float hp) {
        this.entityHp = hp;
    }

    public void setArmors(List<ArmorDTO> armorDTOList) {
        this.armors = new ArrayList<>(armorDTOList);
    }

    public ArrayList<ArmorDTO> getArmors() {
        return armors;
    }
}
