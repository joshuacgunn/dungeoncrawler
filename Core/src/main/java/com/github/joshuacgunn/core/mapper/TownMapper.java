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

import java.util.ArrayList;
import java.util.UUID;

/**
 * A MapStruct mapper interface that handles conversion between Town domain objects
 * and their corresponding DTOs (Data Transfer Objects).
 * Manages the complex relationships between towns, shops, and NPCs while
 * maintaining data integrity during serialization and deserialization.
 */
@Mapper
public interface TownMapper {

    /** Singleton instance of the TownMapper */
    TownMapper INSTANCE = Mappers.getMapper(TownMapper.class);

    /**
     * Converts a Town domain object to its DTO representation.
     * Maps the complete town structure including all shops and their associated NPCs.
     *
     * Mapped properties include:
     * - Town attributes (name, UUID, shop count)
     * - Shop collection with their types and UUIDs
     * - Shop owner references
     * - NPCs present in each shop
     *
     * @param town The source Town object to convert
     * @return TownDTO containing the mapped town data and its internal structure
     */
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
            shopDTO.setParentTownUUID(town.getLocationUUID());
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

    /**
     * Converts a TownDTO back to a Town domain object.
     * Reconstructs the complete town structure while maintaining proper object references
     * and preventing circular dependencies.
     *
     * Features:
     * - Creates a non-generating town instance
     * - Restores shop collection with proper types
     * - Relinks shop owners from entity map
     * - Rebuilds NPC collections for each shop
     * - Ignores locationMap to prevent circular references
     *
     * @param townDTO The TownDTO to convert back to a Town
     * @return Town object containing the reconstructed town structure
     */
    @Mapping(target="locationMap", ignore = true)
    default Town townDtoToTown(TownDTO townDTO) {
        Town town = new Town(townDTO.getTownUUID(), false);
        town.setShopCount(townDTO.getShopCount());
        town.setLocationName(townDTO.getTownName());

        ArrayList<Shop> shopsInTown = new ArrayList<>();
        for (ShopDTO shopDTO : townDTO.getShopsInTown()) {
            Shop shop = new Shop(shopDTO.getShopType(), shopDTO.getShopUUID(), (NPC) Entity.entityMap.get(shopDTO.getShopOwnerUUID()), false);
            shop.setParentTown(town);
            shopsInTown.add(shop);
            ArrayList<NPC> npcsInShop = new ArrayList<>();
            for (UUID npcUUID : shopDTO.getNpcsInShop()) {
                npcsInShop.add((NPC) Entity.entityMap.get(npcUUID));
            }
            shop.setNpcsInShop(npcsInShop);
        }
        town.setShopsInTown(shopsInTown);
        return town;
    }
}
