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

    public Town(String name, UUID uuid, boolean isNew) {
        super(name, uuid);
        if (isNew) {
            this.shopCount = Math.max(1, new Random().nextInt(Shop.ShopType.values().length)+1);
            this.shopsInTown = generateShops();
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
}
