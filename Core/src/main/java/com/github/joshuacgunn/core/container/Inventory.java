package com.github.joshuacgunn.core.container;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Inventory extends Container {
    private final Entity parentEntity;

    public Inventory(UUID containerUUID, Entity parentEntity) {
        super(containerUUID, "Inventory");
        this.parentEntity = parentEntity;
    }

    public Entity getParentEntity() {
        return parentEntity;
    }
}
