package com.github.joshuacgunn.core.location;

import com.github.joshuacgunn.core.container.Chest;
import com.github.joshuacgunn.core.entity.Enemy;

import java.util.*;

/**
 * Represents a floor within a dungeon in the game world.
 * Each floor is identified by a unique UUID and has a specific floor number
 * within its parent dungeon. Floors contain enemies, may have a chest with
 * items, and have a difficulty rating based on various factors.
 */
public class DungeonFloor extends Location{
    /** The number of this floor within its parent dungeon */
    private final int floorNumber;

    /**
     * The calculated difficulty rating of this floor based on enemies
     * and floor number
     */
    private float difficultyRating;

    /** Reference to the parent dungeon that contains this floor */
    private final Dungeon parentDungeon;

    /** List of enemies present on this floor */
    private ArrayList<? extends Enemy> enemiesOnFloor = new ArrayList<>();

    /** Indicates whether this floor contains a chest */
    private boolean hasChest;

    /** The chest on this floor, if any */
    private Chest chest;

    /** Random number generator for enemy and loot generation */
    Random rand = new Random();

    /**
     * Creates a new dungeon floor with a specified UUID.
     *
     * @param floorUUID The unique identifier for this floor
     * @param parentDungeon The dungeon that contains this floor
     * @param floorNumber The number of this floor within the dungeon
     * @param skipEnemyGeneration If true, enemies will not be generated automatically
     */
    public DungeonFloor(UUID floorUUID, Dungeon parentDungeon, int floorNumber, boolean skipEnemyGeneration) {
        super("Floor " + floorNumber + " in " + parentDungeon.getLocationName(), floorUUID );
        this.floorNumber = floorNumber;
        this.parentDungeon = parentDungeon;
        if (!skipEnemyGeneration) {
            generateEnemies();
            for (Enemy enemy : enemiesOnFloor) {
                enemy.setCurrentLocation(this.locationUUID);
            }
        }
        this.difficultyRating = calculateDifficulty();
        this.hasChest = true;
        this.chest = new Chest(Chest.ChestRarity.COMMON, UUID.randomUUID(), false,this);
    }

    private void generateEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Base scaling formula with logarithmic growth
        int baseEnemyCount = 1 + (int)(Math.log(floorNumber + 1) * 2);

        // More significant random variation (0-3 for early floors, more for deeper floors)
        int variationRange = 1 + (int)(floorNumber / 5);
        int variableCount = rand.nextInt(variationRange + 1);

        // Every 5th floor gets a difficulty spike (mini-boss floors)
        boolean isBossFloor = floorNumber % 5 == 0 && floorNumber > 0;
        int bossFloorBonus = isBossFloor ? 1 : 0;

        // Calculate max enemy count with sensible upper limit
        int calculatedCount = baseEnemyCount + variableCount + bossFloorBonus;
        int hardCap = 10; // Never exceed this many enemies

        int maxEnemyCount = Math.min(calculatedCount, hardCap);

        // Generate enemies based on the calculated count
        for (int i = 0; i < maxEnemyCount; i++) {
            // Your existing enemy type selection logic
            float generateChance = rand.nextFloat();
            float floorProgress = Math.min(floorNumber / 15.0f, 0.9f); // Floor difficulty factor

            if (floorNumber < 3 && generateChance < 0.4f - (floorProgress * 0.3f)) {
                enemies.add(Enemy.createEnemy(Enemy.EnemyType.GOBLIN, UUID.randomUUID(), true));
            } else if (generateChance < 0.7f - (floorProgress * 0.2f)) {
                enemies.add(Enemy.createEnemy(Enemy.EnemyType.ORC, UUID.randomUUID(), true));
            } else if (generateChance < 0.9f) {
                enemies.add(Enemy.createEnemy(Enemy.EnemyType.TROLL, UUID.randomUUID(), true));
            } else {
                // Small chance for rarer enemy type (once you add more types)
                enemies.add(Enemy.createEnemy(Enemy.EnemyType.TROLL, UUID.randomUUID(), true));
            }
        }

        this.enemiesOnFloor = enemies;

        // Update enemy locations
        for (Enemy enemy : enemiesOnFloor) {
            enemy.setCurrentLocation(this.locationUUID);
        }
    }

    /**
     * Sets the list of enemies on this floor.
     * Used primarily for loading saved game states.
     *
     * @param enemiesOnFloor The list of enemies to place on this floor
     */
    public void setEnemiesOnFloor(ArrayList<Enemy> enemiesOnFloor) {
        this.enemiesOnFloor = enemiesOnFloor;
    }

    /**
     * Gets the list of enemies currently on this floor.
     *
     * @return List of enemies on this floor
     */
    public ArrayList<? extends Enemy> getEnemiesOnFloor() {
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

    /**
     * Sets the difficulty rating of this floor manually.
     *
     * @param rating The new difficulty rating value
     */
    public void setDifficultyRating(float rating) {
        this.difficultyRating = rating;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    /**
     * Calculates the difficulty rating of this floor based on multiple factors:
     * - Floor number (deeper floors are more difficult)
     * - Enemy statistics (HP, damage, defense)
     * - Number of enemies
     * - Variety of enemy types
     *
     * @return The calculated difficulty rating as a float
     */
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
            totalDamage += enemy.getCurrentWeapon().getWeaponDamage();
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

    /**
     * Gets the current difficulty rating of this floor.
     *
     * @return The difficulty rating
     */
    public float getDifficultyRating() {
        return this.difficultyRating;
    }

    /**
     * Checks if this floor has a chest.
     *
     * @return true if the floor has a chest, false otherwise
     */
    public boolean isHasChest() {
        return hasChest;
    }

    /**
     * Sets whether this floor has a chest.
     *
     * @param hasChest true if the floor should have a chest, false otherwise
     */
    public void setHasChest(boolean hasChest) {
        this.hasChest = hasChest;
    }

    /**
     * Gets the chest on this floor, if any.
     *
     * @return The chest object on this floor, or null if no chest exists
     */
    public Chest getChest() {
        return chest;
    }
}