package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;


public class GameLoop {
    private GameState currentGameState;
    private GameState previousGameState;
    private boolean isRunning = true;
    private final Player player;

    public GameLoop(Player player) {
        this.player = player;
        Location playerLocation = player.getEntityLocation();
        
        // Initialize the appropriate game state based on player location
        if (playerLocation instanceof com.github.joshuacgunn.core.location.Dungeon) {
            this.currentGameState = new DungeonState(this);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Town) {
            this.currentGameState = new TownState(this);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Shop) {
            this.currentGameState = new ShopState(this);
        }
        
        // Ensure the game state is properly initialized
        if (this.currentGameState == null) {
            throw new IllegalStateException("Could not determine initial game state from player location: " + playerLocation);
        }
        
        // Set initial game state
        player.setGameState(this.currentGameState);
        
        // Main game loop
        while (isRunning && currentGameState != null) {
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

    public static GameLoop createGameLoop(Player player) {
        Location playerLocation = player.getEntityLocation();
        return new GameLoop(player);
    }
}