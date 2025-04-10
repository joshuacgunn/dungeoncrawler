package com.joshuacgunn.core.container;

import com.joshuacgunn.core.entity.Entity;

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
}
