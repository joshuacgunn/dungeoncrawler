package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.item.Item;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class InventoryDTO implements Serializable {
    private UUID parentEntityUUID;
    private UUID inventoryUUID;
    private List<ItemDTO> itemList;

    public InventoryDTO() { }

    public List<ItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemDTO> itemList) {
        this.itemList = itemList;
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
