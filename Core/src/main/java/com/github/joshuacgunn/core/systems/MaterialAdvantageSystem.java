package com.github.joshuacgunn.core.systems;

import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;

public class MaterialAdvantageSystem {
    // Higher values mean weapon material has advantage against armor material
    private static final float[][] ADVANTAGE_MATRIX = {
            // CLOTH   LEATHER  BONE    IRON    STEEL   MITHRIL CELESTIUM
            { 1.0f,   1.0f,    0.8f,   0.6f,   0.4f,   0.3f,   0.2f    }, // BONE weapon
            { 1.2f,   1.1f,    1.3f,   1.0f,   0.8f,   0.7f,   0.5f    }, // IRON weapon
            { 1.3f,   1.2f,    1.4f,   1.2f,   1.0f,   0.8f,   0.6f    }, // STEEL weapon
            { 1.5f,   1.4f,    1.6f,   1.4f,   1.2f,   0.9f,   0.7f    }, // OBSIDIAN weapon
            { 1.7f,   1.6f,    1.8f,   1.6f,   1.4f,   1.0f,   0.9f    }, // MITHRIL weapon
            { 2.0f,   1.8f,    2.0f,   1.8f,   1.6f,   1.4f,   1.0f    }  // CELESTIUM weapon
    };

    public static float getAdvantageMultiplier(Weapon.WeaponMaterial weaponMaterial, Armor.ArmorMaterial armorMaterial) {
        return ADVANTAGE_MATRIX[weaponMaterial.ordinal()][armorMaterial.ordinal()];
    }
}