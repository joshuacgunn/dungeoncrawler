package io.game.core.randomevent;

public abstract class AbstractEvent {
    String eventType;
    boolean isHappening = true;

    protected AbstractEvent(String eventType) {
        this.eventType = eventType;
    }

}
