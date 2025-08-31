package io.github.joshuacgunn.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.github.joshuacgunn.entity.Entity;

public class Potion extends Item {

    private PotionType potionType;
    private int restoreAmount;
    private List<Entity.StatusEffect> potionEffects;
    private Random rand = new Random();

    public enum PotionType {
        HEALING("Healing potion"),
        MANA("Mana restoration potion"),
        ALCOHOL("Alcohol"),
        STATCHANGING("Elixir");

        private final String name;

        PotionType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public interface PotionEffect {
        String applyEffect(Entity entity);
    }

    /**
     * Creates a new item and registers it in the global item map.
     *
     * @param potionType The type of potion to create
     * @param itemUUID   The unique identifier for the item
     */
    public Potion(UUID itemUUID, PotionType potionType, ItemRarity itemRarity, boolean isNew) {
        super(potionType.name, itemUUID);
        this.potionType = potionType;
        this.itemRarity = itemRarity;
        this.potionEffects = new ArrayList<>();

        if (isNew) {
            generatePotionEffects();
        }
    }

    public PotionType getPotionType() {
        return potionType;
    }

    private void generatePotionEffects() {
        switch (potionType) {
            case HEALING:
                this.restoreAmount = rand.nextInt(10,(itemRarity.ordinal()+1) * 10);
            case MANA:
                this.restoreAmount = rand.nextInt(10,(itemRarity.ordinal()+1) * 10);
        }
    }

    public static void consumePotion(Entity entity, Entity.StatusEffect effect) {
        switch (effect) {
            case ALCOHOL:
                entity.addStatusEffect(Entity.StatusEffect.CHARISMA_POTION, 60000);
                entity.addStatusEffect(Entity.StatusEffect.STRENGTH_POTION, 60000);
        }
    }
}
