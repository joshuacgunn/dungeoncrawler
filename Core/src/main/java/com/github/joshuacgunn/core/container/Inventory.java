package com.github.joshuacgunn.core.container;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.item.Item;

import java.util.UUID;

/**
 * Represents an inventory system that extends the Container class to manage items for entities.
 * Each inventory is uniquely associated with a parent entity and provides methods for item management.
 */
public class Inventory extends Container {

    /**
     * The entity that owns this inventory.
     */
    private final Entity parentEntity;

    /**
     * Creates a new Inventory instance for a specific entity.
     *
     * @param containerUUID The unique identifier for this inventory container
     * @param parentEntity The entity that owns this inventory
     */
    public Inventory(UUID containerUUID, Entity parentEntity) {
        super(containerUUID, (parentEntity + "'s Inventory"));
        this.parentEntity = parentEntity;
    }

    /**
     * Retrieves the entity that owns this inventory.
     *
     * @return The parent entity that owns this inventory
     */
    public Entity getOwner() {
        return parentEntity;
    }

    /**
     * Removes a specific item from the inventory if it exists.
     * This method can be used to simulate dropping or discarding items.
     *
     * @param item The item to be removed from the inventory
     */
    public void dropItem(Item item) {
        if (this.getItems().contains(item)) {
            this.items.remove(item);
        }
    }
}