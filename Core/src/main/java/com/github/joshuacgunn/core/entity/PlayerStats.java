package com.github.joshuacgunn.core.entity;

public class PlayerStats {
    private int strength;
    private int dexterity;
    private int vitality;
    private int intelligence;
    private int luck;
    private int charisma;

    public int getStrength() {
        return this.strength;
    }

    public int getDexterity() {
        return this.dexterity;
    }

    public int getVitality() {
        return this.vitality;
    }

    public int getIntelligence() {
        return this.intelligence;
    }

    public int getLuck() {
        return this.luck;
    }

    public int getCharisma() {
        return this.charisma;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
}
