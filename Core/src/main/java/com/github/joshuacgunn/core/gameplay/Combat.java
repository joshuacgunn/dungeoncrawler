package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;

import java.util.Scanner;

public class Combat implements GameState{

    private boolean isInCombat = true;

    private final Enemy currentEnemy;

    private final Player player;

    private int currentAction;

    public Combat(Player player, Enemy enemy) {
        this.currentEnemy = enemy;
        this.player = player;
        while (isInCombat) {
            handleGameState();
        }
    }

    @Override
    public void handleGameState() {
        while (isInCombat) {
            handleInput();
        }
        if (player.isAlive()) {
            changeState(GameStateType.EXPLORING);
            System.out.println("You have survived!");
        } else {
            System.out.println("You died!");
        }
    }

    @Override
    public void update() {
        switch (currentAction) {
            case 1:
                currentEnemy.takeDamage(player.calculateWeaponDamage());
                player.takeDamage(currentEnemy.getCurrentWeapon().getWeaponDamage());
                if (currentEnemy.getEntityHp() < player.calculateWeaponDamage()) {
                    System.out.println(currentEnemy.getEntityName() + " took " + player.calculateWeaponDamage() + " damage and died!");
                } else {
                    System.out.println(currentEnemy.getEntityName() + " took " + player.calculateWeaponDamage() + " damage.");
                }
                if (player.getEntityHp() < currentEnemy.getCurrentWeapon().getWeaponDamage()) {
                    System.out.println(player.getEntityName() + " took " + currentEnemy.getCurrentWeapon().getWeaponDamage() + " damage and died!");
                } else {
                    System.out.println(player.getEntityName() + " took " + currentEnemy.getCurrentWeapon().getWeaponDamage() + " damage.");
                }
            case 2:
                for (Item item: player.getInventory().getItems()) {
                    System.out.println(item.getItemName());
                    if (item instanceof Weapon weapon) {
                        System.out.println("    Damage: " + weapon.getWeaponDamage());
                        System.out.println("    Durability: " + weapon.getWeaponDurability());
                    } else if (item instanceof Armor) {
                        System.out.println("    Defense: " + ((Armor) item).getArmorDefense());
                        System.out.println("    Quality: " + ((Armor) item).getArmorQuality());
                        System.out.println("    Slot: " + ((Armor) item).getArmorSlot());
                    }
                }
                if (!player.getInventory().getItems().isEmpty()) {
                    System.out.println("Armor:");
                    for (Armor armor : player.getArmors()) {
                        System.out.println(armor.getItemName());
                        System.out.println("    Defense: " + armor.getArmorDefense());
                        System.out.println("    Quality: " + armor.getArmorQuality().name().toLowerCase());
                        System.out.println("    Slot: " + armor.getArmorSlot().name().toLowerCase());
                    }
                }
            case 3:


            case 4:
                isInCombat = false;
        }
    }

    @Override
    public void handleInput() {
        System.out.println("What would you like to do?");
        System.out.println("1. Attack enemy");
        System.out.println("2. Open inventory");
        System.out.println("3. Drink Potion (not available as of this release)");
        System.out.println("4. Run");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                this.currentAction = 1;
                break;
            case "2":
                this.currentAction = 2;
                break;
            case "3":
                this.currentAction = 3;
                break;
            case "4":
                this.currentAction = 4;
                break;
        }
        update();
    }

    @Override
    public void changeState(GameStateType newState) {

    }

}
