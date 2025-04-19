package com.github.joshuacgunn.core.location;

import com.github.javafaker.Faker;
import com.github.joshuacgunn.core.entity.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Shop extends Location {
    private NPC shopOwner;

    private ShopType shopType;

    private List<NPC> npcsInShop = new ArrayList<>();

    public enum ShopType {
        BLACKSMITH("Blacksmith", 3),
        TAVERN("Tavern", 7),
        EMPORIUM("Emporium", 2);

        public final String name;
        public final int maxNpcCount;

        ShopType(String name, int maxNpcCount) {
            this.name = name;
            this.maxNpcCount = maxNpcCount;
        }
    }

    public Shop(ShopType shopType, UUID uuid, NPC shopOwner, boolean isNew) {
        super(shopType.name, uuid);
        this.shopOwner = shopOwner;
        this.shopType = shopType;
        this.setLocationName(shopOwner.getEntityName() + "'s " + shopType.name);
        if (isNew) {
            this.npcsInShop = generateNPCs();
        }
    }

    public NPC getShopOwner() {
        return this.shopOwner;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public List<NPC> getNpcsInShop() {
        return npcsInShop;
    }

    public void setNpcsInShop(List<NPC> npcsInShop) {
        this.npcsInShop = npcsInShop;
    }

    public List<NPC> generateNPCs() {
        Faker faker = new Faker();

        List<NPC> npcsToReturn = new ArrayList<>();

        Random random = new Random();

        int npcsToGenerate = random.nextInt(1, shopType.maxNpcCount);

        for (int i = 0; i <= npcsToGenerate; i++) {
            NPC npc = new NPC(faker.name().firstName(), UUID.randomUUID());
            npcsToReturn.add(npc);
            npc.setCurrentLocation(this);
        }

        return npcsToReturn;
    }
}
