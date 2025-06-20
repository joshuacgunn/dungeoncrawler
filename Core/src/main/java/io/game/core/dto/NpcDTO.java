package io.game.core.dto;

import io.game.core.entity.NPC;

public class NpcDTO extends EntityDTO{

    private NPC.Personality npcPersonality;
    private boolean hasQuest;

    public NpcDTO() {
        super();
        this.setEntityType("NPC");
    }

    public void setNpcPersonality(NPC.Personality npcPersonality) {
        this.npcPersonality = npcPersonality;
    }

    public NPC.Personality getNpcPersonality() {
        return npcPersonality;
    }

    public void setHasQuest(boolean hasQuest) {
        this.hasQuest = hasQuest;
    }

    public boolean isHasQuest() {
        return hasQuest;
    }
}
