package com.github.joshuacgunn.core.location;

import java.util.UUID;

public class World extends Location {

    /**
     * Creates a new location and registers it in the global location map.
     *
     * @param name The name of the location
     * @param uuid The unique identifier for the location
     */
    public World(UUID uuid) {
        super("World", uuid);
    }
}
