package com.joshuacgunn.core.location;

import com.joshuacgunn.core.entity.Goblin;

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
        ArrayList<Shop> shops = new ArrayList<>();
        for (int i = 0; i < shopCount; i++) {
            Shop shop = new Shop("Blacksmith", UUID.randomUUID(), new Goblin(UUID.randomUUID()));
            shops.add(shop);
        }
        return shops;
    }
}
