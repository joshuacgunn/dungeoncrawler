package com.github.joshuacgunn.core.dto;

public class WeaponDTO extends ItemDTO {
    private float weaponDamage;
    private float weaponDurability;

    public WeaponDTO() {
        setItemType("Weapon");
    }

    public float getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(float damage) {
        this.weaponDamage = damage;
    }

    public float getWeaponDurability() {
        return weaponDurability;
    }

    public void setWeaponDurability(float weaponDurability) {
        this.weaponDurability = weaponDurability;
    }
}
