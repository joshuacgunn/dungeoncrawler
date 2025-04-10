package com.joshuacgunn.core.container;

import com.joshuacgunn.core.item.Item;

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
    private final List<Item> items = new ArrayList<>();

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
     * Gets the name of this container.
     *
     * @return The name of this container
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * Returns a list of all registered containers.
     *
     * @return A list containing all containers
     */
    public List<Container> getContainers() {
        return containerMap.values().stream().toList();
    }

    /**
     * Returns a filtered list of containers of the specified type.
     *
     * @param <T> The specific container type to filter by
     * @param containerClass The class object representing the container type
     * @return A list containing only containers of the specified type
     */
    public <T extends Container> List<T> getContainersByType(Class<T> containerClass) {
        return containerMap.values().stream().filter(containerClass::isInstance).map(containerClass::cast).toList();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}