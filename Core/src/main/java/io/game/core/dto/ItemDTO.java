package io.game.core.dto;

import io.game.core.item.Item;

import java.io.Serializable;
import java.util.UUID;

public class ItemDTO implements Serializable {
    private String itemType;
    private String itemName;
    private UUID itemUUID;
    private Item.ItemRarity itemRarity;
    private float itemValue;

    public ItemDTO() { }

    public String getItemType() {
        return itemType;
    }

    public float getItemValue() {
        return itemValue;
    }

    public void setItemValue(float value) {
        this.itemValue = value;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Item.ItemRarity getItemRarity() {
        return itemRarity;
    }

    public void setItemRarity(Item.ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
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
