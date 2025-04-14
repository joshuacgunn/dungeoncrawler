package com.github.joshuacgunn.core.gson;

import com.github.joshuacgunn.core.dto.EntityDTO;
import com.github.joshuacgunn.core.dto.ItemDTO;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.typeadapter.EntityDTOTypeAdapter;
import com.github.joshuacgunn.core.typeadapter.EntityTypeAdapter;
import com.github.joshuacgunn.core.typeadapter.ItemDTOTypeAdapter;
import com.github.joshuacgunn.core.typeadapter.ItemTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemTypeAdapter())
            .registerTypeAdapter(ItemDTO.class, new ItemDTOTypeAdapter())
            .registerTypeAdapter(Entity.class, new EntityTypeAdapter())
            .registerTypeAdapter(EntityDTO.class, new EntityDTOTypeAdapter())
            .setPrettyPrinting()
            .create();
}