package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.entity.NPC;

public class NpcDTO extends EntityDTO{

    private NPC.Personality npcPersonality;

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
}
