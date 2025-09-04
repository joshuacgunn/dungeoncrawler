package io.github.joshuacgunn.core.entity;

import java.util.Random;
import java.util.UUID;

/**
 * Represents a Non-Player Character (NPC) in the game world.
 * NPCs are entities with distinct personalities that affect their dialogue and behavior.
 * Each NPC has a randomly assigned personality upon creation.
 */
public class NPC extends Entity {

    /**
     * Defines the possible personality types an NPC can have.
     * Each personality affects the NPC's dialogue and interactions.
     */
    public enum Personality {
        SARCASTIC,
        SERIOUS,
        ANGRY,
        FUNNY,
        DEPRESSED,
        STUPID,
        CHARISMATIC,
        COWARDLY,
        OPTIMISTIC,
        PESSIMISTIC,
        GREEDY,
        LOYAL,
        MYSTERIOUS,
        ADVENTUROUS,
        WISE,
        LAZY
    }

    /** The personality trait of this NPC */
    private Personality npcPersonality;

    /** Random number generator for personality assignment */
    private final Random random = new Random();

    private boolean hasQuest = false;

    /**
     * Creates a new NPC with specified name and UUID.
     * The NPC is initialized with 100 HP and a random personality.
     *
     * @param name The name of the NPC
     * @param uuid The unique identifier for this NPC
     */
    public NPC(String name, UUID uuid) {
        super(name, uuid);
        this.entityHp = 100;
        this.npcPersonality = Personality.values()[random.nextInt(Personality.values().length)];
        if (random.nextFloat() < 0.03 && npcPersonality != Personality.ANGRY && npcPersonality != Personality.LAZY && npcPersonality != Personality.DEPRESSED) {
            this.hasQuest = true;
        }
    }

    public boolean hasQuest() {
        return hasQuest;
    }

    public void setHasQuest(boolean hasQuest) {
        this.hasQuest = hasQuest;
    }

    /**
     * Gets the personality type of this NPC.
     *
     * @return The current personality of the NPC
     */
    public Personality getNpcPersonality() {
        return this.npcPersonality;
    }

    /**
     * Sets or changes the personality of this NPC.
     *
     * @param personality The new personality to assign to the NPC
     */
    public void setNpcPersonality(Personality personality) {
        this.npcPersonality = personality;
    }
}
