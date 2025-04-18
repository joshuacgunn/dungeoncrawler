package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.Scanner;

public class TownState implements GameState {
    private final GameLoop parentLoop;
    private final Player player;
    private int currentAction;
    private boolean inTown = true;
    private final Town whichTown;
    Scanner scanner = new Scanner(System.in);

    public TownState(GameLoop parentLoop) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichTown = (Town) player.getEntityLocation();
        String townSize;
        if (whichTown.getShopsInTown().size() == 3) {
            townSize = "large";
        } else if (whichTown.getShopsInTown().size() == 2) {
            townSize = "medium";
        } else {
            townSize = "small";
        }
        if (parentLoop.getPreviousGameState() != this) {
            System.out.println("You have entered " + whichTown.getLocationName() + ", a " + townSize + " town with a " + getShopsInTown());
        }
    }

    @Override
    public void handleGameState() {
        while (inTown) {
            update();
        }
        System.out.println("You have left the town");
        GameEvents.switchGameStates(player, new ExploringState(parentLoop));
    }

    @Override
    public void update() {
        System.out.println("What would you like to do?");
        System.out.println("1. Visit a shop");
        System.out.println("2. Leave the town");
        handleInput();
        switch (currentAction) {
            case 1:
                System.out.println("Which shop would you like to go to? ");
                for (int i = 0; i < whichTown.getShopsInTown().size(); i++) {
                    Shop shop = whichTown.getShopsInTown().get(i);
                    System.out.println((i + 1) + ": " + shop.getLocationName());
                }
                handleInput();
                player.setCurrentLocation(whichTown.getShopsInTown().get(currentAction-1).getLocationUUID());
                System.out.println("You have entered " + whichTown.getShopsInTown().get(currentAction-1).getLocationName());
                ShopState shopState = new ShopState(parentLoop);
                GameEvents.switchGameStates(player, shopState);
                shopState.handleGameState();
                break;
            case 2:
                inTown = false;
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
        return "TownState";
    }

    @Override
    public GameLoop getParentLoop() {
        return parentLoop;
    }

    private String getShopsInTown() {
        StringBuilder shopsInTown = new StringBuilder();
        for (Shop shop : whichTown.getShopsInTown() ) {
            shopsInTown.append(shop.getShopType().name.toLowerCase()).append(", ");
        }
        return shopsInTown.toString();
    }
}
