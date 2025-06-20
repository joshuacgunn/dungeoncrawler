package io.game.core.dto;

import io.game.core.item.Potion;

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
