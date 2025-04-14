package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.item.Armor;

public class ArmorDTO extends ItemDTO {
    private Armor.ArmorSlot slot;
    private Armor.ArmorQuality quality;
    private float armorDefense;

    public ArmorDTO() {
        super();
        this.setItemType("Armor");
    }

    public Armor.ArmorSlot getSlot() {
        return this.slot;
    }

    public void setSlot(Armor.ArmorSlot slot) {
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

    public Armor.ArmorQuality getQuality() {
        return quality;
    }

    public void setQuality(Armor.ArmorQuality quality) {
        this.quality = quality;
    }
}