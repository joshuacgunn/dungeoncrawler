package io.github.joshuacgunn.core.dto;

import io.github.joshuacgunn.core.item.Armor;

public class ArmorDTO extends ItemDTO {
    private Armor.ArmorSlot armorSlot;
    private Armor.ArmorQuality armorQuality;
    private Armor.ArmorMaterial armorMaterial;
    private float armorDefense;

    public ArmorDTO() {
        super();
        this.setItemType("Armor");
    }

    public Armor.ArmorMaterial getArmorMaterial() {
        return armorMaterial;
    }

    public void setArmorMaterial(Armor.ArmorMaterial armorMaterial) {
        this.armorMaterial = armorMaterial;
    }

    public Armor.ArmorSlot getArmorSlot() {
        return this.armorSlot;
    }

    public void setArmorSlot(Armor.ArmorSlot armorSlot) {
        this.armorSlot = armorSlot;
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

    public Armor.ArmorQuality getArmorQuality() {
        return armorQuality;
    }

    public void setArmorQuality(Armor.ArmorQuality armorQuality) {
        this.armorQuality = armorQuality;
    }
}
