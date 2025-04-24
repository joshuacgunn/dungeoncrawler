package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.EntityDTO;
import com.github.joshuacgunn.core.dto.EnemyDTO;
import com.github.joshuacgunn.core.dto.NpcDTO;
import com.github.joshuacgunn.core.dto.PlayerDTO;
import com.github.joshuacgunn.core.dto.InventoryDTO;
import com.github.joshuacgunn.core.entity.*;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.World;
import com.github.joshuacgunn.core.mechanics.EntityStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * Provides mapping functionality between Entity domain objects and their corresponding DTOs.
 * Handles polymorphic mapping of different entity types (Player, Enemy, NPC) and their properties,
 * including inventory management and equipment state.
 */

@Mapper(uses = {ItemMapper.class, DungeonMapper.class})
public interface EntityMapper {
    /**
     * Singleton instance of the mapper.
     */
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    /**
     * Maps an Entity object to its appropriate DTO representation based on the entity type.
     * Supports polymorphic mapping for Player, Enemy, and NPC entities while preserving
     * type-specific properties.
     *
     * Mapped properties include:
     * - Basic entity attributes (name, UUID, HP, defense)
     * - Life status
     * - Current location
     * - Inventory contents
     * - Equipped armor
     * - Current weapon
     *
     * Special handling:
     * - Null safety for inventory items and equipment
     * - Type-specific conversion for Player, Enemy, and NPC entities
     * - Location reference management via UUID
     * - Collection mapping for inventory and armor items
     *
     * @param entity The source Entity object to convert
     * @return EntityDTO containing the mapped data, with appropriate subtype for specialized entities
     */
    default EntityDTO entityToEntityDTO(Entity entity) {
        if (entity == null) return null;

        EntityDTO dto;
        if (entity instanceof Player) {
            dto = playerToPlayerDTO((Player) entity);
        } else if (entity instanceof Enemy) {
            dto = enemyToEnemyDTO((Enemy) entity);
        } else if (entity instanceof NPC) {
            dto = npcToNpcDTO((NPC) entity);
        }
        else {
            dto = new EntityDTO();
            dto.setEntityType(entity.getClass().getSimpleName());
        }

        // Set common entity properties
        dto.setEntityName(entity.getEntityName());
        dto.setEntityUUID(entity.getEntityUUID());
        dto.setEntityHp(entity.getEntityHp());
        dto.setEntityDefense(entity.getEntityDefense());
        dto.setAlive(entity.isAlive());
        dto.setStatusEffects(entity.getActiveStatusEffects());
        if (entity.getCurrentLocation() != null) {
            dto.setCurrentLocationUUID(entity.getCurrentLocation().getLocationUUID());
        }

        // Handle inventory items with null check
        if (entity.getInventory() != null && entity.getInventory().getItems() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            List<UUID> itemUUIDList = new ArrayList<>();

            for (Item item : entity.getInventory().getItems()) {
                if (item != null) {  // Add null check here
                    itemUUIDList.add(item.getItemUUID());
                }
            }

            inventoryDTO.setItemUUIDs(itemUUIDList);
            dto.setInventory(inventoryDTO);
        }

        // Handle armor items with null check
        if (entity.getArmors() != null) {
            List<UUID> armorUUIDList = new ArrayList<>();
            for (Item item : entity.getArmors()) {
                if (item instanceof Armor) {  // Add null check here
                    armorUUIDList.add(item.getItemUUID());
                }
            }
            dto.setEquippedArmorUUIDs(armorUUIDList);
        }

        // Handle current weapon with null check
        if (entity.getCurrentWeapon() != null) {
            dto.setCurrentWeaponUUID(entity.getCurrentWeapon().getItemUUID());
        }

        if (entity.getEntityStats() != null) {
            dto.setEntityStats(entity.getEntityStats());
            dto.setEntityStatsValue(EntityStats.Stat.STRENGTH, entity.getEntityStats().getStatValue(EntityStats.Stat.STRENGTH));
            dto.setEntityStatsValue(EntityStats.Stat.DEXTERITY, entity.getEntityStats().getStatValue(EntityStats.Stat.DEXTERITY));
            dto.setEntityStatsValue(EntityStats.Stat.INTELLIGENCE, entity.getEntityStats().getStatValue(EntityStats.Stat.INTELLIGENCE));
            dto.setEntityStatsValue(EntityStats.Stat.LUCK, entity.getEntityStats().getStatValue(EntityStats.Stat.LUCK));
            dto.setEntityStatsValue(EntityStats.Stat.CHARISMA, entity.getEntityStats().getStatValue(EntityStats.Stat.CHARISMA));
            dto.setEntityStatsValue(EntityStats.Stat.VITALITY, entity.getEntityStats().getStatValue(EntityStats.Stat.VITALITY));
        }

        return dto;
    }


    /**
     * Maps a Player entity to a PlayerDTO.
     * Includes player-specific attributes along with common entity properties.
     *
     * @param player The Player entity to convert
     * @return PlayerDTO containing the mapped player data
     */

    default PlayerDTO playerToPlayerDTO(Player player) {
        if (player == null) return null;

        PlayerDTO dto = new PlayerDTO();

        // Handle current dungeon
        if (player.getCurrentLocation() != null) {
            dto.setCurrentLocationUUID(player.getCurrentLocation().getLocationUUID());
        } else {
            dto.setCurrentLocationUUID(new World(UUID.randomUUID()).getLocationUUID());
        }

        if (player.getGameState() != null) {
            dto.setGameState(player.getGameState().getGameStateName());
        }

        if (player.getPreviousGameStateName() != null) {
            dto.setPreviousGameStateName(player.getPreviousGameState().getGameStateName());
        }

        dto.setPlayerClass(player.getPlayerClass());

        dto.setPlayerLevel(player.getPlayerLevel() );

        return dto;
    }

