package io.github.joshuacgunn.core.mapper;

import io.github.joshuacgunn.core.dto.ArmorDTO;
import io.github.joshuacgunn.core.dto.ItemDTO;
import io.github.joshuacgunn.core.dto.WeaponDTO;
import io.github.joshuacgunn.core.item.Armor;
import io.github.joshuacgunn.core.item.Item;
import io.github.joshuacgunn.core.item.Weapon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A MapStruct mapper interface that handles conversion between Item domain objects
 * and their corresponding DTOs (Data Transfer Objects).
 * Supports polymorphic mapping of different item types (Weapon, Armor, etc.)
 * while maintaining data integrity and preventing circular references.
 */
@Mapper
public interface ItemMapper {

    /** Singleton instance of the ItemMapper */
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    /**
     * Converts an Item domain object to its DTO representation.
     * Handles polymorphic mapping based on item type and preserves all relevant
     * item attributes during conversion.
     *
     * Mapped properties include:
     * - Basic item attributes (name, UUID, value)
     * - Type-specific properties (damage for weapons, defense for armor)
     * - Rarity information
     * - Item state and condition
     * - Custom attributes and enchantments
     *
     * Special handling:
     * - Polymorphic type detection and appropriate subtype mapping
     * - Null safety for optional attributes
     * - Preservation of item type information
     *
     * @param item The source Item object to convert
     * @return ItemDTO containing the mapped data, with appropriate subtype for specialized items
     */
    default ItemDTO itemToItemDTO(Item item) {
        if (item == null) return null;

        if (item instanceof Weapon weapon) {
            WeaponDTO dto = new WeaponDTO();
            dto.setItemRarity(weapon.getItemRarity());
            dto.setItemName(weapon.getItemName());
            dto.setItemUUID(weapon.getItemUUID());
            dto.setItemValue(weapon.getItemValue());
            dto.setWeaponQuality(weapon.getWeaponQuality());
            dto.setWeaponMaterial(weapon.getWeaponMaterial());
            dto.setWeaponDamage(weapon.getWeaponDamage());
            dto.setWeaponDurability(weapon.getWeaponDurability());
            dto.setArmorPenetration(weapon.getArmorPenetration());
            dto.setItemType("Weapon"); // Explicitly set type
            return dto;
        } else if (item instanceof Armor armor) {
            ArmorDTO dto = new ArmorDTO();
            dto.setItemRarity(armor.getItemRarity());
            dto.setItemName(armor.getItemName());
            dto.setItemUUID(armor.getItemUUID());
            dto.setItemValue(armor.getItemValue());
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
            dto.setItemValue(item.getItemValue());
            dto.setItemRarity(item.getItemRarity());
            dto.setItemType("Item"); // Explicitly set type
            return dto;
        }
    }

    /**
     * Converts an ItemDTO back to an Item domain object.
     * Maps all relevant fields while ignoring the itemMap to prevent
     * circular references in the object graph.
     *
     * Features:
     * - Type-aware conversion based on stored item type
     * - Recreation of item attributes and properties
     * - Proper handling of item state
     * - Reference management for related entities
     *
     * @param dto The ItemDTO to convert back to an Item
     * @return Item object containing the mapped data
     */
    @Mapping(target = "itemMap", ignore = true)
    default Item itemDtoToItem(ItemDTO dto) {
        if (dto == null) return null;

        if (dto instanceof WeaponDTO weaponDTO) {
            Weapon weapon = new Weapon(
                    weaponDTO.getItemName(),
                    weaponDTO.getItemUUID(),
                    weaponDTO.getItemRarity(),
                    false
            );
            weapon.setWeaponDamage(weaponDTO.getWeaponDamage());
            weapon.setWeaponQuality(weaponDTO.getWeaponQuality());
            weapon.setWeaponMaterial(weaponDTO.getWeaponMaterial());
            weapon.setArmorPenetration(weaponDTO.getArmorPenetration());
            weapon.setWeaponDurability(weaponDTO.getWeaponDurability());
            weapon.setWeaponDurability(weaponDTO.getWeaponDurability());
            weapon.setItemValue(weaponDTO.getItemValue());
            return weapon;
        } if (dto instanceof ArmorDTO armorDTO) {
            Armor armor = new Armor(
                    armorDTO.getItemUUID(),
                    armorDTO.getArmorSlot(),
                    armorDTO.getItemName(),
                    armorDTO.getItemRarity(),
                    false
                    );
            armor.setArmorQuality(armorDTO.getArmorQuality());
            armor.setArmorMaterial(armorDTO.getArmorMaterial());
            armor.setArmorDefense(armorDTO.getArmorDefense());
            armor.setItemValue(armorDTO.getItemValue());
            return armor;
        } else {
            return new Item(dto.getItemName(), dto.getItemUUID());
        }
    }
}
