package com.joshuacgunn.core.dto;

import com.joshuacgunn.core.container.Chest;

import java.util.List;
import java.util.UUID;

public class ChestDTO {
    private boolean locked;
    private UUID chestUUID;
    private UUID keyUUID;
    private Chest.ChestRarity chestRarity;
    private List<UUID> chestContents;
    private UUID parentFloor;

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setKeyUUID(UUID keyUUID) {
        this.keyUUID = keyUUID;
    }

    public UUID getKeyUUID() {
        return keyUUID;
    }

    public void setChestRarity(Chest.ChestRarity chestRarity) {
        this.chestRarity = chestRarity;
    }

    public Chest.ChestRarity getChestRarity() {
        return chestRarity;
    }

    public void setChestContents(List<UUID> chestContents) {
        this.chestContents = chestContents;
    }

    public List<UUID> getChestContents() {
        return chestContents;
    }

    public void setParentFloor(UUID parentFloor) {
        this.parentFloor = parentFloor;
    }

    public UUID getParentFloor() {
        return parentFloor;
    }

    public void setChestUUID(UUID chestUUID) {
        this.chestUUID = chestUUID;
    }

    public UUID getChestUUID() {
        return chestUUID;
    }


}
