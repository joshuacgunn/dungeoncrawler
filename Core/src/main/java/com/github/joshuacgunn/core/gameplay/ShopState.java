package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.Scanner;

public class ShopState implements GameState {
    private GameLoop parentLoop;
    private final Shop whichShop;
    private int currentAction;
    private boolean inShop = true;
    Scanner scanner = new Scanner(System.in);
    private final Player player;

    public ShopState(GameLoop parentLoop) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        this.whichShop = (Shop) parentLoop.getPlayer().getCurrentLocation();
        if (parentLoop.getPreviousGameState() != this) {
            System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameEvents.npcDialogue(whichShop.getShopOwner(), 1));
        }
    }


    @Override
    public void handleGameState() {
        while (inShop) {
            update();
        }
        System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameEvents.npcDialogue(whichShop.getShopOwner(), 2));
        System.out.println("You have left the shop");
        for (Town town : Location.getLocationsByType(Town.class) ) {
            for (Shop shop : town.getShopsInTown()) {
                if (shop.equals(player.getCurrentLocation())) {
                    player.setCurrentLocation(town);
                }
            }
        }
        TownState townState = new TownState(parentLoop);
        GameEvents.switchGameStates(player, townState);
        townState.handleGameState();
    }

    @Override
    public void update() {
        if (!inShop) return;
        System.out.println("What would you like to do?");
        System.out.println("1. Buy an item");
        System.out.println("2. Leave the shop");
        handleInput();
        switch (currentAction) {
            case 1:
                System.out.println("Which item would you like to buy?");
            case 2:
                inShop = false;
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
