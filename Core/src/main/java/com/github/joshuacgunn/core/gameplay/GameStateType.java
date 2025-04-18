package com.github.joshuacgunn.core.gameplay;

public enum GameStateType {
    COMBAT(Combat.class),
    EXPLORING(Exploring.class),
    TOWN(TownTraversal.class),
    DUNGEON(DungeonTraversal.class);

    private final Class<? extends GameState> gameLoopClass;

    GameStateType(Class<? extends GameState> gameLoopClass) {
        this.gameLoopClass = gameLoopClass;
    }
}