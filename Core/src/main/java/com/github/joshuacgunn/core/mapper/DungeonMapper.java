package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.DungeonDTO;
import com.github.joshuacgunn.core.dto.DungeonFloorDTO;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.DungeonFloor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DungeonMapper {

    DungeonMapper INSTANCE = Mappers.getMapper(DungeonMapper.class);

    @Mapping(target = "floors", source = "floors")
    @Mapping(target = "currentFloorUUID", expression = "java(dungeon.getCurrentFloor() != null ? dungeon.getCurrentFloor().getFloorUUID() : null)")

    DungeonDTO dungeonToDungeonDto(Dungeon dungeon);

    @Mapping(target = "locationMap", ignore = true)
    default Dungeon dungeonDtoToDungeon(DungeonDTO dungeonDTO) {
        Dungeon dungeon = new Dungeon(dungeonDTO.getLocationName(), dungeonDTO.getLocationUUID());

        dungeon.getFloors().clear();

        for (DungeonFloorDTO floorDTO : dungeonDTO.getFloors()) {
            DungeonFloor floor = new DungeonFloor(floorDTO.getFloorUUID(), dungeon, floorDTO.getFloorNumber());
            dungeon.getFloors().add(floor);

            if (floorDTO.getFloorUUID().equals(dungeonDTO.getCurrentFloorUUID())) {
                dungeon.setCurrentFloor(floor);
            }
        }
        return dungeon;
    }


}
