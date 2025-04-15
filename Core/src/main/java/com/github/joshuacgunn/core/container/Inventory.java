package com.github.joshuacgunn.core.container;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.item.Item;

import java.util.UUID;

public class Inventory extends Container {
    private final Entity parentEntity;

    public Inventory(UUID containerUUID, Entity parentEntity) {
        super(containerUUID, (parentEntity + "'s Inventory"));
        this.parentEntity = parentEntity;
    }

    public Entity getOwner() {
        return parentEntity;
    }

    public void dropItem(Item item) {
        if (this.getItems().contains(item)) {
            this.items.remove(item);
        }
    }
}
