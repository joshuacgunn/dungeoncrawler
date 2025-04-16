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
                shopsInTown.add(new Shop(Shop.ShopType.TAVERN, UUID.randomUUID(), new NPC(new Faker().name().firstName(), UUID.randomUUID())));
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
            Shop shop = new Shop(shopToMake, UUID.randomUUID(), npc);
            npc.setCurrentLocation(shop.locationUUID);
            shops.add(shop);
            i++;
        }
        return shops;
    }

    public static String generateTownName() {
        Random rand = new Random();
        float nameChance = rand.nextFloat();
        Faker faker = new Faker();

        if (nameChance < 0.25f) {
            return faker.pokemon().location();
        } else if (nameChance < .5f) {
            return faker.gameOfThrones().city();
        } else if (nameChance < .75f) {
            return faker.witcher().location();
        } else {
            return faker.lordOfTheRings().location();
        }
    }
}
