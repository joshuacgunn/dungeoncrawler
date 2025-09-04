package io.github.joshuacgunn.core.item;

import java.util.*;

/**
 * Abstract base class representing an item in the game world.
 * Provides common functionality for all item types and manages
 * a global registry of all items.
 */
public class Item {
    /** Global registry mapping UUIDs to all created items */
    public static Map<UUID, Item> itemMap = new HashMap<>();
    protected String itemName;
    private UUID itemUUID;
    public boolean isEquippable = false;
    protected float itemValue;
    protected ItemRarity itemRarity;
    public enum ItemRarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        MYTHICAL,
        DEMONIC,
    }

    /**
     * Creates a new item and registers it in the global item map.
     *
     * @param itemName The name of the item
     * @param itemUUID The unique identifier for the item
     */
    public Item(String itemName, UUID itemUUID) {
        this.itemName = itemName;
        this.itemUUID = itemUUID;
        itemMap.put(itemUUID, this);
    }

    /**
     * Gets the name of this item.
     *
     * @return The name of this item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets the unique identifier of this item.
     *
     * @return The UUID of this item
     */
    public UUID getItemUUID() {
        return itemUUID;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public ItemRarity getItemRarity() {
        return this.itemRarity;
    }

    /**
     * Returns a list of all registered items.
     *
     * @return A list containing all items
     */
    public static List<Item> getItems() {
        return itemMap.values().stream().toList();
    }

    public float getItemValue() {
        return this.itemValue;
    }

    public void setItemValue(float value) {
        this.itemValue = value;
    }

    /**
     * Returns a filtered list of items of the specified type.
     *
     * @param <T> The specific item type to filter by
     * @param itemClass The class object representing the item type
     * @return A list containing only items of the specified type
     */
    public static <T extends Item> List<T> getItemsByType(Class<T> itemClass) {
        return itemMap.values().stream().filter(itemClass::isInstance).map(itemClass::cast).toList();
    }
}
