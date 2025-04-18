package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;

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
        parentLoop.setCurrentGameState(parentLoop.getPreviousGameState());
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
                player.takeDamage(enemy.getCurrentWeapon().getWeaponDamage());
                if (player.getEntityHp() < 0) {
                    player.setDeathStatus(false);
                    System.out.println("You died!");
                    inCombat = false;
                    break;
                } else if (!enemy.isAlive() && player.getEntityHp() > 0) {
                    enemy.setDeathStatus(false);
                    System.out.println("You killed the enemy!");
                    inCombat = false;
                    break;
                }
                break;
            case 2:
                System.out.println("You ran away!");
                inCombat = false;
                parentLoop.setRunning(false);
                break;
            case 3:
                System.out.println("Your inventory:");
                for (Item item : player.getInventory().getItems()) {
                    System.out.println(item.getItemName());
                }
                if (!player.getArmors().isEmpty()) {
                    System.out.println("Your equipped armor:");
                    for (Armor item : player.getArmors()) {
                        System.out.println("    Name: " + item.getItemName());
                        System.out.println("    Defense: " + item.getArmorDefense());
                        System.out.println("    Slot: " + item.getArmorSlot().name().toLowerCase());
                        System.out.println("    Material: " + item.getArmorMaterial().name().toLowerCase());
                        System.out.println("    Quality: " + item.getArmorQuality().name().toLowerCase());
                    }
                }
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
}
