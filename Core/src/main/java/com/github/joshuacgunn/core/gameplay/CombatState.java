package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class CombatState implements GameState {
    private Player player;
    private Enemy enemy;
    public Scanner scanner = new Scanner(System.in);
    private boolean inCombat = true;
    private int currentAction;
    private GameLoop parentLoop;

    public CombatState(Enemy enemy, GameLoop parentLoop) {
        this.player = parentLoop.getPlayer();
        this.enemy = enemy;
        this.parentLoop = parentLoop;
        System.out.println("You are approached by " + enemy.getEntityName() + ", wielding a[n] " + enemy.getCurrentWeapon().getWeaponMaterial().name().toLowerCase() + " sword");
    }

    public void handleGameState() {
        while (inCombat) {
            update();
        }
        if (!player.isAlive()) {
            parentLoop.setRunning(false);
        } else {
            GameEvents.switchGameStates(player, parentLoop.getPreviousGameState());
        }
    }


    @Override
    public void update() {
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
                    System.out.println("You dealt " + player.calculateWeaponDamage() + " damage, killing the enemy!");
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
                inCombat = false;
                GameEvents.switchGameStates(player, parentLoop.getPreviousGameState());
                break;
            case 3:
                GameEvents.showInventory(player);
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
            default:
                System.out.println("Invalid input");
                handleInput();
                break;
        }
    }

    @Override
    public String getGameStateName() {
        return "CombatState";
    }

    @Override
    public GameLoop getParentLoop() {
        return this.parentLoop;
    }
}
