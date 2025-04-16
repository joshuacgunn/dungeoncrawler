package com.github.joshuacgunn.core.location;

import com.github.javafaker.Faker;
import com.github.joshuacgunn.core.entity.NPC;

import java.util.ArrayList;
import java.util.UUID;

public class Town extends Location {
    private int shopCount;
    private ArrayList<Shop> shopsInTown = new ArrayList<>();

    public Town(String name, UUID uuid, int shopCount, boolean isNew) {
        super(name, uuid);
        this.shopCount = shopCount;
        if (isNew) {
            this.shopsInTown = generateShops();
        }
    }

    public ArrayList<Shop> getShopsInTown() {
        return shopsInTown;
    }

    public int getShopCount() {
        return this.shopCount;
    }

    public void setShopsInTown(ArrayList<Shop> shopsInTown) {
        this.shopsInTown = shopsInTown;
    }

    public ArrayList<Shop> generateShops() {
        Faker faker = new Faker();
        ArrayList<Shop> shops = new ArrayList<>();
        for (int i = 0; i < shopCount; i++) {
            NPC npc = new NPC(faker.name().firstName(), UUID.randomUUID());
            Shop shop = new Shop(Shop.ShopType.BLACKSMITH, UUID.randomUUID(), npc); // Blacksmith is a placeholder name
            npc.setCurrentLocation(shop.locationUUID);
            shops.add(shop);
        }
        return shops;
    }
}
