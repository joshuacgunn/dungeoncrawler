package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.mechanics.PlayerStats;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public Player.PlayerClass playerClass;
    public PlayerStats playerStats;
    public String gameState;
    public String previousGameState;
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

    public int getPlayerStatsValue(PlayerStats.Stat stat) {
        switch (stat) {
            case LUCK:
                return this.playerStats.getStatValue(PlayerStats.Stat.LUCK);
            case CHARISMA:
                return this.playerStats.getStatValue(PlayerStats.Stat.CHARISMA);
            case VITALITY:
                return this.playerStats.getStatValue(PlayerStats.Stat.VITALITY);
            case INTELLIGENCE:
                return this.playerStats.getStatValue(PlayerStats.Stat.INTELLIGENCE);
            case STRENGTH:
                return this.playerStats.getStatValue(PlayerStats.Stat.STRENGTH);
            case DEXTERITY:
                return this.playerStats.getStatValue(PlayerStats.Stat.DEXTERITY);
            default:
                return 0;
        }
    }

    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public void setPlayerStatsValue(PlayerStats.Stat stat, int value) {
        this.playerStats.setStatValue(stat, value);
    }
}