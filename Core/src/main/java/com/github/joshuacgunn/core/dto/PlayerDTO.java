package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.mechanics.EntityStats;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public Player.PlayerClass playerClass;
    public EntityStats entityStats;
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

    public int getPlayerStatsValue(EntityStats.Stat stat) {
        switch (stat) {
            case LUCK:
                return this.entityStats.getStatValue(EntityStats.Stat.LUCK);
            case CHARISMA:
                return this.entityStats.getStatValue(EntityStats.Stat.CHARISMA);
            case VITALITY:
                return this.entityStats.getStatValue(EntityStats.Stat.VITALITY);
            case INTELLIGENCE:
                return this.entityStats.getStatValue(EntityStats.Stat.INTELLIGENCE);
            case STRENGTH:
                return this.entityStats.getStatValue(EntityStats.Stat.STRENGTH);
            case DEXTERITY:
                return this.entityStats.getStatValue(EntityStats.Stat.DEXTERITY);
            default:
                return 0;
        }
    }

    public EntityStats getPlayerStats() {
        return this.entityStats;
    }

    public void setPlayerStats(EntityStats entityStats) {
        this.entityStats = entityStats;
    }

    public void setPlayerStatsValue(EntityStats.Stat stat, int value) {
        this.entityStats.setStatValue(stat, value);
    }
}