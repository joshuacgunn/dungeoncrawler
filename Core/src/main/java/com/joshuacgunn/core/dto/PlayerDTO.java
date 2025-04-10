package com.joshuacgunn.core.dto;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public PlayerDTO() {
        super();
        this.setEntityType("Player");
    }
}