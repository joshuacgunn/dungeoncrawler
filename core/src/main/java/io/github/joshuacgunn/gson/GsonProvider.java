package io.github.joshuacgunn.gson;

import io.github.joshuacgunn.dto.EntityDTO;
import io.github.joshuacgunn.dto.ItemDTO;
import io.github.joshuacgunn.entity.Entity;
import io.github.joshuacgunn.item.Item;
import io.github.joshuacgunn.typeadapter.EntityDTOTypeAdapter;
import io.github.joshuacgunn.typeadapter.EntityTypeAdapter;
import io.github.joshuacgunn.typeadapter.ItemDTOTypeAdapter;
import io.github.joshuacgunn.typeadapter.ItemTypeAdapter;
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