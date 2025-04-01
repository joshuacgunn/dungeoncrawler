package com.github.joshuacgunn.core.typeadapter;

import com.github.joshuacgunn.core.dto.ArmorDTO;
import com.github.joshuacgunn.core.dto.ItemDTO;
import com.github.joshuacgunn.core.dto.WeaponDTO;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing ItemDTO objects with GSON.
 * <p>
 * This adapter ensures proper handling of the ItemDTO class hierarchy, particularly
 * between base ItemDTO objects and WeaponDTO subclasses. It preserves type information
 * during serialization and recreates the appropriate object type during deserialization.
 * <p>
 * Key responsibilities:
 * - Serializes ItemDTO and WeaponDTO objects with all their properties
 * - Ensures weapon-specific properties are included when serializing WeaponDTO objects
 * - Determines the correct object type to create during deserialization based on itemType
 */
public class ItemDTOTypeAdapter implements JsonSerializer<ItemDTO>, JsonDeserializer<ItemDTO> {

    /**
     * Serializes an ItemDTO object to JSON.
     * <p>
     * Common properties (itemName, itemUUID, itemType) are serialized for all DTOs.
     * For WeaponDTO objects, additional weapon-specific properties (weaponDamage,
     * weaponDurability) are also included in the JSON output.
     *
     * @param src The ItemDTO object to serialize
     * @param typeOfSrc The runtime type of the source object
     * @param context The serialization context
     * @return A JsonElement representing the serialized object
     */
    @Override
    public JsonElement serialize(ItemDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Common properties
        result.addProperty("itemName", src.getItemName());
        result.addProperty("itemUUID", src.getItemUUID().toString());
        result.addProperty("itemType", src.getItemType());

        // Set weapon-specific properties
        if (src instanceof WeaponDTO weaponDTO) {
            result.addProperty("weaponDamage", weaponDTO.getWeaponDamage());
            result.addProperty("weaponDurability", weaponDTO.getWeaponDurability());
        } else if (src instanceof ArmorDTO armorDTO) {
            result.addProperty("armorSlot", armorDTO.getSlot());
            result.addProperty("armorDefense", armorDTO.getArmorDefense());
        }

        return result;
    }

    /**
     * Deserializes a JSON element to an appropriate ItemDTO or WeaponDTO object.
     * <p>
     * The method examines the "itemType" field to determine which type of object
     * to create. For "Weapon" types, it creates a WeaponDTO with all weapon-specific
     * properties; otherwise, it creates a standard ItemDTO.
     *
     * @param json The JSON element to deserialize
     * @param typeOfT The type of the object to deserialize to
     * @param context The deserialization context
     * @return An ItemDTO or WeaponDTO object populated with data from the JSON
     * @throws JsonParseException If the JSON structure is invalid or missing required fields
     */
    @Override
    public ItemDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Determine the type
        String itemType = jsonObject.has("itemType") ? jsonObject.get("itemType").getAsString() : "Item";

        if (itemType.equals("Weapon")) {
            // Create WeaponDTO
            WeaponDTO weaponDTO = new WeaponDTO();
            weaponDTO.setItemName(jsonObject.get("itemName").getAsString());
            weaponDTO.setItemUUID(UUID.fromString(jsonObject.get("itemUUID").getAsString()));
            weaponDTO.setWeaponDamage(jsonObject.get("weaponDamage").getAsFloat());
            weaponDTO.setWeaponDurability(jsonObject.get("weaponDurability").getAsFloat());
            return weaponDTO;
        } else if (itemType.equals("Armor")) {
            ArmorDTO armorDTO = new ArmorDTO();
            armorDTO.setItemName(jsonObject.get("itemName").getAsString());
            armorDTO.setItemUUID(UUID.fromString(jsonObject.get("itemUUID").getAsString()));
            armorDTO.setArmorDefense(jsonObject.get("armorDefense").getAsFloat());
            armorDTO.setSlot(jsonObject.get("armorSlot").getAsString());
            return armorDTO;
        } else {
            // Create regular ItemDTO
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemName(jsonObject.get("itemName").getAsString());
            itemDTO.setItemUUID(UUID.fromString(jsonObject.get("itemUUID").getAsString()));
            itemDTO.setItemType(itemType);
            return itemDTO;
        }
    }
}