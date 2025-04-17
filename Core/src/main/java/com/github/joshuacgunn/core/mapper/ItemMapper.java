package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.ArmorDTO;
import com.github.joshuacgunn.core.dto.ItemDTO;
import com.github.joshuacgunn.core.dto.WeaponDTO;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    default ItemDTO itemToItemDTO(Item item) {
        if (item == null) return null;

        if (item instanceof Weapon weapon) {
            WeaponDTO dto = new WeaponDTO();
            dto.setItemName(weapon.getItemName());
            dto.setItemUUID(weapon.getItemUUID());
            dto.setWeaponQuality(weapon.getWeaponQuality());
            dto.setWeaponMaterial(weapon.getWeaponMaterial());
            dto.setWeaponDamage(weapon.getWeaponDamage());
            dto.setWeaponDurability(weapon.getWeaponDurability());
            dto.setArmorPenetration(weapon.getArmorPenetration());
            dto.setItemType("Weapon"); // Explicitly set type
            return dto;
        } else if (item instanceof Armor armor) {
            ArmorDTO dto = new ArmorDTO();
            dto.setItemName(armor.getItemName());
            dto.setItemUUID(armor.getItemUUID());
            dto.setArmorDefense(armor.getArmorDefense());
            dto.setArmorSlot(armor.getArmorSlot());
            dto.setArmorQuality(armor.getArmorQuality());
            dto.setArmorMaterial(armor.getArmorMaterial());
            dto.setItemType("Armor"); // Explicitly set type
            return dto;
        } else {
            ItemDTO dto = new ItemDTO();
            dto.setItemName(item.getItemName());
            dto.setItemUUID(item.getItemUUID());
            dto.setItemType("Item"); // Explicitly set type
            return dto;
        }
    }

    @Mapping(target = "itemMap", ignore = true)
    default Item itemDtoToItem(ItemDTO dto) {
        if (dto == null) return null;

        if (dto instanceof WeaponDTO weaponDTO) {
            Weapon weapon = new Weapon(
                    weaponDTO.getItemName(),
                    weaponDTO.getItemUUID(),
                    weaponDTO.getWeaponQuality(),
                    weaponDTO.getWeaponMaterial()
            );
            weapon.setWeaponDamage(weaponDTO.getWeaponDamage());
            weapon.setArmorPenetration(weaponDTO.getArmorPenetration());
            weapon.setWeaponDurability(weaponDTO.getWeaponDurability());
            weapon.setWeaponDurability(weaponDTO.getWeaponDurability());
            return weapon;
        } if (dto instanceof ArmorDTO armorDTO) {
            Armor armor = new Armor(
                    armorDTO.getItemUUID(),
                    armorDTO.getArmorSlot(),
                    armorDTO.getItemName(),
                    armorDTO.getArmorQuality(),
                    armorDTO.getArmorMaterial()
                    );
            armor.setArmorDefense(armorDTO.getArmorDefense());
            return armor;
        } else {
            return new Item(dto.getItemName(), dto.getItemUUID());
        }
    }
}