package io.github.joshuacgunn.item;

import io.github.joshuacgunn.container.Chest;
import io.github.joshuacgunn.container.Container;
import io.github.joshuacgunn.container.Inventory;
import io.github.joshuacgunn.entity.Entity;
import io.github.joshuacgunn.entity.NPC;

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
        BRONZE(0.8f),
        IRON(1.0f),
        STEEL(1.2f),
        MITHRIL(1.5f),
        CELESTIUM(1.8f),
        DEMONITE(2.0f);

        public final float baseDefenseMultiplier;

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
        EXQUISITE(34f, 8),
        MASTERWORK(36f, 9),
        HEAVENLY(38f, 10),
        HELLMADE(40f, 11);

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
    public Armor(UUID itemUUID, ArmorSlot slot, String name, ItemRarity itemRarity, boolean isNew) {
        super(name, itemUUID);
        this.armorSlot = slot;
        this.itemRarity = itemRarity;
        this.isEquippable = true;
        if (isNew) {
            updateArmor();
        }
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

    public void setArmorMaterial(ArmorMaterial material) {
        this.armorMaterial = material;
    }

    public void setArmorQuality(ArmorQuality quality) {
        this.armorQuality = quality;
    }

    public void setArmorDefense(float value) {
        this.armorDefense = value;
    }

    /**
     * Updates the armor's defense value based on its quality and slot.
     * Should be called after changing either quality or slot.
     */
    public void updateArmor() {
        updateQualityMaterial();
        this.armorDefense = calculateDefense();
    }

    /**
     * Updates the armor's quality and material based on its rarity.
     * Higher rarity items get better quality and material, with a small chance
     * of getting an exceptional quality upgrade.
     */
    public void updateQualityMaterial() {
        Random random = new Random();
        float extraQualityChance = random.nextFloat();

        switch (this.itemRarity) {
            case COMMON:
                this.itemValue = random.nextFloat(10f, 15f);
                if (extraQualityChance < .9f) {
                    this.armorQuality = ArmorQuality.values()[random.nextInt(0, 2)]; // FLIMSY or WORN
                } else {
                    this.armorQuality = ArmorQuality.DECENT;
                }
                this.armorMaterial = ArmorMaterial.LEATHER;
                break;
            case UNCOMMON:
                this.itemValue = random.nextFloat(20f, 30f);
                if (extraQualityChance < .15f) {
                    this.armorQuality = ArmorQuality.WORN;
                } else if (extraQualityChance < .9f) {
                    this.armorQuality = ArmorQuality.DECENT;
                } else {
                    this.armorQuality = ArmorQuality.STURDY;
                }
                this.armorMaterial = ArmorMaterial.BRONZE;
                break;
            case RARE:
                this.itemValue = random.nextFloat(35f, 45f);
                if (extraQualityChance < .9f) {
                    this.armorQuality = ArmorQuality.values()[random.nextInt(3, 4)]; // DECENT or STURDY
                } else {
                    this.armorQuality = ArmorQuality.HARDENED;
                }
                this.armorMaterial = ArmorMaterial.IRON;
                break;
            case EPIC:
                this.itemValue = random.nextFloat(50f, 60f);
                if (extraQualityChance < .9f) {
                    this.armorQuality = ArmorQuality.values()[4]; // HARDENED
                } else {
                    this.armorQuality = ArmorQuality.EXQUISITE;
                }
                this.armorMaterial = ArmorMaterial.STEEL;
                break;
            case LEGENDARY:
                this.itemValue = random.nextFloat(65f, 75f);
                if (extraQualityChance < .9f) {
                    this.armorQuality = ArmorQuality.values()[5]; // FINE
                } else {
                    this.armorQuality = ArmorQuality.MASTERWORK;
                }
                this.armorMaterial = ArmorMaterial.MITHRIL;
                break;
            case MYTHICAL:
                this.itemValue = random.nextFloat(200f, 225f);
                this.armorQuality = ArmorQuality.HEAVENLY;
                this.armorMaterial = ArmorMaterial.CELESTIUM;
                break;
            case DEMONIC:
                this.itemValue = random.nextFloat(300f, 350f);
                this.armorQuality = ArmorQuality.HELLMADE;
                this.armorMaterial = ArmorMaterial.DEMONITE;
                break;
            case null, default:
                this.armorMaterial = ArmorMaterial.LEATHER;
                this.armorQuality = ArmorQuality.FLIMSY;
        }
    }

    /**
     * Calculates the total defense value for this armor piece based on its quality
     * and slot type.
     *
     * @return The calculated defense value for this armor piece
     */
    private float calculateDefense() {
        // Base defense from quality, adjusted by slot multiplier
        float baseDefense = this.armorQuality.defenseValue * this.armorSlot.defenseMult;

        // Add small random variation (±10%) to make identical armor pieces slightly different
        float variationFactor = 0.6f + (rand.nextFloat() * 0.2f);

        return Math.round(((baseDefense * this.armorMaterial.baseDefenseMultiplier) * variationFactor));
    }

    private float calculateDurability() {
        float baseDurability = armorQuality.durabilityMultiplier * armorMaterial.baseDefenseMultiplier;
        return 0.5f;
    }


    /**
     * Generates a random armor piece with the specified rarity and adds it to a container.
     *
     * @param rarity The desired rarity for the generated armor
     * @param container The container to add the armor to
     * @return The generated armor piece
     */
    public static Armor generateArmor(ItemRarity rarity, Container container, boolean equipArmor) {
        final Random rand = new Random();
        Entity entity = null;

        if (container instanceof Inventory inventory) {
            entity = inventory.getOwner();
        }
        Armor.ArmorSlot slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];

        Armor generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", rarity, true);

        if (equipArmor && entity != null && entity.armors.containsKey(generatedArmor.getArmorSlot())) {
            while (entity.armors.containsKey(generatedArmor.getArmorSlot())) {
                Item.itemMap.remove(generatedArmor.getItemUUID());
                slot = Armor.ArmorSlot.values()[rand.nextInt(0, 4)];
                generatedArmor = new Armor(UUID.randomUUID(), slot, "Generated Armor", rarity, true);
            }
        }

        if (entity != null) {
            if (!(entity instanceof NPC)) {
                generatedArmor.setItemName(entity.getEntityName() + "'s " + generatedArmor.armorQuality.name().toLowerCase() + " " + generatedArmor.armorMaterial.name().toLowerCase() + " " + generatedArmor.getArmorSlot().name().toLowerCase());
                entity.getInventory().addItem(generatedArmor);
                if (equipArmor) {
                    entity.equipArmor(generatedArmor);
                }
            } else {
                generatedArmor.setItemName(generatedArmor.armorQuality.name().toLowerCase() + " " + generatedArmor.armorMaterial.name().toLowerCase() + " " + generatedArmor.getArmorSlot().name().toLowerCase());
                entity.getInventory().addItem(generatedArmor);
            }
        } else if (container instanceof Chest) {
            generatedArmor.setItemName(generatedArmor.armorQuality.name().toLowerCase() + " " + generatedArmor.armorMaterial.name().toLowerCase() + " armor");
            container.addItem(generatedArmor);
        }
        return generatedArmor;
    }
}
