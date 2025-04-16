package com.github.joshuacgunn.core.location;

import com.github.joshuacgunn.core.entity.NPC;

import java.util.UUID;

public class Shop extends Location {
    private NPC shopOwner;

    private ShopType shopType;

    public enum ShopType {
        BLACKSMITH("Blacksmith"),
        TAVERN("Tavern"),
        EMPORIUM("Emporium");

        public final String name;

        ShopType(String name) {
            this.name = name;
        }
    }

    public Shop(ShopType shopType, UUID uuid, NPC shopOwner) {
        super(shopType.name, uuid);
        this.shopOwner = shopOwner;
        this.shopType = shopType;
        this.setLocationName(shopOwner.getEntityName() + "'s " + shopType.name);
    }

    public NPC getShopOwner() {
        return this.shopOwner;
    }

    public ShopType getShopType() {
        return shopType;
    }
}
