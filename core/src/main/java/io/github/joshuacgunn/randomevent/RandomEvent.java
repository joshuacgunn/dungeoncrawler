package io.github.joshuacgunn.randomevent;

public interface RandomEvent {
    void happen();
    void cancel();
    void setCancelled(boolean cancelled);
    String getEventType();
}
