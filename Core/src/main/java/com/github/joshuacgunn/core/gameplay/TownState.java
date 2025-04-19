package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.location.World;
import com.github.joshuacgunn.core.mechanics.GameEvents;
import com.github.joshuacgunn.core.save.SaveManager;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.Scanner;
import java.util.UUID;

public class TownState implements GameState {
    private final GameLoop parentLoop;
    private final Player player;
    private int currentAction;
    private boolean inTown = true;
    private boolean inGame = true;
    private boolean inShop = false;
    private final Town whichTown;
    Scanner scanner = new Scanner(System.in);

    public TownState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichTown = (Town) player.getCurrentLocation();

        // isNew is true for new games, false for loaded games
        if (isNew) {
            String townSize = getTownSize();
            System.out.println("You have entered " + whichTown.getLocationName() +
                    ", a " + townSize + " town with a " + getShopsInTown());
        }
    }

    private String getTownSize() {
        if (whichTown.getShopsInTown().size() == 3) {
            return "large";
        } else if (whichTown.getShopsInTown().size() == 2) {
            return "medium";
        } else {
            return "small";
        }
    }

    @Override
    public void handleGameState() {
        while (inTown) {
            update();
        }
        if (inGame && !inShop) {
            System.out.println("You have left the town");
            player.setCurrentLocation(new World(UUID.randomUUID()));
            player.setPreviousGameState(this);
            ExploringState exploringState = new ExploringState(parentLoop, true);
            GameEvents.switchGameStates(player, exploringState);
            exploringState.handleGameState();
        } else if (inShop) {
            ShopState shopState = new ShopState(parentLoop);
            GameEvents.switchGameStates(player, shopState);
            shopState.handleGameState();
        } else {
            GameEvents.leaveGame(player, parentLoop);
        }
    }

    @Override
    public void update() {
        if (!inTown) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Leave the game");
        System.out.println("1. Visit a shop");
        System.out.println("2. Leave the town");
        handleInput();
        switch (currentAction) {
            case 0:
                inTown = false;
                inGame = false;
                break;
            case 1:
                System.out.println("Which shop would you like to go to? ");
                for (int i = 0; i < whichTown.getShopsInTown().size(); i++) {
                    Shop shop = whichTown.getShopsInTown().get(i);
                    System.out.println((i + 1) + ": " + shop.getLocationName());
                }
                handleInput();
                player.setCurrentLocation(whichTown.getShopsInTown().get(currentAction-1));
                System.out.println("You have entered " + whichTown.getShopsInTown().get(currentAction-1).getLocationName());
                inTown = false;
                inShop = true;
                break;
            case 2:
                inTown = false;
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
