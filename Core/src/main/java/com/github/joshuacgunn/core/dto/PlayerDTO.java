package com.github.joshuacgunn.core.dto;

import java.io.Serializable;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public PlayerDTO() {
        super();
        this.setEntityType("Player");
    }
}