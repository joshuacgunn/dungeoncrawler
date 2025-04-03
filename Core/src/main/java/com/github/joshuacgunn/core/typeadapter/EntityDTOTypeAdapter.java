package com.github.joshuacgunn.core.typeadapter;

import com.github.joshuacgunn.core.dto.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing EntityDTO objects with GSON.
 * <p>
 * This adapter ensures proper handling of the EntityDTO class hierarchy, preserving
 * type information during serialization and recreating the appropriate object type
 * during deserialization.
 */
public class EntityDTOTypeAdapter implements JsonSerializer<EntityDTO>, JsonDeserializer<EntityDTO> {

    /**
     * Serializes an EntityDTO object to JSON.
     * <p>
     * Common entity properties are serialized for all DTOs, and subclass-specific
     * properties are included based on the concrete type.
     *
     * @param src The EntityDTO object to serialize
     * @param typeOfSrc The runtime type of the source object
     * @param context The serialization context
     * @return A JsonElement representing the serialized object
     */
    @Override
    public JsonElement serialize(EntityDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Serialize common EntityDTO properties
        result.addProperty("entityName", src.getEntityName());
        result.addProperty("entityUUID", src.getEntityUUID().toString());
        result.addProperty("entityHp", src.getEntityHp());
        result.addProperty("isDead", src.isDead());
        result.addProperty("entityDefense", src.getEntityDefense());
        result.addProperty("entityType", src.getEntityType());

        // Serialize current weapon if present
        if (src.getCurrentWeapon() != null) {
            result.add("currentWeapon", context.serialize(src.getCurrentWeapon()));
        }

        // Serialize armors if present
        if (src.getEquippedArmors() != null && !src.getEquippedArmors().isEmpty()) {
            result.add("armors", context.serialize(src.getEquippedArmors()));
        }

        // Handle PlayerDTO-specific fields
        if (src instanceof PlayerDTO playerDTO) {
            if (playerDTO.getCurrentDungeon() != null) {
                result.add("currentDungeon", context.serialize(playerDTO.getCurrentDungeon()));
            }
            if (playerDTO.getInventory() != null) {
                result.add("inventory", context.serialize(playerDTO.getInventory()));
            }
        }

        // Handle EnemyDTO-specific fields if needed
        if (src instanceof EnemyDTO enemyDTO) {
            if (enemyDTO.getInventory() != null) {
                result.add("inventory", context.serialize(enemyDTO.getInventory()));
            }
        }
        return result;
    }

    /**
     * Deserializes a JSON element to an appropriate EntityDTO subclass.
     * <p>
     * Examines the "entityType" field to determine which type of object to create.
     *
     * @param json The JSON element to deserialize
     * @param typeOfT The type of the object to deserialize to
     * @param context The deserialization context
     * @return An EntityDTO or subclass object populated with data from the JSON
     * @throws JsonParseException If the JSON structure is invalid or missing required fields
     */
    @Override
    public EntityDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Determine the entity type
        String entityType = jsonObject.has("entityType") ?
                jsonObject.get("entityType").getAsString() : "Entity";

        // Create the appropriate DTO based on entityType
        EntityDTO dto;
        switch (entityType) {
            case "Player":
                dto = new PlayerDTO();

                // Set player-specific fields
                if (jsonObject.has("currentDungeon")) {
                    ((PlayerDTO) dto).setCurrentDungeon(
                            context.deserialize(jsonObject.get("currentDungeon"), DungeonDTO.class));
                }
                if (jsonObject.has("inventory")) {
                    ((PlayerDTO) dto).setInventory(
                            context.deserialize(jsonObject.get("inventory"), InventoryDTO.class));
                }
                break;

            case "Enemy":
            case "Goblin":
                dto = new EnemyDTO();
                if (jsonObject.has("inventory")) {
                    ((EnemyDTO) dto).setInventory(
                            context.deserialize(jsonObject.get("inventory"), InventoryDTO.class));
                }
                break;

            default:
                dto = new EntityDTO();
        }

        // Set common entity fields
        dto.setEntityType(entityType);
        dto.setEntityName(jsonObject.get("entityName").getAsString());
        dto.setEntityUUID(UUID.fromString(jsonObject.get("entityUUID").getAsString()));
        dto.setEntityHp(jsonObject.get("entityHp").getAsFloat());

        if (jsonObject.has("isDead")) {
            dto.setDead(jsonObject.get("isDead").getAsBoolean());
        }

        if (jsonObject.has("entityDefense")) {
            dto.setEntityDefense(jsonObject.get("entityDefense").getAsFloat());
        }

        // Handle current weapon
        if (jsonObject.has("currentWeapon")) {
            dto.setCurrentWeapon(context.deserialize(
                    jsonObject.get("currentWeapon"), WeaponDTO.class));
        }

        // Handle armors list
        if (jsonObject.has("armors")) {
            dto.setEquippedArmors(context.deserialize(
                    jsonObject.get("armors"), new TypeToken<List<ArmorDTO>>(){}.getClass()));
        }

        return dto;
    }

    // Helper type token class for generic type handling
    private static class TypeToken<T> implements Type {
        // This is a placeholder - you would need to implement this properly
        // or use Gson's TypeToken utility if available
    }
}