package io.game.core.dto;

import io.game.core.location.Shop;

import java.util.List;
import java.util.UUID;

public class ShopDTO {
    public String shopName;
    public UUID shopUUID;
    public UUID shopOwner;
    public Shop.ShopType shopType;
    public List<UUID> npcsInShop;
    public UUID parentTownUUID;

    public void setParentTownUUID(UUID uuid) {
        this.parentTownUUID = uuid;
    }

    public UUID getParentTownUUID() {
        return this.parentTownUUID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public UUID getShopUUID() {
        return shopUUID;
    }

    public void setShopUUID(UUID shopUUID) {
        this.shopUUID = shopUUID;
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
