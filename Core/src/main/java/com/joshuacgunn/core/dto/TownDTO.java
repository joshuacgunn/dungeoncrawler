package com.joshuacgunn.core.dto;

import java.util.ArrayList;
import java.util.UUID;

public class TownDTO {
    private String townName;
    private UUID townUUID;
    private ArrayList<ShopDTO> shopsInTown = new ArrayList<>();
    private int shopCount;

    public TownDTO() { }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public ArrayList<ShopDTO> getShopsInTown() {
        return shopsInTown;
    }

    public void setShopsInTown(ArrayList<ShopDTO> shopsInTown) {
        this.shopsInTown = shopsInTown;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public UUID getTownUUID() {
        return this.townUUID;
    }

    public void setTownUUID(UUID uuid) {
        this.townUUID = uuid;
    }
}
