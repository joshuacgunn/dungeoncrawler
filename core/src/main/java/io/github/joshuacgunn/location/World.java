package io.github.joshuacgunn.location;

import io.github.joshuacgunn.quest.Quest;
import io.github.joshuacgunn.randomevent.RandomEvent;

import java.util.List;
import java.util.UUID;

public class World extends Location {

    private List<Quest> quests;
    private List<RandomEvent> events;

    /**
     * Creates a new location and registers it in the global location map.
     *
     * @param uuid The unique identifier for the location
     */
    public World(UUID uuid) {
        super("World", uuid);
    }
}