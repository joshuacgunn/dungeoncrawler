package io.game.core.gson;

import io.game.core.dto.EntityDTO;
import io.game.core.dto.ItemDTO;
import io.game.core.entity.Entity;
import io.game.core.item.Item;
import io.game.core.typeadapter.EntityDTOTypeAdapter;
import io.game.core.typeadapter.EntityTypeAdapter;
import io.game.core.typeadapter.ItemDTOTypeAdapter;
import io.game.core.typeadapter.ItemTypeAdapter;
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