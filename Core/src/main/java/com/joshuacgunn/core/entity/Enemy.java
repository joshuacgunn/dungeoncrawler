package com.joshuacgunn.core.entity;


import java.util.UUID;

/**
 * Represents an abstract enemy entity in the game.
 * This class serves as a base for all specific enemy types.
 */
public abstract class Enemy extends Entity {

    /**
     * Constructs a new enemy entity with the given name, UUID, and initial hit points.
     *
     * @param name The name of the enemy.
     * @param uuid The unique identifier for the enemy.
     * @param hp   The initial hit points of the enemy.
     */
    public Enemy(String name, UUID uuid, float hp) {
        super(name, uuid);
        this.entityHp = hp;
    }
}
