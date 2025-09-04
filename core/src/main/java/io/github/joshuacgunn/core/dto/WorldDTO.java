package io.github.joshuacgunn.core.dto;

public class WorldDTO {
    private int currentWorldTick;

    public void setCurrentWorldTick(int tick) {
        this.currentWorldTick = tick;
    }

    public int getCurrentWorldTick() {
        return this.currentWorldTick;
    }
}
