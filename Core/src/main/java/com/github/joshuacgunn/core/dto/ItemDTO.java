package com.github.joshuacgunn.core.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemDTO implements Serializable {
    private String itemName;
    private UUID itemUUID;
    private String itemType;

    public ItemDTO() { }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public UUID getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(UUID itemUUID) {
        this.itemUUID = itemUUID;
    }
}