    /**
     * Maps an NPC entity to an NPCDTO.
     * Includes NPC-specific attributes along with common entity properties.
     *
     * @param npc The NPC entity to convert
     * @return NPCDTO containing the mapped NPC data
     */
    default NpcDTO npcToNpcDTO(NPC npc) {

        NpcDTO dto = new NpcDTO();

        dto.setNpcPersonality(npc.getNpcPersonality());

        return dto;
    }

    /**
     * Maps an Enemy entity to an EnemyDTO.
     * Includes enemy-specific attributes along with common entity properties.
     *
     * @param enemy The Enemy entity to convert
     * @return EnemyDTO containing the mapped enemy data
     */

    default EnemyDTO enemyToEnemyDTO(Enemy enemy) {
        if (enemy == null) return null;

        return new EnemyDTO();
    }

    /**
     * Maps an EntityDTO back to its corresponding Entity object.
     * Handles reverse mapping of all properties including inventory and equipment.
     *
     * @param dto The EntityDTO to convert back to an Entity
     * @return Entity object containing the mapped data
     */
    @Mapping(target = "entityMap", ignore = true)
    default Entity entityDtoToEntity(EntityDTO dto) {
        if (dto == null) return null;

        String entityType = dto.getEntityType();
        if (entityType == null) {
            entityType = "Entity";
        }


        Entity entity;

        switch(entityType) {
            case "Player":
                entity = playerDtoToPlayer((PlayerDTO) dto);
                break;
            case "Enemy":
                entity = enemyDtoToEnemy((EnemyDTO) dto);
                break;
            case "NPC":
                entity = npcDtotoNPC((NpcDTO) dto);
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }

        // Set common entity properties that weren't set in the constructor
        entity.setDeathStatus(dto.isAlive());
        entity.setEntityHp(dto.getEntityHp());

        if (dto.getEntityStats() != null) {
            entity.setEntityStats(dto.getEntityStats());

            // Set player stats
            entity.getEntityStats().setStatValue(EntityStats.Stat.STRENGTH, dto.getEntityStatsValue(EntityStats.Stat.STRENGTH));
            entity.getEntityStats().setStatValue(EntityStats.Stat.DEXTERITY, dto.getEntityStatsValue(EntityStats.Stat.DEXTERITY));
            entity.getEntityStats().setStatValue(EntityStats.Stat.INTELLIGENCE, dto.getEntityStatsValue(EntityStats.Stat.INTELLIGENCE));
            entity.getEntityStats().setStatValue(EntityStats.Stat.LUCK, dto.getEntityStatsValue(EntityStats.Stat.LUCK));
            entity.getEntityStats().setStatValue(EntityStats.Stat.CHARISMA, dto.getEntityStatsValue(EntityStats.Stat.CHARISMA));
            entity.getEntityStats().setStatValue(EntityStats.Stat.VITALITY, dto.getEntityStatsValue(EntityStats.Stat.VITALITY));
        }

        // Handle status effects
        if (dto.getStatusEffects() != null) {
            entity.setActiveStatusEffects(dto.getStatusEffects());
        }

        // Handle items in inventory
        if (dto.getInventory() != null && dto.getInventory().getItemUUIDs() != null) {
            for (UUID itemUUID : dto.getInventory().getItemUUIDs()) {
                Item item = Item.itemMap.get(itemUUID);
                entity.getInventory().addItem(item);
            }
        }

        // Handle current weapon
        if (dto.getCurrentWeaponUUID() != null) {
            Weapon weapon = (Weapon) Item.itemMap.get(dto.getCurrentWeaponUUID());
            entity.setCurrentWeapon(weapon);
        }

        // Handle equipped armor
        if (!dto.getEquippedArmorUUIDs().isEmpty() && dto.getEquippedArmorUUIDs() != null) {
            for (UUID armorUUID : dto.getEquippedArmorUUIDs()) {
                Armor armor = (Armor) Item.itemMap.get(armorUUID);
                if (armor != null) {  // Add this null check
                    entity.equipArmor(armor);
                }
            }
        }

        // Handle current location
        if (dto.getCurrentLocationUUID() != null) {
            entity.setCurrentLocation(Location.locationMap.get(dto.getCurrentLocationUUID()));
        }

        return entity;
    }

    default Player playerDtoToPlayer(PlayerDTO dto) {
        if (dto == null) return null;

        Player player = new Player(dto.getEntityName(), dto.getEntityUUID(), dto.getPlayerClass(), false);

        player.setPlayerLevel(dto.getPlayerLevel());

        if (Location.locationMap.containsKey(dto.getCurrentLocationUUID())) {
            player.setCurrentLocation(Location.locationMap.get(dto.getCurrentLocationUUID()));
        } else {
            player.setCurrentLocation(new World(UUID.randomUUID()));
        }

        if (dto.getGameState() != null) {
            player.setGameStateName(dto.getGameState());
        }

        if (dto.getPreviousGameStateName() != null) {
            player.setPreviousGameStateName(dto.getPreviousGameStateName());
        }

    return player;
}

    default NPC npcDtotoNPC(NpcDTO dto) {

        NPC npc = new NPC(dto.getEntityName(), dto.getEntityUUID());

        npc.setNpcPersonality(dto.getNpcPersonality());

        return npc;
    }

    default Enemy enemyDtoToEnemy(EnemyDTO dto) {
        if (dto == null) return null;
        Enemy enemy = new Enemy(Enemy.EnemyType.valueOf(dto.getEntityName().toUpperCase()), dto.getEntityUUID(), false);
        enemy.setDeathStatus(dto.isAlive());
        return enemy;
    }

}