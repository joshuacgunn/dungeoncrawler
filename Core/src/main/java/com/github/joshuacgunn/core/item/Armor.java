package com.github.joshuacgunn.core.item;

import com.github.joshuacgunn.core.container.Chest;
import com.github.joshuacgunn.core.container.Container;
import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.entity.Entity;

import java.util.UUID;
import java.util.Random;

public class Armor extends Item {
    private float armorDefense;

    /**
     * Enum representing various types of armor materials with associated
     * base defense multipliers. These materials factor into the calculation
     * of the total defense value of an armor piece.
     */
    public enum ArmorMaterial {
        LEATHER(0.5f),
        BONE(0.8f),
        IRON(1.0f),
        STEEL(1.2f),
        MITHRIL(1.5f),
        CELESTIUM(1.8f);

        private final float baseDefenseMultiplier;

        ArmorMaterial(float baseDefenseMultiplier) {
            this.baseDefenseMultiplier = baseDefenseMultiplier;
        }
    }

    /**
     * Represents the quality tier of a piece of armor, determining its
     * defensive capabilities and durability multiplier.
     * Each quality tier has a predefined defense value and a durability multiplier.
     */
    public enum ArmorQuality {
        FLIMSY(5f, 1),
        WORN(7f, 3),
        DECENT(14f, 4),
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

    /**
     * The ArmorSlot enum represents the different types of slots available for armor pieces
     * in the game, each associated with a specific defense multiplier. This multiplier is used
     * to calculate the contribution of the armor piece to the overall defense value.
     */
    public enum ArmorSlot {
        HELMET(1.1f),
        CHESTPLATE(1.8f),
        LEGGINGS(1.6f),
        BOOTS(1.1f);

        public final float defenseMult;

        ArmorSlot(float defenseMult) {
            this.defenseMult = defenseMult;
        }
    }
    private ArmorSlot armorSlot;
    private ArmorQuality armorQuality;
    private ArmorMaterial armorMaterial;
    Random rand = new Random();
    /**
     * Creates a new item and registers it in the global item map.
     *
     * @param slot         the armorSlot of the armor piece
     * @param itemUUID     The unique identifier for the item
     */
    public Armor(UUID itemUUID, ArmorSlot slot, String name, ArmorQuality armorQuality, ArmorMaterial armorMaterial) {
        super(name, itemUUID);
        this.armorSlot = slot;
        this.armorQuality = armorQuality;
        this.armorMaterial = armorMaterial;
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

    public ArmorMaterial getArmorMaterial() {
        return armorMaterial;
    }

    public void setArmorDefense(float value) {
        this.armorDefense = value;
    }

    /**
     * Updates the armor's defense value based on its quality and slot.
     * Should be called after changing either quality or slot.
     */
    public void updateArmorDefense() {
        this.armorDefense = calculateDefense();
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

        return Math.round(((baseDefense * armorMaterial.baseDefenseMultiplier) * variationFactor));
    }

    private float calculateDurability() {
        float baseDurability = armorQuality.durabilityMultiplier * armorMaterial.baseDefenseMultiplier;
        return 0.5f;
    }


    public static Armor generateArmor(int minQuality, int maxQuality, Container container) {
        final Random rand = new Random();
        Entity entity = null;
        if (container instanceof Inventory inventory) {
            entity = inventory.getOwner();
        }
        Armor.ArmorQuality qualityToUse = Armor.ArmorQuality.values()[rand.nextInt(minQuality, maxQuality +1)];

        Armor.ArmorMaterial materialToUse;

        if (qualityToUse.ordinal() < ArmorQuality.STURDY.ordinal()) {
            materialToUse = ArmorMaterial.values()[rand.nextInt(3)];
        } else if (qualityToUse.ordinal() < ArmorQuality.EXQUISITE.ordinal()) {
            materialToUse = ArmorMaterial.values()[2 + rand.nextInt(2)];
        } else {
            materialToUse = ArmorMaterial.values()[4 + rand.nextInt(2)];
        }

        Armor.ArmorSlot slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];

        Armor generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", qualityToUse, materialToUse);

        if (entity != null && entity.armors.containsKey(generatedArmor.getArmorSlot())) {
            Item.itemMap.remove(generatedArmor);
            while (entity.armors.containsKey(generatedArmor.getArmorSlot())) {
                if (entity.armors.containsKey(generatedArmor.getArmorSlot())) {
                    Item.itemMap.remove(generatedArmor);
                }
                Item.itemMap.remove(generatedArmor.getItemUUID(), generatedArmor);
                slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];
                generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", qualityToUse, materialToUse);
            }
        }

        if (entity != null) {
                generatedArmor.setItemName(entity.getEntityName() + "'s " + generatedArmor.armorQuality.name().toLowerCase() + " " + generatedArmor.armorMaterial.name().toLowerCase() + " " + generatedArmor.getArmorSlot().name().toLowerCase());
                entity.getInventory().addItem(generatedArmor);
                entity.equipArmor(generatedArmor);
        } else if (container instanceof Chest) {
            generatedArmor.setItemName(generatedArmor.armorQuality.name().toLowerCase() + " " + generatedArmor.armorMaterial.name().toLowerCase() + " armor");
            container.addItem(generatedArmor);
        }
        return generatedArmor;
    }
}
