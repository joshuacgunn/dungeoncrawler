package com.github.joshuacgunn.core.typeadapter;

import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ItemTypeAdapter implements JsonSerializer<Item>, JsonDeserializer<Item> {

    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.addProperty("itemType", src.getClass().getSimpleName());
        return jsonObject;
    }

    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Handle case where itemType is missing
        if (!jsonObject.has("itemType")) {
            // Check if weapon properties exist
            if (jsonObject.has("weaponDamage") || jsonObject.has("weaponDurability")) {
                return context.deserialize(jsonObject, Weapon.class);
            } else if (jsonObject.has("armorSlot") || jsonObject.has("armorDefense")) {
                return context.deserialize(jsonObject, Armor.class);
            }
            return context.deserialize(jsonObject, Item.class);
        }

        String itemType = jsonObject.get("itemType").getAsString();
        Class<? extends Item> itemClass = switch (itemType) {
            case "Weapon", "WEAPON" -> Weapon.class;
            case "Armor", "ARMOR" -> Armor.class;
            default -> Item.class;
        };

        return context.deserialize(jsonObject, itemClass);
    }
}