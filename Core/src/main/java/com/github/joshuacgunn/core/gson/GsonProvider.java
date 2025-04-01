package com.github.joshuacgunn.core.gson;

import com.github.joshuacgunn.core.dto.ItemDTO;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.typeadapter.ItemDTOTypeAdapter;
import com.github.joshuacgunn.core.typeadapter.ItemTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemTypeAdapter())
            .registerTypeAdapter(ItemDTO.class, new ItemDTOTypeAdapter())
            .setPrettyPrinting()
            .create();
}