package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.mechanics.GameEvents;
import com.github.joshuacgunn.core.save.SaveManager;

import java.io.File;

/**
 * Manages the game's main loop and state transitions.
 * <p>
 * The GameLoop class is responsible for maintaining the current game state,
 * handling state transitions, and managing the overall flow of the game.
 * It works with different game states (Town, Dungeon, Shop, Exploring)
 * and manages the player's current state and location.
 */
public class GameLoop {
    /** The current active game state */
    private GameState currentGameState;

    /** The previous game state, used for state transitions */
    private GameState previousGameState;

    /** Flag indicating if the game loop is currently running */

    /** The player associated with this game loop */
    private final Player player;
    private final boolean isNewGame;

    /**
     * Creates a new GameLoop instance.
     *
     * @param player The player associated with this game loop
     */
    public GameLoop(Player player, boolean isNewGame) {
        this.player = player;
        this.isNewGame = isNewGame;
        Location playerLocation = player.getCurrentLocation();

        if (!isNewGame) {
            GameEvents.loadGameGreet(player);
        }

        initializeGameState(playerLocation);

        if (!isNewGame && player.getGameStateName() != null) {
            // Create the actual state objects for the game
            createAndSetGameState(player.getGameStateName(), false);
            
            // Restore previous state if it exists
            if (player.getPreviousGameStateName() != null) {
                createAndSetPreviousGameState(player.getPreviousGameStateName());
            }
        }
    }

    private void createAndSetGameState(String stateName, boolean isNew) {
        if (stateName == null) return;
        
        GameState newState = switch (stateName) {
            case "DungeonState" -> new DungeonState(this, isNew);
            case "ExploringState" -> new ExploringState(this, isNew);
            case "TownState" -> new TownState(this, isNew);
            case "ShopState" -> new ShopState(this, isNew);
            default -> null;
        };
        
        if (newState != null) {
            this.currentGameState = newState;
            player.setGameState(newState);
        }
    }

    private void createAndSetPreviousGameState(String stateName) {
        if (stateName == null) return;
        
        GameState prevState = switch (stateName) {
            case "DungeonState" -> new DungeonState(this, false);
            case "ExploringState" -> new ExploringState(this, false);
            case "TownState" -> new TownState(this, false);
            case "ShopState" -> new ShopState(this, false);
            default -> null;
        };
        
        if (prevState != null) {
            this.previousGameState = prevState;
            player.setPreviousGameState(prevState);
        }
    }

    /**
     * Initializes the appropriate game state based on player location.
     *
     * @param playerLocation The current location of the player
     */
    private void initializeGameState(Location playerLocation) {
        if (playerLocation instanceof com.github.joshuacgunn.core.location.Dungeon) {
            this.currentGameState = new DungeonState(this, isNewGame);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Town) {
            this.currentGameState = new TownState(this, isNewGame);
        } else if (playerLocation instanceof com.github.joshuacgunn.core.location.Shop) {
            this.currentGameState = new ShopState(this, isNewGame);
        } else {
            this.currentGameState = new ExploringState(this, isNewGame);
        }
    }

    /**
     * Stops the game loop and exits the application.
     */
    public void stopGame() {
        this.currentGameState = null;
        System.exit(0);
    }

    /**
     * Sets the current game state.
     *
     * @param currentGameState The new game state to set
     */
    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    /**
     * Gets the current game state.
     *
     * @return The current game state
     */
    public GameState getCurrentGameState() {
        return this.currentGameState;
    }

    /**
     * Gets the previous game state.
     *
     * @return The previous game state
     */
    public GameState getPreviousGameState() {
        return this.previousGameState;
    }

    /**
     * Sets the previous game state.
     *
     * @param previousGameState The game state to set as previous
     */
    public void setPreviousGameState(GameState previousGameState) {
        this.previousGameState = previousGameState;
    }

    /**
     * Sets the running state of the game loop.
     *
     * @param running Whether the game loop should be running
     */

    /**
     * Gets the player associated with this game loop.
     *
     * @return The current player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Creates a new GameLoop instance without immediately starting state handling.
     *
     * @param player The player for the new game loop
     * @return A new GameLoop instance
     */
    public static GameLoop createGameLoop(Player player) {
        return new GameLoop(player, false);
    }

    /**
     * Starts the game loop if a valid game state exists.
     * This method begins processing the current game state
     * and handles any state transitions.
     */
    public void startGameLoop() {
        if (this.currentGameState != null) {
            this.currentGameState.handleGameState();
        }
    }
}