package io.github.joshuacgunn.gameplay;

import io.github.joshuacgunn.entity.NPC;
import io.github.joshuacgunn.entity.Player;
import io.github.joshuacgunn.location.Location;
import io.github.joshuacgunn.location.Shop;
import io.github.joshuacgunn.location.Town;
import io.github.joshuacgunn.misc.GameMethods;

import java.util.Scanner;
import java.util.UUID;


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
        if (player.getCurrentLocation() != null && player.getCurrentLocation() instanceof Shop) {
            this.whichShop = (Shop) parentLoop.getPlayer().getCurrentLocation();
        } else {
            Town town = new Town(UUID.randomUUID(), false);
            Shop shop = new Shop(Shop.ShopType.BLACKSMITH, UUID.randomUUID(), new NPC("TestNPC", UUID.randomUUID()), false, town);
            this.whichShop = shop;
            Location.locationMap.remove(shop.getLocationUUID());
            Location.locationMap.remove(town.getLocationUUID());
        }

        if (isNew) {
            GameMethods.printScreen(this);
            System.out.println("You have entered " + whichShop.getLocationName());
            System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameMethods.npcDialogue(whichShop.getShopOwner(), 1));
        }
    }

    @Override
    public void handleGameState() {
        while (inShop) {
            update();
        }
        if (inGame) {
            System.out.println(whichShop.getShopOwner().getEntityName() + ": " + GameMethods.npcDialogue(whichShop.getShopOwner(), 2));
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
            GameMethods.switchGameStates(player, townState);
        } else {
            player.setLastGameLocation(player.getCurrentLocation());
            player.setCurrentLocation(null);
            GameMethods.switchGameStates(player, new MainMenuState());
        }
    }

    @Override
    public void update() {
        if (!inShop) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Back to the main menu");
        System.out.println("1. Buy an item");
        System.out.println("2. Talk to an NPC");
        System.out.println("3. Leave the shop");
        handleInput();
        switch (currentAction) {
            case 0:
                inShop = false;
                inGame = false;
                break;
            case 1:
                System.out.println("Which item would you like to buy?");
                GameMethods.showInventory(whichShop.getShopOwner(), false);
                break;
            case 2:
                System.out.println("Which NPC would you like to talk to?: ");
                int k = 1;
                for (NPC npc : whichShop.getNpcsInShop()) {
                    if (npc.equals(whichShop.getShopOwner())) {
                    } else {
                        System.out.println(k + ". " + npc.getEntityName());
                        k++;
                    }
                }
                int action = scanner.nextInt();
                scanner.nextLine();
                System.out.println(whichShop.getNpcsInShop().get(action-1).getEntityName() + ": " + GameMethods.npcDialogue(whichShop.getNpcsInShop().get(action-1), 2));
                break;
            case 3:
                inShop = false;
                break;
        }
    }

    @Override
    public void handleInput() {
        System.out.print("Choice: ");
        String input = scanner.nextLine();
        try {
            currentAction = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            handleInput();
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
