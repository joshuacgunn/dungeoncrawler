package com.joshuacgunn.core.mapper;

import com.joshuacgunn.core.dto.ShopDTO;
import com.joshuacgunn.core.dto.TownDTO;
import com.joshuacgunn.core.entity.Entity;
import com.joshuacgunn.core.location.Shop;
import com.joshuacgunn.core.location.Town;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.UUID;

@Mapper
public interface TownMapper {

    TownMapper INSTANCE = Mappers.getMapper(TownMapper.class);

    default TownDTO townToTownDto(Town town) {
        TownDTO dto = new TownDTO();

        dto.setShopCount(town.getShopCount());
        dto.setTownUUID(town.getLocationUUID());
        dto.setTownName(town.getLocationName());

        ArrayList<ShopDTO> shopsInTown = new ArrayList<>();
        for (Shop shop : town.getShopsInTown()) {
            ShopDTO shopDTO = new ShopDTO();
            shopDTO.setShopOwnerUUID(shop.getShopOwner().getEntityUUID());
            shopDTO.setShopUUID(shop.getLocationUUID());
            shopDTO.setShopName(shop.getLocationName());

            shopsInTown.add(shopDTO);
        }
        dto.setShopsInTown(shopsInTown);
        return dto;
    }

    @Mapping(target="locationMap", ignore = true)
    default Town townDtoToTown(TownDTO townDTO) {
        Town town = new Town(townDTO.getTownName(), townDTO.getTownUUID(), townDTO.getShopCount(), false);

        ArrayList<Shop> shopsInTown = new ArrayList<>();
        for (ShopDTO shopDTO : townDTO.getShopsInTown()) {
            Shop shop = new Shop(shopDTO.getShopName(), shopDTO.getShopUUID(), Entity.entityMap.get(shopDTO.getShopOwnerUUID()));
            shopsInTown.add(shop);
        }
        town.setShopsInTown(shopsInTown);
        return town;
    }
}
