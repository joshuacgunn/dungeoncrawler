package com.github.joshuacgunn.core.entity;

import java.util.UUID;
import java.util.Random;

/**
 * Represents a Goblin enemy in the game.
 * Extends the {@link Enemy} class.
 */
public class Goblin extends Enemy {

    /**
     * Constructs a new Goblin enemy with the given UUID.
     *
     * @param uuid The unique identifier for the Goblin.
     */
    public Goblin(UUID uuid) {
        super("Goblin", uuid, new Random().nextFloat(25f, 35f));
    }
}