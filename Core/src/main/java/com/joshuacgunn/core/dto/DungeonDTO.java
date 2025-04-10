package com.joshuacgunn.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DungeonDTO implements Serializable {
    private String dungeonName;
    private float difficultyRating;
    private UUID dungeonUUID;
    private UUID currentFloorUUID;
    private List<DungeonFloorDTO> floors = new ArrayList<>();

    public DungeonDTO() { }

    public List<DungeonFloorDTO> getFloors() {
        return floors;
    }

    public float getDifficultyRating() {
        return this.difficultyRating;
    }

    public void setDifficultyRating(float rating) {
        this.difficultyRating = rating;
    }

    public void setFloors(List<DungeonFloorDTO> floors) {
        this.floors = floors;
    }

    public UUID getCurrentFloorUUID() {
        return currentFloorUUID;
    }

    public void setCurrentFloorUUID(UUID currentFloorUUID) {
        this.currentFloorUUID = currentFloorUUID;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public void setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
    }

    public UUID getDungeonUUID() {
        return dungeonUUID;
    }

    public void setDungeonUUID(UUID dungeonUUID) {
        this.dungeonUUID = dungeonUUID;
    }
}
