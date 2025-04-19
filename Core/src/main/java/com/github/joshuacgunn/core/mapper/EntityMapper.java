package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.EntityDTO;
import com.github.joshuacgunn.core.dto.EnemyDTO;
import com.github.joshuacgunn.core.dto.NpcDTO;
import com.github.joshuacgunn.core.dto.PlayerDTO;
import com.github.joshuacgunn.core.dto.InventoryDTO;
import com.github.joshuacgunn.core.entity.*;
import com.github.joshuacgunn.core.gameplay.*;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.location.World;
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
                if (item instanceof Armor && item != null) {  // Add null check here
                    armorUUIDList.add(item.getItemUUID());
                }
            }
            dto.setEquippedArmorUUIDs(armorUUIDList);
        }

        // Handle current weapon with null check
        if (entity.getCurrentWeapon() != null) {
            dto.setCurrentWeaponUUID(entity.getCurrentWeapon().getItemUUID());
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
        if (player.getCurrentLocation() != null) {
            dto.setCurrentLocationUUID(player.getCurrentLocation().getLocationUUID());
        }

        if (player.getGameState() != null) {
            dto.setGameState(player.getGameState().getGameStateName());
        }

        if (player.getPreviousGameState() != null) {
            dto.setPreviousGameState(player.getPreviousGameState() != null ? player.getPreviousGameState().getGameStateName() : null);
        }

        dto.setPlayerClass(player.getPlayerClass());

        dto.setPlayerLevel(player.getPlayerLevel() );

        dto.setPlayerStats(player.getPlayerStats());

        dto.getPlayerStats().setStrength(player.getPlayerStats().getStrength());
        dto.getPlayerStats().setDexterity(player.getPlayerStats().getDexterity());
        dto.getPlayerStats().setIntelligence(player.getPlayerStats().getIntelligence());
        dto.getPlayerStats().setLuck(player.getPlayerStats().getLuck());
        dto.getPlayerStats().setCharisma(player.getPlayerStats().getCharisma());
        dto.getPlayerStats().setVitality(player.getPlayerStats().getVitality());


        return dto;
    }

    default NpcDTO npcToNpcDTO(NPC npc) {

        NpcDTO dto = new NpcDTO();

        dto.setNpcPersonality(npc.getNpcPersonality());

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

        return new EnemyDTO();
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

        // Handle armors
        if (dto.getEquippedArmorUUIDs() != null) {
            for (UUID armorUUID : dto.getEquippedArmorUUIDs()) {
                Item armor = Item.itemMap.get(armorUUID);
                entity.equipArmor((Armor) armor);
            }
        }

        if (dto.getCurrentWeaponUUID() != null) {
            Item weapon = Item.itemMap.get(dto.getCurrentWeaponUUID());
            entity.setCurrentWeapon((Weapon) weapon);
        }

        if (dto.getCurrentLocationUUID() != null) {
            entity.setCurrentLocation(Location.locationMap.get(dto.getCurrentLocationUUID()));
        }

        if (dto.getInventory() != null && dto.getInventory().getItemUUIDs() != null) {
            for (UUID itemUUID : dto.getInventory().getItemUUIDs()) {
                Item item = Item.itemMap.get(itemUUID);
                entity.getInventory().addItem(item);
            }
        }

        // Handle current weapon
        return entity;
    }

    default Player playerDtoToPlayer(PlayerDTO dto) {
        if (dto == null) return null;

        Player player = new Player(dto.getEntityName(), dto.getEntityUUID(), dto.getPlayerClass());

        player.setPlayerStats(dto.getPlayerStats());
        player.setPlayerLevel(dto.getPlayerLevel());
        
        // Set player stats
        player.getPlayerStats().setStrength(dto.getPlayerStats().getStrength());
        player.getPlayerStats().setDexterity(dto.getPlayerStats().getDexterity());
        player.getPlayerStats().setIntelligence(dto.getPlayerStats().getIntelligence());
        player.getPlayerStats().setLuck(dto.getPlayerStats().getLuck());
        player.getPlayerStats().setCharisma(dto.getPlayerStats().getCharisma());
        player.getPlayerStats().setVitality(dto.getPlayerStats().getVitality());

        if (Location.locationMap.containsKey(dto.getCurrentLocationUUID())) {
            player.setCurrentLocation(Location.locationMap.get(dto.getCurrentLocationUUID()));
        } else {
            player.setCurrentLocation(new World(UUID.randomUUID()));
        }

        // Create GameLoop without handling state
        GameLoop gameLoop = new GameLoop(player, false);

        // Set game states based on saved data
        String gameStateName = dto.getGameState();
        String previousGameStateName = dto.getPreviousGameState();

        if (gameStateName != null) {
            switch (gameStateName) {
                case "DungeonState":
                    player.setGameState(new DungeonState(gameLoop));
                    break;
                case "ExploringState":
                    player.setGameState(new ExploringState(gameLoop, false));
                    break;
                case "TownState":
                    player.setGameState(new TownState(gameLoop));
                    break;
                case "ShopState":
                    player.setGameState(new ShopState(gameLoop));
                    break;
            }
        }

//        if (previousGameStateName != null) {
//            switch (previousGameStateName) {
//                case "DungeonState":
//                    player.setPreviousGameState(new DungeonState(gameLoop));
//                    break;
//                case "ExploringState":
//                    player.setPreviousGameState(new ExploringState(gameLoop, false));
//                    break;
//                case "TownState":
//                    player.setPreviousGameState(new TownState(gameLoop));
//                    break;
//                case "ShopState":
//                    player.setPreviousGameState(new ShopState(gameLoop));
//                    break;
//        }
//    }

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