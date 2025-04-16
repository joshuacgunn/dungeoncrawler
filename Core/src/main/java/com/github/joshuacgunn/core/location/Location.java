package com.github.joshuacgunn.core.location;

import java.util.*;

/**
 * Abstract base class representing a location in the game world.
 * Provides common functionality for all location types and manages
 * a global registry of all locations.
 */
public abstract class Location {
    /** The name of this location */
    protected String locationName;

    /** Unique identifier for this location */
    protected UUID locationUUID;

    /** Global registry mapping UUIDs to all created locations */
    public static Map<UUID, Location> locationMap = new HashMap<>();

    /**
     * Creates a new location and registers it in the global location map.
     *
     * @param name The name of the location
     * @param uuid The unique identifier for the location
     */
    public Location(String name, UUID uuid) {
        this.locationName = name;
        this.locationUUID = uuid;
        locationMap.put(uuid, this);
    }

    /**
     * Returns a list of all registered locations.
     *
     * @return A list containing all locations
     */
    public static List<Location> getLocations() {
        return locationMap.values().stream().toList();
    }

    /**
     * Returns a filtered list of locations of the specified type.
     *
     * @param <T> The specific location type to filter by
     * @param locationClass The class object representing the location type
     * @return A list containing only locations of the specified type
     */
    public static <T extends Location> List<T> getLocationsByType(Class<T> locationClass) {
        return locationMap.values().stream().filter(locationClass::isInstance).map(locationClass::cast).toList();
    }

    /**
     * Gets the unique identifier of this location.
     *
     * @return The UUID of this location
     */
    public UUID getLocationUUID() {
        return locationUUID;
    }

    /**
     * Gets the name of this location.
     *
     * @return The name of this location
     */
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}