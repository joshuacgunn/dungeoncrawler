package io.github.joshuacgunn.gameplay;

import io.github.joshuacgunn.entity.Player;
import io.github.joshuacgunn.location.Location;
import io.github.joshuacgunn.location.Shop;
import io.github.joshuacgunn.location.Town;
import io.github.joshuacgunn.location.World;
import io.github.joshuacgunn.misc.GameMethods;

import java.util.Scanner;
import java.util.UUID;

import static io.github.joshuacgunn.misc.GameMethods.printScreen;

public class TownState implements GameState {
    private final GameLoop parentLoop;
    private final Player player;
    private int currentAction;
    private boolean inTown = true;
    private boolean inGame = true;
    private boolean inShop = false;
    private final Town whichTown;
    Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new TownState instance, representing the state of the game
     * when the player is in a town.
     *
     * @param parentLoop The parent GameLoop object managing the current game state.
     * @param isNew      A flag indicating whether this is a new game (true)
     *                   or a loaded game (false).
     */
    public TownState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        if (player.getCurrentLocation() != null && player.getCurrentLocation() instanceof Town ) {
            this.whichTown = (Town) player.getCurrentLocation();
        } else {
            Town town = new Town(UUID.randomUUID(), false);
            whichTown = town;
            Location.locationMap.remove(town.getLocationUUID());
        }

        if (isNew) {
            if (player.getPreviousGameState() != null && player.getPreviousGameState().getGameStateName().equals("ShopState")) {
                printScreen(this);
                System.out.println("You have re-entered " + whichTown.getLocationName());
            } else {
                printScreen(this);
                String townSize = getTownSize();
                System.out.println("You have entered " + whichTown.getLocationName() +
                        ", a " + townSize + " town with a " + getShopsInTown(whichTown));
            }
        }
    }

    private String getTownSize() {
        if (whichTown.getShopsInTown().size() >= 3) {
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
            GameMethods.switchGameStates(player, exploringState);
        } else if (inShop) {
            ShopState shopState = new ShopState(parentLoop, true);
            GameMethods.switchGameStates(player, shopState);
        } else {
            player.setLastGameLocation(player.getCurrentLocation());
            player.setCurrentLocation(null);
            GameMethods.switchGameStates(player, new MainMenuState());
        }
    }

    @Override
    public void update() {
        if (!inTown) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Back to the main menu");
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
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                player.setCurrentLocation(whichTown.getShopsInTown().get(choice-1));
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
        try {
            currentAction = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            handleInput();
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

    static String getShopsInTown(Town town) {
        StringBuilder shopsInTown = new StringBuilder();
        for (Shop shop : town.getShopsInTown() ) {
            if (town.getShopsInTown().indexOf(shop) == town.getShopsInTown().size()-1) {
                shopsInTown.append(shop.getShopType().name.toLowerCase()).append(".");
            } else if (town.getShopsInTown().indexOf(shop) == town.getShopsInTown().size()-2) {
                shopsInTown.append(shop.getShopType().name.toLowerCase()).append(" and ");
            }
            else {
                shopsInTown.append(shop.getShopType().name.toLowerCase()).append(", ");
            }
        }
        return shopsInTown.toString();
    }
}
