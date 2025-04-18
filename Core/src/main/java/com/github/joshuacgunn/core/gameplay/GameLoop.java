package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;

import java.util.UUID;

public class GameLoop {
    private GameState currentGameState;
    private GameState previousGameState;
    private boolean isRunning = true;
    private Player player;

    public GameLoop(Player player) {
        this.player = player;
        Location playerLocation = player.getEntityLocation();
        if (playerLocation instanceof com.github.joshuacgunn.core.location.Dungeon) {
            this.currentGameState = new DungeonState(this);
        }
        while (isRunning) {
            this.currentGameState.handleGameState();
        }
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public GameState getCurrentGameState() {
        return this.currentGameState;
    }

    public GameState getPreviousGameState() {
        return this.previousGameState;
    }

    public void setPreviousGameState(GameState previousGameState) {
        this.previousGameState = previousGameState;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public Player getPlayer() {
        return player;
    }
}
