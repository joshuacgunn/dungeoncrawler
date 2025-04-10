package com.joshuacgunn.core.mapper;

import com.joshuacgunn.core.dto.*;
import com.joshuacgunn.core.dto.DungeonDTO;
import com.joshuacgunn.core.dto.DungeonFloorDTO;
import com.joshuacgunn.core.entity.Enemy;
import com.joshuacgunn.core.entity.Entity;
import com.joshuacgunn.core.item.Item;
import com.joshuacgunn.core.location.Dungeon;
import com.joshuacgunn.core.location.DungeonFloor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface DungeonMapper {

    DungeonMapper INSTANCE = Mappers.getMapper(DungeonMapper.class);

    default DungeonDTO dungeonToDungeonDto(Dungeon dungeon) {
        if (dungeon == null) return null;

        DungeonDTO dungeonDTO = new DungeonDTO();
        dungeonDTO.setDungeonName(dungeon.getLocationName());
        dungeonDTO.setDungeonUUID(dungeon.getLocationUUID());
        dungeonDTO.setCurrentFloorUUID(dungeon.getCurrentFloor().getFloorUUID());
        ArrayList<DungeonFloorDTO> floorDTOs = new ArrayList<>();
        float difficulty = 0;
        for (DungeonFloor floor : dungeon.getFloors()) {
            DungeonFloorDTO floorDTO = new DungeonFloorDTO();
            floorDTO.setFloorNumber(floor.getFloorNumber());
            floorDTO.setFloorUUID(floor.getFloorUUID());
            floorDTO.setDifficultyRating(floor.getDifficultyRating());

            if (floor.isHasChest()) {
                floorDTO.setHasChest(true);
                floorDTO.setChest(new ChestDTO());
                floorDTO.getChest().setChestRarity(floor.getChest().getChestRarity());
                floorDTO.getChest().setChestUUID(floor.getChest().getContainerUUID());
                List<UUID> chestContents = new ArrayList<>();
                for (Item item : floor.getChest().getItems()) {
                    chestContents.add(item.getItemUUID());
                }
                floorDTO.getChest().setChestContents(chestContents);
            }

            difficulty += floor.getDifficultyRating();

            ArrayList<UUID> enemyUUIDs = new ArrayList<>();
            for (Enemy enemy : floor.getEnemiesOnFloor()) {
                enemyUUIDs.add(enemy.getEntityUUID());
            }
            floorDTO.setEnemyUUIDs(enemyUUIDs);

            floorDTOs.add(floorDTO);
        }
        dungeonDTO.setFloors(floorDTOs);
        dungeonDTO.setDifficultyRating(difficulty);
        return dungeonDTO;
    }

    @Mapping(target = "locationMap", ignore = true)
    default Dungeon dungeonDtoToDungeon(DungeonDTO dungeonDTO) {
        Dungeon dungeon = new Dungeon(dungeonDTO.getDungeonName(), dungeonDTO.getDungeonUUID(), false);

        for (DungeonFloorDTO floorDTO : dungeonDTO.getFloors()) {
            DungeonFloor floor = new DungeonFloor(floorDTO.getFloorUUID(), dungeon, floorDTO.getFloorNumber(), true);

            ArrayList<Enemy> enemies = new ArrayList<>();
            for (UUID enemyUUID : floorDTO.getEnemyUUIDs()) {
                Entity entity = Entity.entityMap.get(enemyUUID);
                if (entity instanceof Enemy) {
                    enemies.add((Enemy) entity);
                }
            }

            floor.setEnemiesOnFloor(enemies);

            floor.setDifficultyRating(floor.calculateDifficulty());

            if (floorDTO.hasChest()) {
                floor.setHasChest(true);
                for (UUID itemDTO : floorDTO.getChest().getChestContents()) {
                    Item item = Item.itemMap.get(itemDTO);
                    floor.getChest().addItem(item);
                }
            }

            dungeon.getFloors().add(floor);

            if (floorDTO.getFloorUUID().equals(dungeonDTO.getCurrentFloorUUID())) {
                dungeon.setCurrentFloor(floor);
            }
        }
        dungeon.setDifficultyRating(dungeonDTO.getDifficultyRating());
        return dungeon;
    }


}
