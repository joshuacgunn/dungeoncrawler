package com.joshuacgunn.core.location;

import com.joshuacgunn.core.entity.Entity;

import java.util.UUID;

public class Shop extends Location {
    private Entity shopOwner;

    public Shop(String name, UUID uuid, Entity shopOwner) {
        super(name, uuid);
        this.shopOwner = shopOwner;
    }

    public Entity getShopOwner() {
        return this.shopOwner;
    }

}
