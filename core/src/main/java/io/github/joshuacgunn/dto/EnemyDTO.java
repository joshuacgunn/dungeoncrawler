package io.github.joshuacgunn.dto;

/**
 * EnemyDTO extends EntityDTO to represent enemy-specific properties.
 */
public class EnemyDTO extends EntityDTO {
    private boolean isQuestEnemy;

    public EnemyDTO() {
        super();
        this.setEntityType("Enemy");
    }

    public boolean getIsQuestEnemy() {
        return isQuestEnemy;
    }

    public void setQuestEnemy(boolean isQuestEnemy) {
        this.isQuestEnemy = isQuestEnemy;
    }
}