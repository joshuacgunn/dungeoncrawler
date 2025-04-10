package com.joshuacgunn.core.gson;

import com.joshuacgunn.core.dto.ItemDTO;
import com.joshuacgunn.core.item.Item;
import com.joshuacgunn.core.typeadapter.ItemDTOTypeAdapter;
import com.joshuacgunn.core.typeadapter.ItemTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemTypeAdapter())
            .registerTypeAdapter(ItemDTO.class, new ItemDTOTypeAdapter())
            .setPrettyPrinting()
            .create();
}