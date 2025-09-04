package io.github.joshuacgunn.core.quest;

import io.github.joshuacgunn.core.entity.NPC;
import io.github.joshuacgunn.core.entity.Player;

public class KillQuest extends AbstractQuest implements Quest {


    protected KillQuest(String name, String description, NPC questGiver) {
        super(name, description, questGiver);
    }

    @Override
    public String getQuestName() {
        return "";
    }

    @Override
    public String getQuestDescription() {
        return "";
    }

    @Override
    public boolean isQuestCompleted() {
        return false;
    }

    @Override
    public void checkProgress(Player player) {

    }

    @Override
    public void giveRewards(Player player) {

    }

    @Override
    public String getQuestType() {
        return "";
    }
}
