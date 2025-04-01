package com.github.joshuacgunn.core.location;

import java.util.UUID;

/**
 * Represents a floor within a dungeon in the game world.
 * Each floor is identified by a unique UUID and has a specific floor number
 * within its parent dungeon.
 */
public class DungeonFloor {
    /** Unique identifier for this dungeon floor */
    private final UUID floorUUID;

    /** The number of this floor within its parent dungeon */
    private final int floorNumber;

    /** Reference to the parent dungeon that contains this floor */
    private final Dungeon parentDungeon;

    /**
     * Creates a new dungeon floor with a specified UUID.
     *
     * @param floorUUID The unique identifier for this floor
     * @param parentDungeon The dungeon that contains this floor
     * @param floorNumber The number of this floor within the dungeon
     */
    public DungeonFloor(UUID floorUUID, Dungeon parentDungeon, int floorNumber) {
        this.floorUUID = floorUUID;
        this.floorNumber = floorNumber;
        this.parentDungeon = parentDungeon;
    }

    /**
     * Gets the unique identifier of this floor.
     *
     * @return The UUID of this floor
     */
    public UUID getFloorUUID() {
        return floorUUID;
    }

    /**
     * Gets the floor number of this dungeon floor.
     *
     * @return The floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Gets the parent dungeon that contains this floor.
     *
     * @return The parent dungeon
     */
    public Dungeon getParentDungeon() {
        return parentDungeon;
    }
}