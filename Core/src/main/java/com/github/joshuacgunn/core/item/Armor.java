package com.github.joshuacgunn.core.item;

import com.github.joshuacgunn.core.container.Chest;
import com.github.joshuacgunn.core.container.Container;
import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.entity.Entity;

import java.util.UUID;
import java.util.Random;

public class Armor extends Item {
    private float armorDefense;

    public enum ArmorQuality {
        FLIMSY(7f, 1),
        WORN(12f, 3),
        DECENT(15f, 4),
        STURDY(19f, 5),
        HARDENED(26f, 6),
        FINE(30f, 7),
        EXQUISITE(34f, 8),
        MASTERWORK(36f, 9);

        public final float defenseValue;
        public final int durabilityMultiplier;

        ArmorQuality(float defense, int durabilityMultiplier) {
            this.defenseValue = defense;
            this.durabilityMultiplier = durabilityMultiplier;
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
    private float calculateDefense() {
        // Base defense from quality, adjusted by slot multiplier
        float baseDefense = armorQuality.defenseValue * armorSlot.defenseMult;

        // Add small random variation (±10%) to make identical armor pieces slightly different
        float variationFactor = 0.6f + (rand.nextFloat() * 0.2f);

        return Math.round((baseDefense * variationFactor));
    }

    /**
     * Updates the armor's defense value based on its quality and slot.
     * Should be called after changing either quality or slot.
     */
    public void updateArmorDefense() {
        this.armorDefense = calculateDefense();
    }

    public static Armor generateArmor(int minQuality, int maxQuality, Container container) {
        final Random rand = new Random();
        Entity entity = null;
        if (container instanceof Inventory inventory) {
            entity = inventory.getOwner();
        }
        Armor.ArmorQuality qualityToUse = Armor.ArmorQuality.values()[rand.nextInt(minQuality, maxQuality +1)];
        Armor.ArmorSlot slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];
        Armor generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", qualityToUse);
        if (entity != null && entity.armors.containsKey(generatedArmor.getArmorSlot())) {
            while (entity.armors.containsKey(generatedArmor.getArmorSlot())) {
                slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];
                generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", qualityToUse);
            }
        }
        if (entity != null) {
                generatedArmor.setItemName(entity.getEntityName() + "'s " + " " + qualityToUse.name().toLowerCase() + " " + generatedArmor.getArmorSlot().name().toLowerCase());
                entity.getInventory().addItem(generatedArmor);
                entity.equipArmor(generatedArmor);
        } else if (container instanceof Chest){
            container.addItem(generatedArmor);
        }
        return generatedArmor;
    }
}
