package io.github.joshuacgunn.core.gson;

import io.github.joshuacgunn.core.dto.EntityDTO;
import io.github.joshuacgunn.core.dto.ItemDTO;
import io.github.joshuacgunn.core.entity.Entity;
import io.github.joshuacgunn.core.item.Item;
import io.github.joshuacgunn.core.typeadapter.EntityDTOTypeAdapter;
import io.github.joshuacgunn.core.typeadapter.EntityTypeAdapter;
import io.github.joshuacgunn.core.typeadapter.ItemDTOTypeAdapter;
import io.github.joshuacgunn.core.typeadapter.ItemTypeAdapter;
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
