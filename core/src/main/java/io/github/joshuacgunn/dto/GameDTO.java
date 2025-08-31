package io.github.joshuacgunn.dto;

import io.github.joshuacgunn.entity.Player;

public class GameDTO {
    public String currentGameState;
    public String previousGameState;
    public boolean isRunning;
    public Player player;

    public GameDTO() { }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(String currentGameState) {
        this.currentGameState = currentGameState;
    }

    public String getPreviousGameState() {
        return previousGameState;
    }

    public void setPreviousGameState(String previousGameState) {
        this.previousGameState = previousGameState;
    }
}
