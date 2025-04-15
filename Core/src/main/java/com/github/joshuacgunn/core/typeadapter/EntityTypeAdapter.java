package com.github.joshuacgunn.core.typeadapter;

import com.google.gson.*;
import com.github.joshuacgunn.core.entity.*;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Location;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing Entity objects with GSON.
 * <p>
 * This adapter handles the Entity class hierarchy, preserving type information
 * during serialization and recreating the appropriate object type during deserialization.
 */
public class EntityTypeAdapter implements JsonSerializer<Entity>, JsonDeserializer<Entity> {

    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Common entity properties
        result.addProperty("entityType", src.getClass().getSimpleName());
        result.addProperty("entityName", src.getEntityName());
        result.addProperty("entityUUID", src.getEntityUUID().toString());
        result.addProperty("entityHp", src.getEntityHp());
        result.addProperty("isDead", src.getDeathStatus());
        result.addProperty("entityDefense", src.getEntityDefense());

        // Current location
        if (src.getCurrentLocation() != null) {
            result.addProperty("currentLocationUUID", src.getCurrentLocation().toString());
        }

        // Current weapon
        if (src.getCurrentWeapon() != null) {
            result.addProperty("currentWeaponUUID", src.getCurrentWeapon().getItemUUID().toString());
        }

        // Equipped armor items
        if (src.getArmors() != null && !src.getArmors().isEmpty()) {
            JsonArray armorArray = new JsonArray();
            for (Item item : src.getArmors()) {
                armorArray.add(item.getItemUUID().toString());
            }
            result.add("equippedArmorUUIDs", armorArray);
        }

        // Inventory items (store as UUIDs)
        if (src.getInventory() != null && !src.getInventory().getItems().isEmpty()) {
            JsonArray inventoryArray = new JsonArray();
            for (Item item : src.getInventory().getItems()) {
                inventoryArray.add(item.getItemUUID().toString());
            }
            result.add("inventoryItemUUIDs", inventoryArray);
        }

        // NPC-specific properties
        if (src instanceof NPC npc) {
            result.addProperty("npcPersonality", npc.getNpcPersonality().name());
        }

        return result;
    }

    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("entityType") || !jsonObject.has("entityUUID") || !jsonObject.has("entityName")) {
            throw new JsonParseException("Invalid Entity JSON: missing required fields");
        }

        String entityType = jsonObject.get("entityType").getAsString();
        UUID entityUUID = UUID.fromString(jsonObject.get("entityUUID").getAsString());
        String entityName = jsonObject.get("entityName").getAsString();

        Entity entity;

        // Create appropriate entity subclass based on type
        switch (entityType) {
            case "Player":
                entity = new Player(entityName, entityUUID);
                break;

            case "NPC":
                entity = new NPC(entityName, entityUUID);

                // Set NPC personality if present
                if (jsonObject.has("npcPersonality")) {
                    NPC.Personality personality = NPC.Personality.valueOf(jsonObject.get("npcPersonality").getAsString());
                    ((NPC) entity).setNpcPersonality(personality);
                }
                break;

            case "Goblin":
                entity = new Goblin(entityUUID);
                break;

            case "Enemy":
                // Handle abstract Enemy class with anonymous implementation
                float hp = jsonObject.has("entityHp") ? jsonObject.get("entityHp").getAsFloat() : 0f;
                entity = new Enemy(entityName, entityUUID, hp) {};
                break;

            default:
                throw new JsonParseException("Unknown entity type: " + entityType);
        }

        // Set common entity properties
        if (jsonObject.has("entityHp")) {
            entity.setEntityHp(jsonObject.get("entityHp").getAsFloat());
        }

        if (jsonObject.has("isDead")) {
            entity.setDeathStatus(jsonObject.get("isDead").getAsBoolean());
        }

        // Set current location if present
        if (jsonObject.has("currentLocationUUID")) {
            UUID locationUUID = UUID.fromString(jsonObject.get("currentLocationUUID").getAsString());
            if (Location.locationMap.containsKey(locationUUID)) {
                entity.setCurrentLocation(locationUUID);
            }
        }

        // Set current weapon if present
        if (jsonObject.has("currentWeaponUUID")) {
            UUID weaponUUID = UUID.fromString(jsonObject.get("currentWeaponUUID").getAsString());
            if (Item.itemMap.containsKey(weaponUUID)) {
                Weapon weapon = (Weapon) Item.itemMap.get(weaponUUID);
                entity.setCurrentWeapon(weapon);
            }
        }

        // Set equipped armor items
        if (jsonObject.has("equippedArmorUUIDs") && jsonObject.get("equippedArmorUUIDs").isJsonArray()) {
            JsonArray armorArray = jsonObject.getAsJsonArray("equippedArmorUUIDs");
            for (JsonElement element : armorArray) {
                UUID armorUUID = UUID.fromString(element.getAsString());
                if (Item.itemMap.containsKey(armorUUID)) {
                    Item item = Item.itemMap.get(armorUUID);
                    if (item instanceof Armor) {
                        entity.equipArmor((Armor) item);
                    }
                }
            }
        }

        // Add inventory items
        if (jsonObject.has("inventoryItemUUIDs") && jsonObject.get("inventoryItemUUIDs").isJsonArray()) {
            JsonArray inventoryArray = jsonObject.getAsJsonArray("inventoryItemUUIDs");
            for (JsonElement element : inventoryArray) {
                UUID itemUUID = UUID.fromString(element.getAsString());
                if (Item.itemMap.containsKey(itemUUID)) {
                    entity.addItem(Item.itemMap.get(itemUUID));
                }
            }
        }

        return entity;
    }
}