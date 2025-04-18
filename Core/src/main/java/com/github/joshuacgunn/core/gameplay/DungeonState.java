package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;

import java.util.Random;
import java.util.Scanner;

public class DungeonState implements GameState {
    private GameLoop parentLoop;
    private Player player;
    public Scanner scanner = new Scanner(System.in);
    private boolean inDungeon = true;
    private int currentAction;
    private Dungeon whichDungeon;
    private Random rand = new Random();

    public DungeonState(GameLoop parentLoop) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichDungeon = (Dungeon) player.getEntityLocation();
        if (parentLoop.getPreviousGameState() != this) {
            System.out.println("You have entered " + whichDungeon.getLocationName() + ", a dungeon with " + whichDungeon.getFloors().size() + " floors, and a difficulty of " + whichDungeon.getDifficultyRating());
        }
    }



    @Override
    public void handleGameState() {
        while (inDungeon) {
            update();
        }
        System.out.println("You have left the dungeon");
        parentLoop.setRunning(false);
    }

    @Override
    public void update() {
        System.out.println("You are in the dungeon, what is your next move?");
        System.out.println("1. Attack an enemy");
        System.out.println("2. Try to sneak past to the next floor");
        System.out.println("3. Check your inventory");
        System.out.println("4. Leave the dungeon");
        handleInput();
        switch (currentAction) {
            case 1:
                Enemy enemy = whichDungeon.getCurrentFloor().getEnemiesOnFloor().get(rand.nextInt(0, whichDungeon.getCurrentFloor().getEnemiesOnFloor().size() - 1));
                CombatState combatState = new CombatState(enemy, parentLoop);
                parentLoop.setPreviousGameState(this);
                parentLoop.setCurrentGameState(combatState);
                combatState.handleGameState();
                break;
            case 4:
                inDungeon = false;
                break;
        }
    }

    @Override
    public void handleInput() {
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
            case "4":
                currentAction = 4;
                break;
        }
    }
}
