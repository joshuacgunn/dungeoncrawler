package com.github.joshuacgunn.core.location;

import com.github.joshuacgunn.core.entity.NPC;

import java.util.UUID;

public class Shop extends Location {
    private NPC shopOwner;

    public Shop(String name, UUID uuid, NPC shopOwner) {
        super(name, uuid);
        this.shopOwner = shopOwner;
    }

    public NPC getShopOwner() {
        return this.shopOwner;
    }

}
