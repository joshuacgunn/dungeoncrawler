package com.github.joshuacgunn.core.gameplay;

public interface GameState {
    void handleGameState();

    public void update();

    public void handleInput();

    public void changeState(GameStateType newState);
}
