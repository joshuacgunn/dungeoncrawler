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

import static com.github.joshuacgunn.core.gson.GsonProvider.GSON;

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
            return new Weapon(
                    weaponDTO.getItemName(),
                    weaponDTO.getItemUUID(),
                    weaponDTO.getWeaponDamage(),
                    weaponDTO.getWeaponDurability()
            );
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