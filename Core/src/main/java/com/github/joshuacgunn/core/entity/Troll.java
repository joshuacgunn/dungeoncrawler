package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;

import java.util.Random;
import java.util.UUID;

public class Troll extends Enemy{

    Random rand = new Random();

    public Troll(UUID uuid) {
        super("Troll", uuid, new Random().nextFloat(50f, 75f));
        final int armorPieces = rand.nextInt(2, 4);

        for (int i = 0; i < armorPieces; i++) {
            Armor.generateArmor(2, 4, this.getInventory());
        }

        Weapon.generateWeapon(3, 4, this.getInventory());
    }
}
