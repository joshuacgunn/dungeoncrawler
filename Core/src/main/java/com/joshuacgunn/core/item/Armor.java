package com.joshuacgunn.core.item;

import com.joshuacgunn.core.container.Chest;
import com.joshuacgunn.core.container.Container;
import com.joshuacgunn.core.container.Inventory;
import com.joshuacgunn.core.entity.Entity;

import java.util.UUID;
import java.util.Random;

public class Armor extends Item {
    private float armorDefense;

    public enum ArmorQuality {
        FLIMSY(0.7f, 2),
        WORN(1.2f, 5),
        STURDY(1.9f, 7),
        FINE(2.6f, 9),
        EXQUISITE(3.4f, 10),
        MASTERWORK(4.0f, 11);

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
    public float calculateDefense() {
        // Base defense from quality, adjusted by slot multiplier
        float baseDefense = armorQuality.defenseValue * armorSlot.defenseMult;

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

    public static Armor generateArmor(int minQuality, int maxQuality, Container container) {
        final Random rand = new Random();
        Entity entity = null;
        if (container instanceof Inventory inventory) {
            entity = inventory.getOwner();
        }
        Armor.ArmorQuality qualityToUse = Armor.ArmorQuality.values()[rand.nextInt(minQuality, maxQuality +1)];
        Armor.ArmorSlot slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];
        Armor generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", qualityToUse);
        if (entity != null && !(entity.armors.containsKey(generatedArmor.getArmorSlot()))) {
                generatedArmor.setItemName(entity.getEntityName() + "'s " + " " + qualityToUse.name().toLowerCase() + " " + generatedArmor.getArmorSlot().name().toLowerCase());
                System.out.println("Generated armor piece: " + generatedArmor.getArmorName());
                entity.getInventory().addItem(generatedArmor);
                entity.equipArmor(generatedArmor);
        } else if (container instanceof Chest){
            System.out.println("Generated armor piece: " + generatedArmor.getArmorName());
            container.addItem(generatedArmor);
        }
        return generatedArmor;
    }
}
