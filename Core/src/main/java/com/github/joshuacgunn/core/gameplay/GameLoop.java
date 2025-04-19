package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.save.SaveManager;


public class GameLoop {
    private GameState currentGameState;
    private GameState previousGameState;
    private boolean isRunning = true;
    private final Player player;

    public GameLoop(Player player) {
        this.player = player;
        Location playerLocation = player.getCurrentLocation();

        // Initialize the appropriate game state based on player location
        initializeGameState(playerLocation);
        player.setGameState(this.currentGameState);
        SaveManager.saveState(player);

        // Main game loop - simplified
        if (currentGameState != null) {
            currentGameState.handleGameState();
        }
    }

    private void initializeGameState(Location playerLocation) {
        if (playerLocation instanceof com.github.joshuacgunn.core.location.Dungeon) {
            this.currentGameState = new DungeonState(this);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Town) {
            this.currentGameState = new TownState(this);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Shop) {
            this.currentGameState = new ShopState(this);
        } else {
            this.currentGameState = new ExploringState(this);
        }

        if (this.currentGameState == null) {
            throw new IllegalStateException("Could not determine initial game state from player location: " + playerLocation);
        }
    }

    public void stopGame() {
        this.isRunning = false;
        this.currentGameState = null;
        System.exit(0);  // Add this line to force exit
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
        return new GameLoop(player);
    }
}