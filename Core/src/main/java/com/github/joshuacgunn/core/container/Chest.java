package com.github.joshuacgunn.core.container;

import java.util.UUID;

public class Chest extends Container{
    private boolean locked;
    private UUID keyUUID;
    private ChestRarity chestRarity;

    public enum ChestRarity {
        COMMON(0.5f),
        UNCOMMON(1.0f),
        RARE(1.5f),
        EPIC(2.0f),
        LEGENDARY(2.5f);

        public final float lootMultiplier;

        ChestRarity(float lootMultiplier) {
            this.lootMultiplier = lootMultiplier;
        }
    }

    public Chest(ChestRarity chestRarity, boolean locked) {
        super(UUID.randomUUID(),  "Chest");
        this.containerName = chestRarity + " Chest";
        this.chestRarity = chestRarity;
        this.locked = locked;
    }
}
