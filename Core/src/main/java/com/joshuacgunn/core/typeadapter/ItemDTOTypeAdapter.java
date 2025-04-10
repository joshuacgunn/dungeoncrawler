package com.joshuacgunn.core.typeadapter;

import com.joshuacgunn.core.dto.ArmorDTO;
import com.joshuacgunn.core.dto.ItemDTO;
import com.joshuacgunn.core.dto.WeaponDTO;
import com.joshuacgunn.core.item.Armor;
import com.google.gson.*;
import com.joshuacgunn.core.item.Weapon;

import java.lang.reflect.Type;

public class ItemDTOTypeAdapter implements JsonSerializer<ItemDTO>, JsonDeserializer<ItemDTO> {

    @Override
    public JsonElement serialize(ItemDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Common properties
        result.addProperty("itemName", src.getItemName());
        result.addProperty("itemUUID", src.getItemUUID().toString());

        // Type-specific properties
        if (src instanceof WeaponDTO weaponDTO) {
            result.addProperty("itemType", "Weapon");
            result.addProperty("weaponDamage", weaponDTO.getWeaponDamage());
            result.addProperty("weaponDurability", weaponDTO.getWeaponDurability());
            result.addProperty("armorPenetration", weaponDTO.getArmorPenetration());
            result.addProperty("weaponQuality", weaponDTO.getWeaponQuality().name());
        } else if (src instanceof ArmorDTO armorDTO) {
            result.addProperty("itemType", "Armor");
            result.addProperty("armorDefense", armorDTO.getArmorDefense());
            result.addProperty("slot", armorDTO.getSlot().name());
            result.addProperty("quality", armorDTO.getQuality().name());
        } else {
            result.addProperty("itemType", "Item");
        }

        return result;
    }

    @Override
    public ItemDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String itemType = jsonObject.has("itemType") ?
                jsonObject.get("itemType").getAsString() : "Item";

        ItemDTO dto;

        switch (itemType) {
            case "Weapon":
                dto = new WeaponDTO();
                if (jsonObject.has("weaponDamage")) {
                    ((WeaponDTO) dto).setWeaponDamage(jsonObject.get("weaponDamage").getAsFloat());
                }
                if (jsonObject.has("weaponDurability")) {
                    ((WeaponDTO) dto).setWeaponDurability(jsonObject.get("weaponDurability").getAsFloat());
                }
                if (jsonObject.has("armorPenetration")) {
                    ((WeaponDTO) dto).setArmorPenetration(jsonObject.get("armorPenetration").getAsFloat());
                }
                if (jsonObject.has("weaponQuality")) {
                    try {
                        String qualityStr = jsonObject.get("weaponQuality").getAsString();
                        ((WeaponDTO) dto).setWeaponQuality(Weapon.WeaponQuality.valueOf(qualityStr));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid weapon quality value: " + jsonObject.get("weaponQuality").getAsString());
                    }
                }
                break;

            case "Armor":
                dto = new ArmorDTO();
                if (jsonObject.has("armorDefense")) {
                    ((ArmorDTO) dto).setArmorDefense(jsonObject.get("armorDefense").getAsFloat());
                }

                // Safely parse armor slot
                if (jsonObject.has("slot")) {
                    try {
                        String slotStr = jsonObject.get("slot").getAsString();
                        ((ArmorDTO) dto).setSlot(Armor.ArmorSlot.valueOf(slotStr));
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                        System.err.println("Invalid armor slot value: " + jsonObject.get("slot").getAsString());
                    }
                }

                // Safely parse armor quality
                if (jsonObject.has("quality")) {
                    try {
                        String qualityStr = jsonObject.get("quality").getAsString();
                        ((ArmorDTO) dto).setQuality(Armor.ArmorQuality.valueOf(qualityStr));
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                        System.err.println("Invalid armor quality value: " + jsonObject.get("quality").getAsString());
                    }
                }
                break;

            default:
                dto = new ItemDTO();
                break;
        }

        // Set common properties
        if (jsonObject.has("itemName")) {
            dto.setItemName(jsonObject.get("itemName").getAsString());
        }

        if (jsonObject.has("itemUUID")) {
            dto.setItemUUID(java.util.UUID.fromString(jsonObject.get("itemUUID").getAsString()));
        }

        return dto;
    }
}