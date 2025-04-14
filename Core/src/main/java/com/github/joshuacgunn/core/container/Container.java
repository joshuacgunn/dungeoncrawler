package com.github.joshuacgunn.core.container;

import com.github.joshuacgunn.core.item.Item;

import java.util.*;

/**
 * Abstract base class representing a container in the game world.
 * Provides common functionality for all container types and manages
 * a global registry of all containers.
 */
public abstract class Container {
    /** Unique identifier for this container */
    protected UUID containerUUID;

    /** The name of this container */
    protected String containerName;

    /** The list of items in this container */
    protected List<Item> items = new ArrayList<>();

    /** Global registry mapping UUIDs to all created containers */
    public static Map<UUID, Container> containerMap = new HashMap<>();

    /**
     * Creates a new container and registers it in the global container map.
     *
     * @param containerUUID The unique identifier for the container
     * @param containerName The name of the container
     */
    public Container(UUID containerUUID, String containerName) {
        this.containerUUID = containerUUID;
        this.containerName = containerName;
        containerMap.put(containerUUID, this);
    }

    /**
     * Gets the unique identifier of this container.
     *
     * @return The UUID of this container
     */
    public UUID getContainerUUID() {
        return containerUUID;
    }

    /**
     * Adds an item to this container.
     *
     * @param item The item to add to the container
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Gets all items currently stored in this container.
     *
     * @return A list containing all items in this container
     */
    public List<Item> getItems() {
        return items;
    }
}