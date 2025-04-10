package com.joshuacgunn.core.location;

import com.joshuacgunn.core.container.Chest;
import com.joshuacgunn.core.entity.Enemy;
import com.joshuacgunn.core.entity.Goblin;
import com.joshuacgunn.core.item.Weapon;

import java.util.*;

/**
 * Represents a floor within a dungeon in the game world.
 * Each floor is identified by a unique UUID and has a specific floor number
 * within its parent dungeon.
 */
public class DungeonFloor {
    /** Unique identifier for this dungeon floor */
    private final UUID floorUUID;

    /** The number of this floor within its parent dungeon */
    private final int floorNumber;

    private float difficultyRating;

    /** Reference to the parent dungeon that contains this floor */
    private final Dungeon parentDungeon;

    private ArrayList<Enemy> enemiesOnFloor = new ArrayList<>();

    private boolean hasChest;

    private Chest chest;

    Random rand = new Random();
    /**
     * Creates a new dungeon floor with a specified UUID.
     *
     * @param floorUUID The unique identifier for this floor
     * @param parentDungeon The dungeon that contains this floor
     * @param floorNumber The number of this floor within the dungeon
     */
    public DungeonFloor(UUID floorUUID, Dungeon parentDungeon, int floorNumber, boolean skipEnemyGeneration) {
        this.floorUUID = floorUUID;
        this.floorNumber = floorNumber;
        this.parentDungeon = parentDungeon;
        this.hasChest = true;
        this.chest = new Chest(Chest.ChestRarity.COMMON, false);
//        this.chest.addItem(new Weapon("TestWeapon", UUID.randomUUID(), Weapon.WeaponQuality.EXQUISITE));
        if (!skipEnemyGeneration) {
            generateEnemies();
        }
        this.difficultyRating = calculateDifficulty();
    }

    /**
     * Gets the unique identifier of this floor.
     *
     * @return The UUID of this floor
     */
    public UUID getFloorUUID() {
        return floorUUID;
    }

    private void generateEnemies() {
        float enemyChance = rand.nextFloat();
        for (int i = 0; i < 3; i++) {
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
        }
        if (enemyChance < 0.2f) {
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
        } else if (enemyChance < .5f) {
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
        } else if (enemyChance < .8f) {
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
        } else if (enemyChance < .99f) {
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
            enemiesOnFloor.add(new Goblin(UUID.randomUUID()));
        }
    }

    public void setEnemiesOnFloor(ArrayList<Enemy> enemiesOnFloor) {
        this.enemiesOnFloor = enemiesOnFloor;
    }

    public ArrayList<Enemy> getEnemiesOnFloor() {
        return this.enemiesOnFloor;
    }

    /**
     * Gets the floor number of this dungeon floor.
     *
     * @return The floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Gets the parent dungeon that contains this floor.
     *
     * @return The parent dungeon
     */
    public Dungeon getParentDungeon() {
        return parentDungeon;
    }

    public void setDifficultyRating(float rating) {
        this.difficultyRating = rating;
    }

    public float calculateDifficulty() {
        // Handle empty enemy list case
        if (enemiesOnFloor.isEmpty()) {
            return 0.0f;
        }

        // Base difficulty starts with the floor number
        float difficulty = floorNumber * 0.2f;

        // Total stats across all enemies
        float totalHp = 0;
        float totalDamage = 0;
        float totalDefense = 0;

        // Count different enemy types for variety bonus
        Set<String> enemyTypes = new HashSet<>();

        for (Enemy enemy : enemiesOnFloor) {
            totalHp += enemy.getEntityHp();
//            totalDamage += enemy.getCurrentWeapon().getWeaponDamage();
            totalDefense += enemy.getEntityDefense();
            enemyTypes.add(enemy.getClass().getSimpleName());
        }

        // Calculate average stats but don't divide by enemy count
        float hpFactor = totalHp / 100f;
        float damageFactor = totalDamage / 25f;
        float defenseFactor = totalDefense / 25f;

        // Number of enemies is a multiplier rather than divisor
        float enemyCountFactor = 0.75f + (enemiesOnFloor.size() * 0.25f);

        // Variety bonus (more types = more difficult)
        float varietyBonus = (enemyTypes.size() - 1) * 0.15f;

        // Combine factors
        difficulty += (hpFactor + damageFactor + defenseFactor) * enemyCountFactor + varietyBonus;

        // Round to one decimal place
        return Math.round(difficulty * 10) / 100.0f;
    }

    public float getDifficultyRating() {
        return this.difficultyRating;
    }

    public boolean isHasChest() {
        return hasChest;
    }

    public void setHasChest(boolean hasChest) {
        this.hasChest = hasChest;
    }

    public Chest getChest() {
        return chest;
    }
}