package com.github.joshuacgunn.core.item;

import java.util.UUID;

public class Armor extends Item {
    private String armorSlot;
    private float armorDefense;

    /**
     * Creates a new item and registers it in the global item map.
     *
     * @param slot the armorSlot of the armor piece
     * @param itemUUID The unique identifier for the item
     * @param armorDefense defense value for the armor
     */
    public Armor(UUID itemUUID, float armorDefense, String slot, String name) {
        super(name, itemUUID);
        this.armorDefense = armorDefense;
        this.armorSlot = slot;
    }

    public float getArmorDefense() {
        return armorDefense;
    }

    public String getArmorSlot() {
        return armorSlot;
    }

    public String getArmorName() {
        return getItemName();
    }
}
