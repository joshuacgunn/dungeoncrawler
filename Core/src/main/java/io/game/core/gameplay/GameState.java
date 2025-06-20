package io.game.core.gameplay;

public interface GameState {
    void handleGameState();
    void update();
    void handleInput();
    String getGameStateName();
    GameLoop getParentLoop();
}
