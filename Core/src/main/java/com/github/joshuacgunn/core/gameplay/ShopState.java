package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.Scanner;

public class ShopState implements GameState {
    private final GameLoop parentLoop;
    private final Shop whichShop;
    private int currentAction;
    private boolean inShop = true;
    private boolean inGame = true;
    Scanner scanner = new Scanner(System.in);
    private final Player player;

    /**
     * Creates a new ShopState instance, representing the state where the player
     * interacts with a shop in the game.
     *
     * @param parentLoop The GameLoop instance managing the current game state.
     * @param isNew A boolean flag indicating whether this is a new instance of the ShopState.
     */
    public ShopState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();

        this.whichShop = (Shop) parentLoop.getPlayer().getCurrentLocation();

        if (isNew) {
            System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameEvents.npcDialogue(whichShop.getShopOwner(), 1) );
        }
    }

    @Override
    public void handleGameState() {
        while (inShop) {
            update();
        }
        if (inGame) {
            System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameEvents.npcDialogue(whichShop.getShopOwner(), 2));
            System.out.println("You have left the shop");
            for (Town town : Location.getLocationsByType(Town.class)) {
                for (Shop shop : town.getShopsInTown()) {
                    if (shop.equals(player.getCurrentLocation())) {
                        player.setCurrentLocation(town);
                    }
                }
            }
            player.setPreviousGameState(this);
            TownState townState = new TownState(parentLoop, true);
            GameEvents.switchGameStates(player, townState);
        } else {
            GameEvents.leaveGame(player, parentLoop);
        }
    }

    @Override
    public void update() {
        if (!inShop) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Leave the game");
        System.out.println("1. Buy an item");
        System.out.println("2. Leave the shop");
        handleInput();
        switch (currentAction) {
            case 0:
                inShop = false;
                inGame = false;
                break;
            case 1:
                System.out.println("Which item would you like to buy?");
            case 2:
                inShop = false;
                break;
        }
    }

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
            default:
                System.out.println("Invalid input");
                handleInput();
                break;
        }
    }

    @Override
    public String getGameStateName() {
        return "ShopState";
    }

    @Override
    public GameLoop getParentLoop() {
        return parentLoop;
    }
}
