package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

import static com.github.joshuacgunn.core.mechanics.GameEvents.playerDeath;
import static com.github.joshuacgunn.core.mechanics.GameEvents.printScreen;

/**
 * Represents the combat state of the game where battles between the player and enemies take place.
 * This state handles all combat-related interactions, including turn management and combat actions.
 * Implements the GameState interface to integrate with the game's state management system.
 */
public class CombatState implements GameState {
    
    /** The player character involved in combat */
    private final Player player;
    
    /** The enemy being fought in this combat instance */
    private final Enemy enemy;
    
    /** Scanner for handling user input during combat */
    Scanner scanner = new Scanner(System.in);
    
    /** Flag indicating if combat is still ongoing */
    private boolean inCombat = true;
    
    /** Tracks the current action being performed */
    private int currentAction;
    
    /** Reference to the parent game loop managing this state */
    private final GameLoop parentLoop;

    /**
     * Creates a new combat state instance.
     *
     * @param parentLoop The game loop managing this combat state
     */
    public CombatState(GameLoop parentLoop, boolean isNew) {
        this.player = parentLoop.getPlayer();
        this.parentLoop = parentLoop;
        if (player.getCurrentLocation() instanceof Dungeon) {
            this.enemy = ((Dungeon) player.getCurrentLocation()).getCurrentFloor().getEnemiesOnFloor().get(new java.util.Random().nextInt(((Dungeon) player.getCurrentLocation()).getCurrentFloor().getEnemiesOnFloor().size()));
        } else {
            this.enemy = new Enemy(Enemy.EnemyType.GOBLIN, UUID.randomUUID(), false);
            Entity.entityMap.remove(enemy.getEntityUUID());
        }
        printScreen(this);
        System.out.println("You are approached by a[n] " + enemy.getEntityName() + ", wielding a[n] " + enemy.getCurrentWeapon().getWeaponMaterial().name().toLowerCase() + " sword");
    }

    /**
     * Manages the main combat loop and state transitions.
     * This method is called when the game enters the combat state and handles
     * the overall flow of combat including turns and victory/defeat conditions.
     */
    public void handleGameState() {
        while (inCombat) {
            update();
        }
        if (!player.isAlive()) {
            playerDeath(player);
        } else {
            player.setPreviousGameState(this);
            DungeonState dungeonState = new DungeonState(parentLoop, false);
            dungeonState.ranAway = true;
            GameEvents.switchGameStates(player, dungeonState);
        }
    }

    /**
     * Updates the combat state each game tick.
     * Handles combat calculations, status effects, and state changes.
     */
    @Override
    public void update() {
        if (!inCombat) return;
        System.out.println("What would you like to do?");
        System.out.println("1. Attack the enemy");
        System.out.println("2. Run away");
        System.out.println("3. Check your inventory");
        handleInput();
        switch (currentAction) {
            case 1:
                enemy.takeDamage(player.calculateWeaponDamage());
                if (enemy.isAlive()) {
                    System.out.println("You dealt " + player.calculateWeaponDamage() + " damage to " + enemy.getEntityName() + "!" );
                }
                player.takeDamage(enemy.getCurrentWeapon().getWeaponDamage());
                if (player.isAlive()) {
                    System.out.println(enemy.getEntityName() + " dealt " + enemy.getCurrentWeapon().getWeaponDamage() + " damage to you!");
                }
                if (!player.isAlive()) {
                    player.setDeathStatus(false);
                    System.out.println("You took " + enemy.getCurrentWeapon().getWeaponDamage() + " damage, killing you!");
                    inCombat = false;
                } else if (!enemy.isAlive()) {
                    enemy.setDeathStatus(false);
                    System.out.println("You dealt " + player.calculateWeaponDamage() + " damage, killing the " + enemy.getEntityName() + "!" );
                    inCombat = false;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("You ran away!");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inCombat = false;
                break;
            case 3:
                GameEvents.showInventory(player);
                break;
        }
    }

    /**
     * Processes player input during combat.
     * Handles combat actions such as attacking, using items, or attempting to flee.
     */
    @Override
    public void handleInput() {
        System.out.print("Choice: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                currentAction = 1;
                break;
            case "2":
                currentAction = 2;
                break;
            case "3":
                currentAction = 3;
                break;
            default:
                System.out.println("Invalid input");
                handleInput();
                break;
        }
    }

    /**
     * Gets the name of this game state.
     *
     * @return The string "CombatState"
     */
    @Override
    public String getGameStateName() {
        return "CombatState";
    }

    /**
     * Gets the parent game loop that manages this combat state.
     *
     * @return The GameLoop instance managing this combat state
     */
    @Override
    public GameLoop getParentLoop() {
        return this.parentLoop;
    }
}