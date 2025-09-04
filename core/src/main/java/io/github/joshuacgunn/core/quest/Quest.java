package io.github.joshuacgunn.core.quest;

import io.github.joshuacgunn.core.entity.Player;

public interface Quest {
    String getQuestName();
    String getQuestDescription();
    boolean isQuestCompleted();
    void checkProgress(Player player);
    void giveRewards(Player player);
    String getQuestType(); // e.g "Kill", "Collect", "Explore", etc.
}
