package io.game.core.dto;

/**
 * EnemyDTO extends EntityDTO to represent enemy-specific properties.
 */
public class EnemyDTO extends EntityDTO {

    public EnemyDTO() {
        super();
        this.setEntityType("Enemy");
    }
}