package com.joshuacgunn.core.container;

import com.joshuacgunn.core.item.Armor;
import com.joshuacgunn.core.item.Item;
import com.joshuacgunn.core.item.Weapon;
import com.joshuacgunn.core.location.DungeonFloor;
import org.reflections.Reflections;

import java.util.*;

public class Chest extends Container{
    private boolean locked;
    private UUID keyUUID;
    private ChestRarity chestRarity;
    private List<Item> chestContents;
    private DungeonFloor parentFloor;

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

    public <T extends Item> List<T> generateItems() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < Math.round(parentFloor.getDifficultyRating()); i++)  {
            items.add(generateItem());
        }
        return items;
    }

    public void setContents(List<Item> contents) {
        this.chestContents = contents;
    }

    public List<Item> getContents() {
        return  this.chestContents;
    }

    @SuppressWarnings("unchecked")
    public <T extends Item> T generateItem() {
        try {
            Reflections reflection = new Reflections("com.joshuacgunn");
            Set<Class<? extends Item>> itemClasses = reflection.getSubTypesOf(Item.class);
            int extendedClasses = 0;
            for (Class<?> itemClass : itemClasses) {
                extendedClasses += 1;
            }

            float generatePercent = (100.0f / extendedClasses) / 100.0f;

            Random rand = new Random();

            float chanceToGenerate = rand.nextFloat();
            if (chanceToGenerate < generatePercent) {
                return (T) Armor.generateArmor(0, 4, this);
            } else if (chanceToGenerate < generatePercent * 2) {
                return (T) Weapon.generateWeapon(0, 4, this);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
