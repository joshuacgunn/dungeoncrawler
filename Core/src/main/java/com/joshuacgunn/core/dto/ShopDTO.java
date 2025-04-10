package com.joshuacgunn.core.dto;

import java.util.UUID;

public class ShopDTO {
    private String shopName;
    private UUID ShopUUID;
    private UUID shopOwner;

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
}
