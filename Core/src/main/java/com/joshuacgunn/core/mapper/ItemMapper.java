package com.joshuacgunn.core.mapper;

import com.joshuacgunn.core.dto.ArmorDTO;
import com.joshuacgunn.core.dto.ItemDTO;
import com.joshuacgunn.core.dto.WeaponDTO;
import com.joshuacgunn.core.item.Armor;
import com.joshuacgunn.core.item.Item;
import com.joshuacgunn.core.item.Weapon;
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
            dto.setWeaponDamage(weapon.getWeaponDamage());
            dto.setWeaponDurability(weapon.getWeaponDurability());
            dto.setWeaponQuality(weapon.getWeaponQuality());
            dto.setItemType("Weapon"); // Explicitly set type
            return dto;
        } else if (item instanceof Armor armor) {
            ArmorDTO dto = new ArmorDTO();
            dto.setItemName(armor.getItemName());
            dto.setItemUUID(armor.getItemUUID());
            dto.setArmorDefense(armor.getArmorDefense());
            dto.setSlot(armor.getArmorSlot());
            dto.setQuality(armor.getArmorQuality());
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
                    weaponDTO.getWeaponQuality()
            );
            weapon.setWeaponDamage(weaponDTO.getWeaponDamage());
            weapon.setWeaponDurability(weaponDTO.getWeaponDurability());
        } if (dto instanceof ArmorDTO armorDTO) {
            Armor armor = new Armor(
                    armorDTO.getItemUUID(),
                    armorDTO.getSlot(),
                    armorDTO.getItemName(),
                    armorDTO.getQuality()
                    );
            armor.setArmorDefense(armorDTO.getArmorDefense());
            return armor;
        } else {
            return new Item(dto.getItemName(), dto.getItemUUID());
        }
    }
}