package io.github.joshuacgunn.core.mapper;

import io.github.joshuacgunn.core.dto.WorldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorldMapper {

    WorldMapper INSTANCE = Mappers.getMapper(WorldMapper.class);

    default WorldDTO worldToWorldDto() {

        return null;
    }
}
