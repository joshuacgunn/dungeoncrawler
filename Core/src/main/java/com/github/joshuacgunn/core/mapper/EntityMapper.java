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
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
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
        dto.setDead(entity.getDeathStatus());
        if (entity.getCurrentLocation() != null) {
            dto.setCurrentLocationUUID(entity.getCurrentLocation());
        }

        if (entity.getInventory() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            List<UUID> itemUUIDList = new ArrayList<>();

            for (Item item : entity.getInventory().getItems()) {
                itemUUIDList.add(item.getItemUUID());
            }

            inventoryDTO.setItemUUIDs(itemUUIDList);
            dto.setInventory(inventoryDTO);
        }


        // Handle armor items
        if (entity.getArmors() != null) {
            List<UUID> armorUUIDList = new ArrayList<>();
            for (Item item : entity.getArmors()) {
                if (item instanceof Armor) {
                    armorUUIDList.add(item.getItemUUID());
                }
            }
            dto.setEquippedArmorUUIDs(armorUUIDList);
        }

        // Handle current weapon
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
            dto.setCurrentLocationUUID(player.getCurrentLocation());
        }

        dto.setPlayerClass(player.getPlayerClass());

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
        entity.setDeathStatus(dto.isDead());

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
            entity.setCurrentLocation(dto.getCurrentLocationUUID());
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

    /**
     * Converts a PlayerDTO to a Player entity.
     *
     * @param dto the player DTO to convert
     * @return the player entity
     */
    default Player playerDtoToPlayer(PlayerDTO dto) {
        if (dto == null) return null;

        Player player = new Player(dto.getEntityName(), dto.getEntityUUID(), dto.getPlayerClass());

        player.setPlayerStats(dto.getPlayerStats());

        player.getPlayerStats().setStrength(dto.getPlayerStats().getStrength());
        player.getPlayerStats().setDexterity(dto.getPlayerStats().getDexterity());
        player.getPlayerStats().setIntelligence(dto.getPlayerStats().getIntelligence());
        player.getPlayerStats().setLuck(dto.getPlayerStats().getLuck());
        player.getPlayerStats().setCharisma(dto.getPlayerStats().getCharisma());
        player.getPlayerStats().setVitality(dto.getPlayerStats().getVitality());

        return player;
    }

    default NPC npcDtotoNPC(NpcDTO dto) {

        NPC npc = new NPC(dto.getEntityName(), dto.getEntityUUID());

        npc.setNpcPersonality(dto.getNpcPersonality());

        return npc;
    }

    default Enemy enemyDtoToEnemy(EnemyDTO dto) {
        if (dto == null) return null;
        if (dto.getEntityName().equalsIgnoreCase("GOBLIN")) {
            Goblin goblin = new Goblin(dto.getEntityUUID(), false);
            goblin.setEntityHp(dto.getEntityHp());
            goblin.setDeathStatus(dto.isDead());
            return goblin;
        } else if (dto.getEntityName().equalsIgnoreCase("ORC")) {
            Orc orc = new Orc(dto.getEntityUUID(), false);
            orc.setEntityHp(dto.getEntityHp());
            orc.setDeathStatus(dto.isDead());
            return orc;
        } else if (dto.getEntityName().equalsIgnoreCase("TROLL")) {
            Troll troll = new Troll(dto.getEntityUUID(), false);
            troll.setEntityHp(dto.getEntityHp());
            troll.setDeathStatus(dto.isDead());
            return troll;
        }
        Enemy enemy = new Enemy(dto.getEntityName(), dto.getEntityUUID(), dto.getEntityHp()) {
        };
        enemy.setDeathStatus(dto.isDead());
        return enemy;
    }

}