package io.github.joshuacgunn.core.location;

import com.github.javafaker.Faker;
import io.github.joshuacgunn.core.entity.NPC;
import io.github.joshuacgunn.core.item.Armor;
import io.github.joshuacgunn.core.item.Item;
import io.github.joshuacgunn.core.item.Weapon;
import io.github.joshuacgunn.core.misc.GameMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Represents a shop location in the game world where players can interact with NPCs,
 * buy/sell items, and engage in various shop-specific activities.
 * Extends the Location class to integrate with the game's location system.
 */
public class Shop extends Location {
    /**
     * Defines the different types of shops available in the game world.
     * Each type has specific characteristics and limitations.
     */
    public enum ShopType {
        /** Specializes in weapons and armor selling/buying*/
        BLACKSMITH("Blacksmith", 3),
        /** A social hub with food, drinks, and information */
        TAVERN("Tavern", 7),
        /** Magic shop with various magical items */
        EMPORIUM("Emporium", 2),
        /** General store with various merchandise */
        GENERAL("General Store", 5);

        /** The display name of the shop type */
        public final String name;
        /** Maximum number of NPCs that can be present in this type of shop */
        public final int maxNpcCount;

        /**
         * Creates a new shop type with specified parameters.
         *
         * @param name The display name of the shop type
         * @param maxNpcCount Maximum number of NPCs allowed in this shop type
         */
        ShopType(String name, int maxNpcCount) {
            this.name = name;
            this.maxNpcCount = maxNpcCount;
        }
    }

    private Town parentTown;

    /** The NPC who owns and operates the shop */
    private NPC shopOwner;

    /** The type of this shop */
    private ShopType shopType;

    /** List of NPCs currently present in the shop */
    private List<NPC> npcsInShop = new ArrayList<>();

    /**
     * Creates a new shop instance.
     *
     * @param shopType The type of shop to create
     * @param uuid Unique identifier for this shop
     * @param shopOwner The NPC who owns this shop
     * @param isNew Whether this is a new shop instance (true) or loaded from save (false)
     */
    public Shop(ShopType shopType, UUID uuid, NPC shopOwner, boolean isNew, Town parentTown) {
        super(shopType.name, uuid);
        this.shopOwner = shopOwner;
        this.shopType = shopType;
        this.parentTown = parentTown;
        this.setLocationName(shopOwner.getEntityName() + "'s " + shopType.name);
        if (isNew) {
            this.shopOwner.setCurrentLocation(this);
            generateVendorItems();
            this.npcsInShop = generateNPCs();
            for (NPC npc : GameMethods.generateUniqueNPCs(this)) {
                if (this.npcsInShop.contains(npc)) {
                    continue;
                }
                this.npcsInShop.add(npc);
                npc.setCurrentLocation(this);
            }
        }
    }

    public Town getParentTown() {
        return this.parentTown;
    }

    /**
     * Gets the NPC who owns this shop.
     *
     * @return The shop owner NPC
     */
    public NPC getShopOwner() {
        return this.shopOwner;
    }

    /**
     * Gets the type of this shop.
     *
     * @return The shop's type
     */
    public ShopType getShopType() {
        return shopType;
    }

    /**
     * Gets the list of NPCs currently in the shop.
     *
     * @return List of NPCs present in the shop
     */
    public List<NPC> getNpcsInShop() {
        return npcsInShop;
    }

    /**
     * Sets the list of NPCs in the shop.
     * Useful for loading saved game states.
     *
     * @param npcsInShop List of NPCs to set as present in the shop
     */
    public void setNpcsInShop(List<NPC> npcsInShop) {
        this.npcsInShop = npcsInShop;
    }

    /**
     * Generates a random number of NPCs for the shop, up to the maximum allowed
     * for the shop type. Each NPC is created with a random name and unique ID.
     *
     * @return List of newly generated NPCs
     */
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

    public List<Item> generateVendorItems() {
        List<Item> items = new ArrayList<>();
        Random random = new Random();
        // Common 3-4
        // Uncommon 2-3
        // Rare 1-2
        // Epic 0-1
        if (this.shopType.equals(ShopType.BLACKSMITH)) {
            for (int i = 0; i < random.nextInt(3, 4); i++) {
                float weaponOrArmorChance = random.nextFloat();
                if (weaponOrArmorChance < .5f) {
                    items.add(Weapon.generateWeapon(Item.ItemRarity.COMMON, this.shopOwner.getInventory()));
                } else {
                    items.add(Armor.generateArmor(Item.ItemRarity.COMMON, this.shopOwner.getInventory(), false));
                }
            }
            for (int i = 0; i < random.nextInt(2, 3); i++) {
                float weaponOrArmorChance = random.nextFloat();
                if (weaponOrArmorChance < .5f) {
                    items.add(Weapon.generateWeapon(Item.ItemRarity.UNCOMMON, this.shopOwner.getInventory()));
                } else {
                    items.add(Armor.generateArmor(Item.ItemRarity.UNCOMMON, this.shopOwner.getInventory(), false));
                }
            }
            for (int i = 0; i < random.nextInt(1, 2); i++) {
                float weaponOrArmorChance = random.nextFloat();
                if (weaponOrArmorChance < .5f) {
                    items.add(Weapon.generateWeapon(Item.ItemRarity.RARE, this.shopOwner.getInventory()));
                } else {
                    items.add(Armor.generateArmor(Item.ItemRarity.RARE, this.shopOwner.getInventory(), false));
                }
            }
            for (int i = 0; i < random.nextInt(0, 1); i++) {
                float weaponOrArmorChance = random.nextFloat();
                if (weaponOrArmorChance < .5f) {
                    items.add(Weapon.generateWeapon(Item.ItemRarity.EPIC, this.shopOwner.getInventory()));
                } else {
                    items.add(Armor.generateArmor(Item.ItemRarity.EPIC, this.shopOwner.getInventory(), false));
                }
            }
        } else if (this.shopType.equals(ShopType.TAVERN)) {

        }
        return items;
    }
}
