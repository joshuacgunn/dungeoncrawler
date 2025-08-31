package io.github.joshuacgunn.quest;

import io.github.joshuacgunn.entity.NPC;

public abstract class AbstractQuest {
    protected final String name;
    protected final String description;
    protected boolean completed;
    protected final NPC questGiver;

    protected AbstractQuest(String name, String description, NPC questGiver) {
        this.name = name;
        this.description = description;
        this.completed = false;
        this.questGiver = questGiver;
    }
}
