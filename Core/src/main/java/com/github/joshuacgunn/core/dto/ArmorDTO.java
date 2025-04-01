package com.github.joshuacgunn.core.dto;

import java.io.Serializable;

public class ArmorDTO extends ItemDTO {
    private String slot;
    private float armorDefense;

    public ArmorDTO() {
        super();
        this.setItemType("Armor");
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public float getArmorDefense() {
        return armorDefense;
    }

    public void setArmorDefense(float armorDefense) {
        this.armorDefense = armorDefense;
    }

    public String getArmorName() {
        return getItemName();
    }
}