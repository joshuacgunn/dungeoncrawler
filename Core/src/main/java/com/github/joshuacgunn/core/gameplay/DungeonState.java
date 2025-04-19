package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.World;
import com.github.joshuacgunn.core.mechanics.GameEvents;
import com.github.joshuacgunn.core.save.SaveManager;

import java.util.Scanner;
import java.util.UUID;

public class DungeonState implements GameState {
    private final GameLoop parentLoop;
    private final Player player;
    public Scanner scanner = new Scanner(System.in);
    private boolean inDungeon = true;
    private boolean inGame = true;
    private int currentAction;
    private final Dungeon whichDungeon;

    public DungeonState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichDungeon = (Dungeon) player.getCurrentLocation();
        if (isNew) {
            System.out.println("You have entered " + whichDungeon.getLocationName() + ", a dungeon with " + whichDungeon.getFloors().size() + " floors, and a difficulty of " + whichDungeon.getDifficultyRating());
        }
    }

    @Override
    public void handleGameState() {
        while (inDungeon) {
            update();
        }
        if (inGame) {
            System.out.println("You have left the dungeon");
            GameEvents.switchGameStates(player, new ExploringState(parentLoop, true));
            player.setCurrentLocation(new World(UUID.randomUUID()));
        } else {
            GameEvents.leaveGame(player, parentLoop);
        }
    }

    @Override
    public void update() {
        if (!inDungeon) return;
        System.out.println("What would you like to do?");
        System.out.println("0. Leave the game");
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
                Enemy enemy = whichDungeon.getCurrentFloor().getEnemiesOnFloor().get(0);
                CombatState combatState = new CombatState(enemy, parentLoop);
                GameEvents.switchGameStates(player, combatState);
                combatState.handleGameState();
                break;
            case 3:
                GameEvents.showInventory(player);
                break;
            case 4:
                inDungeon = false;
                break;
        }
    }

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
