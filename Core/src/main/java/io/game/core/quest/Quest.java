package io.game.core.quest;

import io.game.core.entity.Player;

public interface Quest {
    String getQuestName();
    String getQuestDescription();
    boolean isQuestCompleted();
    void checkProgress(Player player);
    void giveRewards(Player player);
    String getQuestType(); // e.g "Kill", "Collect", "Explore", etc.
}
