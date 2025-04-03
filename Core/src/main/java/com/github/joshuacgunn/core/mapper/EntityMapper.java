package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.*;
import com.github.joshuacgunn.core.entity.*;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * Mapper interface for converting between Entity objects and their respective DTOs.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(uses = {ItemMapper.class, DungeonMapper.class})
public interface EntityMapper {
    /**
     * Singleton instance of the mapper.
     */
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    /**
     * Converts an Entity to an appropriate EntityDTO based on its type.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    default EntityDTO entityToEntityDTO(Entity entity) {
        if (entity == null) return null;

        // Determine the specific entity type and create the appropriate DTO
        EntityDTO dto;
        if (entity instanceof Player) {
            dto = playerToPlayerDTO((Player) entity);
        } else if (entity instanceof Enemy) {
            dto = enemyToEnemyDTO((Enemy) entity);
        } else {
            // Generic entity - should not usually happen
            dto = new EntityDTO();
            dto.setEntityType(entity.getClass().getSimpleName());
        }

        // Set common entity properties
        dto.setEntityName(entity.getEntityName());
        dto.setEntityUUID(entity.getEntityUUID());
        dto.setEntityHp(entity.getEntityHp());
        dto.setEntityDefense(entity.getEntityDefense());
        dto.setDead(entity.getDeathStatus());

        // Handle armor items
        if (entity.getArmors() != null) {
            List<ArmorDTO> armorDTOList = new ArrayList<>();
            for (Item item : entity.getArmors()) {
                if (item instanceof Armor) {
                    armorDTOList.add((ArmorDTO) ItemMapper.INSTANCE.itemToItemDTO(item));
                }
            }
            dto.setEquippedArmors(armorDTOList);
        }

        // Handle current weapon
        if (entity.getCurrentWeapon() != null) {
            WeaponDTO weaponDTO = (WeaponDTO) ItemMapper.INSTANCE.itemToItemDTO(entity.getCurrentWeapon());
            dto.setCurrentWeapon(weaponDTO);
        }

        return dto;
    }

    /**
     * Converts an Entity to a Player-specific DTO.
     *
     * @param player the player entity to convert
     * @return the player DTO
     */
    default PlayerDTO playerToPlayerDTO(Player player) {
        if (player == null) return null;

        PlayerDTO dto = new PlayerDTO();

        // Handle current dungeon
        if (player.getCurrentDungeon() != null) {
            dto.setCurrentDungeonUUID(player.getCurrentDungeon().getLocationUUID());
        }

        // Handle items in Inventory
        if (player.getInventory() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            List<ItemDTO> itemDTOList = new ArrayList<>();

            for (Item item : player.getInventory().getItems()) {
                itemDTOList.add(ItemMapper.INSTANCE.itemToItemDTO(item));
            }

            inventoryDTO.setItemList(itemDTOList);
            dto.setInventory(inventoryDTO);
        }

        return dto;
    }

    /**
     * Converts an Enemy to an Enemy-specific DTO.
     *
     * @param enemy the enemy entity to convert
     * @return the enemy DTO
     */
    default EnemyDTO enemyToEnemyDTO(Enemy enemy) {
        if (enemy == null) return null;

        EnemyDTO dto = new EnemyDTO();
        if (enemy.getInventory() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            List<ItemDTO> itemDTOList = new ArrayList<>();

            for (Item item : enemy.getInventory().getItems()) {
                itemDTOList.add(ItemMapper.INSTANCE.itemToItemDTO(item));
            }

            inventoryDTO.setItemList(itemDTOList);
            dto.setInventory(inventoryDTO);
        }

        return dto;
    }

    /**
     * Converts an EntityDTO to an appropriate Entity based on its type.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "entityMap", ignore = true)
    default Entity entityDtoToEntity(EntityDTO dto) {
        if (dto == null) return null;

        String entityType = dto.getEntityType();
        if (entityType == null) {
            entityType = "Entity"; // Default fallback
        }

        Entity entity;
        switch (entityType) {
            case "Player":
                entity = playerDtoToPlayer((PlayerDTO) dto);
                break;
            case "Goblin":
                entity = enemyDtoToEnemy((EnemyDTO) dto);
                break;
            case "Enemy":
                // Since Enemy is abstract, create a basic implementation or use a factory
                entity = new Enemy(dto.getEntityName(), dto.getEntityUUID(), dto.getEntityHp()) {};
                break;
            default:
                // Handle unknown entity types or throw exception
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }

        // Set common entity properties that weren't set in the constructor
        entity.setDeathStatus(dto.isDead());

        // Handle armors
        if (dto.getEquippedArmors() != null) {
            for (ArmorDTO armorDTO : dto.getEquippedArmors()) {
                Armor armor = (Armor) ItemMapper.INSTANCE.itemDtoToItem(armorDTO);
                entity.equipArmor(armor);
            }
        }

        if (dto.getInventory() != null && dto.getInventory().getItemList() != null) {
            for (ItemDTO itemDTO : dto.getInventory().getItemList()) {
                Item item = ItemMapper.INSTANCE.itemDtoToItem(itemDTO);
                entity.getInventory().addItem(item);
            }
        }

        // Handle current weapon
        if (dto.getCurrentWeapon() != null) {
            Weapon weapon = (Weapon) ItemMapper.INSTANCE.itemDtoToItem(dto.getCurrentWeapon());
            entity.setCurrentWeapon(weapon);
        }

        return entity;
    }

    /**
     * Converts a PlayerDTO to a Player entity.
     *
     * @param dto the player DTO to convert
     * @return the player entity
     */
    default Player playerDtoToPlayer(PlayerDTO dto) {
        if (dto == null) return null;

        Player player = new Player(dto.getEntityName(), dto.getEntityUUID());

        // Set current dungeon
        if (dto.getCurrentDungeonUUID() != null) {
            if (Location.locationMap.containsKey(dto.getCurrentDungeonUUID())) {
                Location location = Location.locationMap.get(dto.getCurrentDungeonUUID());
                if (location instanceof Dungeon) {
                    player.setCurrentDungeon((Dungeon) location);
                }
            }
        }
        return player;
    }

    default Enemy enemyDtoToEnemy(EnemyDTO dto) {
        if (dto == null) return null;
        switch (dto.getEntityName().toUpperCase()) {
            case "GOBLIN" -> {
                Goblin goblin = new Goblin(dto.getEntityUUID());
                goblin.setEntityHp(dto.getEntityHp());
                goblin.setDeathStatus(dto.isDead());
                return goblin;
            }
            default -> {
                Enemy enemy = new Enemy(dto.getEntityName(), dto.getEntityUUID(), dto.getEntityHp()) {};
                enemy.setDeathStatus(dto.isDead());
                return enemy;
            }
        }
    }
}