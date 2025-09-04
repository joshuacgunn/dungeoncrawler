package io.github.joshuacgunn.core.entity;

public class EntityStats {

    public int strength = Stat.STRENGTH.statValue;
    public int dexterity = Stat.DEXTERITY.statValue;
    public int vitality = Stat.VITALITY.statValue;
    public int intelligence = Stat.INTELLIGENCE.statValue;
    public int luck = Stat.LUCK.statValue;
    public int charisma = Stat.CHARISMA.statValue;

    public enum Stat {
        STRENGTH("Strength", 0),
        DEXTERITY("Dexterity", 0),
        VITALITY("Vitality", 0),
        INTELLIGENCE("Intelligence", 0),
        LUCK("Luck", 0),
        CHARISMA("Charisma", 0);

        private final String statName;
        private int statValue;

        Stat(String statName, int statValue) {
            this.statName = statName;
            this.statValue = statValue;
        }

        public String getStatName() {
            return this.statName;
        }

        public static Stat getStatByName(String statName) {
            return Stat.valueOf(statName.toUpperCase());
        }
    }

    public int getStatValue(Stat stat) {
        switch (stat) {
            case LUCK:
                return this.luck;
            case CHARISMA:
                return this.charisma;
            case VITALITY:
                return this.vitality;
            case INTELLIGENCE:
                return this.intelligence;
            case STRENGTH:
                return this.strength;
            case DEXTERITY:
                return this.dexterity;
            default:
                return 0;
        }
    }

    public void setStatValue(Stat stat, int statValue) {
        switch (stat) {
            case LUCK:
                this.luck = statValue;
                break;
            case CHARISMA:
                this.charisma = statValue;
                break;
            case VITALITY:
                this.vitality = statValue;
                break;
            case INTELLIGENCE:
                this.intelligence = statValue;
                break;
            case STRENGTH:
                this.strength = statValue;
                break;
            case DEXTERITY:
                this.dexterity = statValue;
        }
    }

    public void addTemporaryStat(Stat stat, int amount) {

    }
}
