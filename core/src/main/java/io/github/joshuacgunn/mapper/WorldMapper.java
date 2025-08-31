package io.github.joshuacgunn.mapper;

import io.github.joshuacgunn.dto.WorldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorldMapper {

    WorldMapper INSTANCE = Mappers.getMapper(WorldMapper.class);

    default WorldDTO worldToWorldDto() {

        return null;
    }
}
