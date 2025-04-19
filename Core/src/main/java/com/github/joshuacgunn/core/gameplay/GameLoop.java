package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;

public class GameLoop {
    private GameState currentGameState;
    private GameState previousGameState;
    private boolean isRunning = true;
    private final Player player;

    public GameLoop(Player player, boolean handleState) {
        this.player = player;
        Location playerLocation = player.getCurrentLocation();

        if (!handleState) {
            // For loading saved games - use existing state if available
            if (player.getGameState() != null) {
                this.currentGameState = player.getGameState();
                this.previousGameState = player.getPreviousGameState();
            } else {
                // Initialize state if none exists
                initializeGameState(playerLocation);
                player.setGameState(this.currentGameState);
            }
        } else {
            // For new games or when explicitly handling state
            initializeGameState(playerLocation);
            player.setGameState(this.currentGameState);

            // Only handle game state if explicitly requested
            if (this.currentGameState != null) {
                this.currentGameState.handleGameState();
            }
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
            this.currentGameState = new ExploringState(this, true);
        }
    }

    public void stopGame() {
        this.isRunning = false;
        this.currentGameState = null;
        System.exit(0);
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
        return new GameLoop(player, false);
    }

    public void startGameLoop() {
        if (this.currentGameState != null) {
            this.currentGameState.handleGameState();
        }
    }
}
