package com.github.joshuacgunn.core.location;

import com.github.javafaker.Faker;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Town extends Location {
    private int shopCount;
    private ArrayList<Shop> shopsInTown = new ArrayList<>();

    public Town(UUID uuid, boolean isNew) {
        super(generateTownName(), uuid);
        if (isNew) {
            this.shopCount = Math.max(1, new Random().nextInt(Shop.ShopType.values().length)+1);
            // If shopcount is only 1, generate a tavern. Wouldn't want to live somewhere you couldn't drink right?
            if (shopCount == 1) {
                NPC npc = new NPC(new Faker().name().firstName(), UUID.randomUUID());
                Shop shop = new Shop(Shop.ShopType.TAVERN, UUID.randomUUID(), npc, true);
                npc.setCurrentLocation(shop);
                shopsInTown.add(shop);
            } else {
                this.shopsInTown = generateShops();
            }
        }
    }

    public ArrayList<Shop> getShopsInTown() {
        return shopsInTown;
    }

    public int getShopCount() {
        return this.shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public void setShopsInTown(ArrayList<Shop> shopsInTown) {
        this.shopsInTown = shopsInTown;
    }

    public ArrayList<Shop> generateShops() {
        Faker faker = new Faker();
        ArrayList<Shop> shops = new ArrayList<>();
        int i = 0;
        outerloop: while (i < shopCount) {
            NPC npc = new NPC(faker.name().firstName(), UUID.randomUUID());
            Shop.ShopType shopToMake = Shop.ShopType.values()[new Random().nextInt(Shop.ShopType.values().length)];
            for (Shop shop : shops) {
                if (shop.getShopType() == shopToMake) {
                    Entity.entityMap.remove(npc.getEntityUUID());
                    continue outerloop;
                }
            }
            Shop shop = new Shop(shopToMake, UUID.randomUUID(), npc, true);
            npc.setCurrentLocation(shop);
            shops.add(shop);
            i++;
        }
        return shops;
    }

    public static String generateTownName() {
        Random rand = new Random();
        float nameChance = rand.nextFloat();
        Faker faker = new Faker();
        boolean generated = false;

        outerloop: while (!generated) {
            if (nameChance < 0.25f) {
                String name = faker.pokemon().location();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    if (name.equals(town.getLocationName())) {
                        continue outerloop;
                    }
                }
                generated = true;
                return name;
            } else if (nameChance < .5f) {
                String name = faker.gameOfThrones().city();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    if (name.equals(town.getLocationName())) {
                        continue outerloop;
                    }
                }
                generated = true;
                return name;
            } else if (nameChance < .75f) {
                String name = faker.witcher().location();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    if (name.equals(town.getLocationName())) {
                        continue outerloop;
                    }
                }
                generated = true;
                return name;
            } else {
                String name = faker.lordOfTheRings().location();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    if (name.equals(town.getLocationName())) {
                        continue outerloop;
                    }
                }
                generated = true;
                return name;
            }
        }
        return null;
    }
}
