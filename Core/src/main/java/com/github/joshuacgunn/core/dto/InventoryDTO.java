package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryDTO implements Serializable {
    private UUID parentEntityUUID;
    private UUID inventoryUUID;
    private List<UUID> itemUUIDs = new ArrayList<>();

    public InventoryDTO() { }

    public List<UUID> getItemUUIDs() {
        return itemUUIDs;
    }

    public void setItemUUIDs(List<UUID> uuids) {
        this.itemUUIDs = uuids;
    }

    public UUID getParentEntityUUID() {
        return parentEntityUUID;
    }

    public void setParentEntityUUID(UUID parentEntityUUID) {
        this.parentEntityUUID = parentEntityUUID;
    }

    public UUID getInventoryUUID() {
        return inventoryUUID;
    }

    public void setInventoryUUID(UUID inventoryUUID) {
        this.inventoryUUID = inventoryUUID;
    }

}
