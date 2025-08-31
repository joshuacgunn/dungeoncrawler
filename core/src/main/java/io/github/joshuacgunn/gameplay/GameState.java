package io.github.joshuacgunn.gameplay;

public interface GameState {
    void handleGameState();
    void update();
    void handleInput();
    String getGameStateName();
    GameLoop getParentLoop();
}
