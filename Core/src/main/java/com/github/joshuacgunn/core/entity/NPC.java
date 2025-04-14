package com.github.joshuacgunn.core.entity;

import java.util.Random;
import java.util.UUID;

public class NPC extends Entity {

    private Personality npcPersonality;

    Random random = new Random();

    public static enum Personality {
        SARCASTIC,
        SERIOUS,
        ANGRY,
        FUNNY,
        DEPRESSED,
        STUPID;
    }

    public NPC(String name, UUID uuid) {
        super(name, uuid);
        this.entityHp = 100;
        this.npcPersonality = Personality.values()[random.nextInt(0, 5)];
    }

    public Personality getNpcPersonality() {
        return this.npcPersonality;
    }

    public void setNpcPersonality(Personality personality) {
        this.npcPersonality = personality;
    }
}
