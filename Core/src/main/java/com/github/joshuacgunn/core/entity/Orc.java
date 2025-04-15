package com.github.joshuacgunn.core.entity;

import com.github.joshuacgunn.core.item.Weapon;

import java.util.Random;
import java.util.UUID;

import static com.github.joshuacgunn.core.item.Armor.generateArmor;

public class Orc extends Enemy {

    Random rand = new Random();

    public Orc(UUID uuid) {
        super("Orc", uuid, new Random().nextFloat(40f, 50f));
        final int armorPieces = rand.nextInt(2, 4);
        for (int i = 0; i < armorPieces; i++) {
            generateArmor(1, 3, this.getInventory());
        }
        Weapon.generateWeapon(2, 4, this.getInventory());
    }
}
