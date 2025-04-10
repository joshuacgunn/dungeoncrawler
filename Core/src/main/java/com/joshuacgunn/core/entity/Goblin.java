package com.joshuacgunn.core.entity;

import com.joshuacgunn.core.item.Armor;
import com.joshuacgunn.core.item.Weapon;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Random;

import static com.joshuacgunn.core.item.Armor.generateArmor;

/**
 * Represents a Goblin enemy in the game.
 * Extends the {@link Enemy} class.
 */
public class Goblin extends Enemy {

    /**
     * Constructs a new Goblin enemy with the given UUID.
     *
     * @param uuid The unique identifier for the Goblin.
     */
    Random rand = new Random();
    public Goblin(UUID uuid) {
        super("Goblin", uuid, new Random().nextFloat(25.0f, 35.0f));
        final int armorPieces = rand.nextInt(1, 4);

        for (int i = 0; i < armorPieces; i++) {
            generateArmor(0, 1, this.getInventory());
        }

        Weapon weapon = Weapon.generateWeapon(0, 3, this.getInventory());
        this.currentWeapon = weapon;
    }
}