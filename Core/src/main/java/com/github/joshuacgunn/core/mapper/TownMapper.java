package com.github.joshuacgunn.core.mapper;

import com.github.joshuacgunn.core.dto.ShopDTO;
import com.github.joshuacgunn.core.dto.TownDTO;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Array;
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
            shopDTO.setShopType(shop.getShopType());
            ArrayList<UUID> npcUUIDs = new ArrayList<>();
            for (NPC npc : shop.getNpcsInShop()) {
                npcUUIDs.add(npc.getEntityUUID());
            }
            shopDTO.setNpcsInShop(npcUUIDs);

            shopsInTown.add(shopDTO);
        }
        dto.setShopsInTown(shopsInTown);
        return dto;
    }

    @Mapping(target="locationMap", ignore = true)
    default Town townDtoToTown(TownDTO townDTO) {
        Town town = new Town(townDTO.getTownUUID(), false);
        town.setShopCount(townDTO.getShopCount());
        town.setLocationName(townDTO.getTownName());

        ArrayList<Shop> shopsInTown = new ArrayList<>();
        for (ShopDTO shopDTO : townDTO.getShopsInTown()) {
            Shop shop = new Shop(shopDTO.getShopType(), shopDTO.getShopUUID(), (NPC) Entity.entityMap.get(shopDTO.getShopOwnerUUID()), false);
            shopsInTown.add(shop);
            ArrayList<NPC> npcsInShop = new ArrayList<>();
            for (UUID npcUUID : shopDTO.getNpcsInShop()) {
                npcsInShop.add((NPC) Entity.entityMap.get(npcUUID));
            }
        }
        town.setShopsInTown(shopsInTown);
        return town;
    }
}
