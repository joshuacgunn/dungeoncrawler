package io.game.core.typeadapter;

import io.game.core.dto.ArmorDTO;
import io.game.core.dto.ItemDTO;
import io.game.core.dto.WeaponDTO;
import io.game.core.item.Armor;
import io.game.core.item.Item;
import com.google.gson.*;
import io.game.core.item.Weapon;

import java.lang.reflect.Type;

public class ItemDTOTypeAdapter implements JsonSerializer<ItemDTO>, JsonDeserializer<ItemDTO> {

    @Override
    public JsonElement serialize(ItemDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        // Common properties
        result.addProperty("itemName", src.getItemName());
        result.addProperty("itemUUID", src.getItemUUID().toString());
        result.addProperty("itemRarity", src.getItemRarity().name());

        // Type-specific properties
        if (src instanceof WeaponDTO weaponDTO) {
            result.addProperty("itemType", "Weapon");
            result.addProperty("weaponDamage", weaponDTO.getWeaponDamage());
            result.addProperty("weaponDurability", weaponDTO.getWeaponDurability());
            result.addProperty("armorPenetration", weaponDTO.getArmorPenetration());
            result.addProperty("weaponQuality", weaponDTO.getWeaponQuality().name());
            result.addProperty("weaponMaterial", weaponDTO.getWeaponMaterial().name());
        } else if (src instanceof ArmorDTO armorDTO) {
            result.addProperty("itemType", "Armor");
            result.addProperty("armorDefense", armorDTO.getArmorDefense());
            result.addProperty("armorSlot", armorDTO.getArmorSlot().name());
            result.addProperty("armorQuality", armorDTO.getArmorQuality().name());
            result.addProperty("armorMaterial", armorDTO.getArmorMaterial().name());
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
                if (jsonObject.has("weaponMaterial")) {
                    try {
                        String materialStr = jsonObject.get("weaponMaterial").getAsString();
                        ((WeaponDTO) dto).setWeaponMaterial(Weapon.WeaponMaterial.valueOf(materialStr));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid weapon material value: " + jsonObject.get("weaponMaterial").getAsString());
                    }
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
                if (jsonObject.has("armorSlot")) {
                    try {
                        String slotStr = jsonObject.get("armorSlot").getAsString();
                        ((ArmorDTO) dto).setArmorSlot(Armor.ArmorSlot.valueOf(slotStr));
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                        System.err.println("Invalid armor slot value: " + jsonObject.get("armorSlot").getAsString());
                    }
                }

                if (jsonObject.has("armorMaterial")) {
                    try {
                        String materialStr = jsonObject.get("armorMaterial").getAsString();
                        ((ArmorDTO) dto).setArmorMaterial(Armor.ArmorMaterial.valueOf(materialStr));
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                        System.err.println("Invalid armor slot value: " + jsonObject.get("armorMaterial").getAsString());
                    }
                }

                // Safely parse armor quality
                if (jsonObject.has("armorQuality")) {
                    try {
                        String qualityStr = jsonObject.get("armorQuality").getAsString();
                        ((ArmorDTO) dto).setArmorQuality(Armor.ArmorQuality.valueOf(qualityStr));
                    } catch (IllegalArgumentException e) {
                        // Handle invalid enum value
                        System.err.println("Invalid armor quality value: " + jsonObject.get("armorQuality").getAsString());
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

        if (jsonObject.has("itemRarity")) {
            String rarityStr = jsonObject.get("itemRarity").getAsString();
            dto.setItemRarity(Item.ItemRarity.valueOf(rarityStr));
        }

        return dto;
    }
}