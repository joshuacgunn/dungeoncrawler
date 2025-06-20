package io.game.core.randomevent;

public interface RandomEvent {
    void happen();
    void cancel();
    void setCancelled(boolean cancelled);
    String getEventType();
}
