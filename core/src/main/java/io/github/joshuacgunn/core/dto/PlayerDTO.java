package io.github.joshuacgunn.core.dto;

import io.github.joshuacgunn.core.entity.Player;

import java.util.UUID;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public Player.PlayerClass playerClass;
    public String gameState;
    public String previousGameState;
    public UUID lastGameLocationUUID;
    public int playerLevel;


    public PlayerDTO() {
        super();
        this.setEntityType("Player");
    }

    public int getPlayerLevel() {
        return this.playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getGameState() {
        return this.gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getPreviousGameStateName() {
        return this.previousGameState;
    }

    public void setPreviousGameStateName(String previousGameState) {
        this.previousGameState = previousGameState;
    }

    public void setPlayerClass(Player.PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public Player.PlayerClass getPlayerClass() {
        return this.playerClass;
    }

    public UUID getLastGameLocationUUID() {
        return lastGameLocationUUID;
    }

    public void setLastGameLocationUUID(UUID uuid) {
        this.lastGameLocationUUID = uuid;
    }
}
