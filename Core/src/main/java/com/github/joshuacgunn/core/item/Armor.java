package com.github.joshuacgunn.core.item;

import java.util.UUID;
import java.util.Random;

public class Armor extends Item {
    private float armorDefense;

    public enum ArmorQuality {
        FLIMSY(0.7f),
        WORN(1.2f),
        STURDY(1.9f),
        FINE(2.6f),
        EXQUISITE(3.4f),
        MASTERWORK(4.0f);

        public final float defense;

        ArmorQuality(float defense) {
            this.defense = defense;
        }
    }
    public enum ArmorSlot {
        HELMET(1.3f),
        CHESTPLATE(1.8f),
        LEGGINGS(1.6f),
        BOOTS(1.2f);

        public final float defenseMult;

        ArmorSlot(float defenseMult) {
            this.defenseMult = defenseMult;
        }
    }
    public ArmorSlot armorSlot;
    public ArmorQuality armorQuality;
    Random rand = new Random();
    /**
     * Creates a new item and registers it in the global item map.
     *
     * @param slot         the armorSlot of the armor piece
     * @param itemUUID     The unique identifier for the item
     */
    public Armor(UUID itemUUID, ArmorSlot slot, String name, ArmorQuality armorQuality) {
        super(name, itemUUID);
        this.armorSlot = slot;
        this.armorQuality = armorQuality;
        updateArmorDefense();
    }

    public float getArmorDefense() {
        return armorDefense;
    }

    public ArmorSlot getArmorSlot() {
        return armorSlot;
    }

    public String getArmorName() {
        return getItemName();
    }

    public ArmorQuality getArmorQuality() {
        return armorQuality;
    }

    public void setArmorDefense(float value) {
        this.armorDefense = value;
    }

    /**
     * Calculates the total defense value for this armor piece based on its quality
     * and slot type.
     *
     * @return The calculated defense value for this armor piece
     */
    public float calculateDefense() {
        // Base defense from quality, adjusted by slot multiplier
        float baseDefense = armorQuality.defense * armorSlot.defenseMult;

        // Add small random variation (±10%) to make identical armor pieces slightly different
        float variationFactor = 0.6f + (rand.nextFloat() * 0.2f);

        return Math.round(10*(baseDefense * variationFactor));
    }

    /**
     * Updates the armor's defense value based on its quality and slot.
     * Should be called after changing either quality or slot.
     */
    public void updateArmorDefense() {
        this.armorDefense = calculateDefense();
    }
}
