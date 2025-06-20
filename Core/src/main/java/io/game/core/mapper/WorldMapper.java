package io.game.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorldMapper {

    WorldMapper INSTANCE = Mappers.getMapper(WorldMapper.class);

    
}
