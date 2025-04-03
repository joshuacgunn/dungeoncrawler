package com.github.joshuacgunn.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DungeonDTO implements Serializable {
    private String locationName;
    private UUID locationUUID;
    private List<DungeonFloorDTO> floors = new ArrayList<>();
    private UUID currentFloorUUID;
    private float difficultyRating;

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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public UUID getLocationUUID() {
        return locationUUID;
    }

    public void setLocationUUID(UUID locationUUID) {
        this.locationUUID = locationUUID;
    }
}
