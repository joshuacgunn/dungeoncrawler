package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.container.Inventory;
import com.github.joshuacgunn.core.dto.*;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * Mapper interface for converting between EntityPlayer and PlayerDTO objects.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(uses = {ItemMapper.class})
public interface PlayerMapper {
    /**
     * Singleton instance of the mapper.
     */
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    /**
     * Converts an EntityPlayer to a PlayerDTO.
     * No explicit mappings needed as property names match.
     *
     * @param player the player to convert
     * @return the converted DTO
     */
    default PlayerDTO playerToPlayerDTO(Player player) {
        if (player == null) return null;

        PlayerDTO dto = new PlayerDTO();
        dto.setEntityName(player.getEntityName());
        dto.setEntityUUID(player.getEntityUUID());
        dto.setEntityHp(player.getEntityHp());

        // Handle current dungeon
        if (player.getCurrentDungeon() != null) {
            DungeonDTO dungeonDTO = DungeonMapper.INSTANCE.dungeonToDungeonDto(player.getCurrentDungeon());
            dto.setCurrentDungeon(dungeonDTO);
        }

        if (player.getArmors() != null) {
            List<ArmorDTO> armorDTOList = new ArrayList<>();
            for (Item item : player.getArmors()) {
                if (item instanceof Armor) {
                    armorDTOList.add((ArmorDTO) ItemMapper.INSTANCE.itemToItemDTO(item));
                }
            }
            dto.setArmors(armorDTOList);
        }

        // Handle items in Inventory
        if (player.getInventory() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            List<ItemDTO> itemDTOList = new ArrayList<>();

            for (Item item : player.getInventory().getItemList()) {
                itemDTOList.add(ItemMapper.INSTANCE.itemToItemDTO(item));
            }

            inventoryDTO.setItemList(itemDTOList);
            dto.setInventory(inventoryDTO);
        }

        // Explicitly handle current weapon
        if (player.getCurrentWeapon() != null) {
            WeaponDTO weaponDTO = (WeaponDTO) ItemMapper.INSTANCE.itemToItemDTO(player.getCurrentWeapon());
            dto.setCurrentWeapon(weaponDTO);
        }
        return dto;
    }

    /**
     * Converts a PlayerDTO to an EntityPlayer.
     * Uses a custom implementation to handle the EntityPlayer constructor.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "entityMap", ignore = true)
    default Player playerDtoToPlayer(PlayerDTO dto) {
        Player player = new Player(dto.getEntityName(), dto.getEntityUUID());
        Inventory playerInventory = player.getInventory();

        Map<UUID, Item> itemMap = new HashMap<>();

        if (dto.getInventory() != null && dto.getInventory().getItemList() != null) {
            for (ItemDTO itemDTO : dto.getInventory().getItemList()) {
                Item item = ItemMapper.INSTANCE.itemDtoToItem(itemDTO);
                playerInventory.addItem(item);
                itemMap.put(item.getItemUUID(), item);
            }
        }
        if (dto.getArmors() != null) {
            for (ArmorDTO armorDTO : dto.getArmors()) {
                Armor armor = (Armor) ItemMapper.INSTANCE.itemDtoToItem(armorDTO);
                player.equipArmor(armor);
            }
        }

        Dungeon dungeon = DungeonMapper.INSTANCE.dungeonDtoToDungeon(dto.getCurrentDungeon());
        player.setCurrentDungeon(dungeon);

        if (dto.getCurrentWeapon() != null) {
            UUID weaponId = dto.getCurrentWeapon().getItemUUID();
            Item weapon = itemMap.get(weaponId);
            if (weapon instanceof Weapon) {
                player.setCurrentWeapon((Weapon) weapon);
            } else {
                WeaponDTO weaponDTO = dto.getCurrentWeapon();
                Weapon newWeapon = new Weapon(
                        weaponDTO.getItemName(),
                        weaponDTO.getItemUUID(),
                        weaponDTO.getWeaponDamage(),
                        weaponDTO.getWeaponDurability()
                );
                player.setCurrentWeapon(newWeapon);
            }
        }

        return player;
    }
}