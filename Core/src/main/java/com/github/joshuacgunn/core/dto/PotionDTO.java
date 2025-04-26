package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.item.Potion;

public class PotionDTO extends ItemDTO {
    public Potion.PotionType potionType;

    public PotionDTO() {
        super();
        this.setItemType("Potion");
    }

    public Potion.PotionType getPotionType() {
        return potionType;
    }

    public void setPotionType(Potion.PotionType potionType) {
        this.potionType = potionType;
    }
}
