package com.joshuacgunn.core.dto;

import com.joshuacgunn.core.item.Weapon;

public class WeaponDTO extends ItemDTO {
    private float weaponDamage;
    private float weaponDurability;
    private Weapon.WeaponQuality weaponQuality;

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

    public Weapon.WeaponQuality getWeaponQuality() {
        return this.weaponQuality;
    }

    public void setWeaponQuality(Weapon.WeaponQuality weaponQuality) {
        this.weaponQuality = weaponQuality;
    }
}
