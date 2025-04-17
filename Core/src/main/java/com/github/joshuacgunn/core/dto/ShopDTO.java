package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.location.Shop;

import java.util.List;
import java.util.UUID;

public class ShopDTO {
    private String shopName;
    private UUID ShopUUID;
    private UUID shopOwner;
    private Shop.ShopType shopType;
    private List<UUID> npcsInShop;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public UUID getShopUUID() {
        return ShopUUID;
    }

    public void setShopUUID(UUID shopUUID) {
        this.ShopUUID = shopUUID;
    }

    public UUID getShopOwnerUUID() {
        return shopOwner;
    }

    public void setShopOwnerUUID(UUID shopOwnerUUID) {
        this.shopOwner = shopOwnerUUID;
    }

    public void setShopType(Shop.ShopType shopType) {
        this.shopType = shopType;
    }

    public Shop.ShopType getShopType() {
        return shopType;
    }

    public List<UUID> getNpcsInShop() {
        return npcsInShop;
    }

    public void setNpcsInShop(List<UUID> npcsInShop) {
        this.npcsInShop = npcsInShop;
    }
}
