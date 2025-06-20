package io.game.core.gameplay;

import io.game.core.entity.Player;
import io.game.core.location.Dungeon;
import io.game.core.location.World;
import io.game.core.misc.GameMethods;

import java.util.Scanner;
import java.util.UUID;

import static io.game.core.misc.GameMethods.printScreen;

/**
 * Represents the dungeon exploration state of the game where players navigate through
 * procedurally generated floors, encounter enemies, and discover treasures.
 * Implements the GameState interface to integrate with the game's state management system.
 */

public class DungeonState implements GameState {
    private final GameLoop parentLoop;
    private final Player player;
    public Scanner scanner = new Scanner(System.in);
    private boolean inDungeon = true;
    private boolean inGame = true;
    private boolean inCombat = false;
    private int currentAction;
    private final Dungeon whichDungeon;
    public boolean ranAway = false;

    /**
     * Creates a new dungeon state instance.
     *
     * @param parentLoop The game loop managing this dungeon state
     * @param isNew Whether this is a new dungeon instance (true) or a loaded one (false)
     */

    public DungeonState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichDungeon = (Dungeon) player.getCurrentLocation();
        if (isNew) {
            printScreen(this);
            System.out.println("You have entered " + whichDungeon.getLocationName() + ", a dungeon with " + whichDungeon.getFloors().size() + " floors, and a difficulty of " + whichDungeon.getDifficultyRating());
        } else if (player.getPreviousGameState() != null && player.getPreviousGameState().getGameStateName().equals("CombatState")) {
            printScreen(this);
            System.out.println("You have re-entered " + whichDungeon.getLocationName());
            if (ranAway) {
                System.out.println("You scurried away from the enemy...");
                ranAway = false;
            }
        }
    }

    /**
     * Handles the main dungeon exploration loop and state transitions.
     * This method manages:
     * - Dungeon navigation
     * - Enemy encounters
     * - Treasure discovery
     * - Floor progression
     * - Exit conditions
     */

    @Override
    public void handleGameState() {
        while (inDungeon) {
            update();
        }
        if (inCombat && inGame) {
            CombatState combatState = new CombatState(parentLoop, true);
            GameMethods.switchGameStates(player, combatState);
        } else if (inGame) {
            System.out.println("You have left the dungeon");
            player.setPreviousGameState(this);
            ExploringState exploringState = new ExploringState(parentLoop, true);
            GameMethods.switchGameStates(player, exploringState);
        } else {
            player.setLastGameLocation(player.getCurrentLocation());
            player.setCurrentLocation(null);
            GameMethods.switchGameStates(player, new MainMenuState());
        }
    }

    /**
     * Updates the dungeon state each game tick.
     * Handles:
     * - Enemy movement and spawning
     * - Environmental effects
     * - Player status updates
     * - Floor events
     */
    @Override
    public void update() {
        if (!inDungeon) return;
        if (whichDungeon.getCurrentFloor().getEnemiesOnFloor().isEmpty()) {
            whichDungeon.clearFloor();
            System.out.println("You cleared the floor! Moving on to floor " + whichDungeon.getCurrentFloor().getFloorNumber());
        }
        System.out.println("What would you like to do?");
        System.out.println("0. Back to the main menu");
        System.out.println("1. Attack an enemy");
        System.out.println("2. Try to sneak past to the next floor");
        System.out.println("3. Check your inventory");
        System.out.println("4. Leave the dungeon");
        handleInput();
        switch (currentAction) {
            case 0:
                inDungeon = false;
                inGame = false;
                break;
            case 1:
                inDungeon = false;
                inCombat = true;
                break;
            case 3:
                GameMethods.showInventory(player);
                break;
            case 4:
                inDungeon = false;
                player.setCurrentLocation(new World(UUID.randomUUID()));
                break;
        }
    }

    /**
     * Processes player input during dungeon exploration.
     * Handles actions such as:
     * - Movement between rooms
     * - Interaction with objects
     * - Combat initiation
     * - Inventory management
     * - Floor navigation
     */

    @Override
    public void handleInput() {
        System.out.print("Choice: ");
        String input = scanner.nextLine();
        switch (input) {
            case "0":
                currentAction = 0;
                break;
            case "1":
                currentAction = 1;
                break;
            case "2":
                currentAction = 2;
                break;
            case "3":
                currentAction = 3;
                break;
            case "4":
                currentAction = 4;
                break;
            default:
                System.out.println("Invalid input");
                handleInput();
                break;
        }
    }

    @Override
    public String getGameStateName() {
        return "DungeonState";
    }

    @Override
    public GameLoop getParentLoop() {
        return this.parentLoop;
    }
}
