package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Weapon;

import java.util.UUID;
import java.util.Random;

import static com.github.joshuacgunn.core.item.Armor.generateArmor;

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
    public Goblin(UUID uuid, boolean newEnemy) {
        super("Goblin", uuid, new Random().nextFloat(25.0f, 35.0f));
        final int armorPieces = rand.nextInt(1, 3);

        if (newEnemy) {
            for (int i = 0; i < armorPieces; i++) {
                generateArmor(0, 1, this.getInventory());
            }

            Weapon.generateWeapon(0, 2, this.getInventory());
        }
    }
}