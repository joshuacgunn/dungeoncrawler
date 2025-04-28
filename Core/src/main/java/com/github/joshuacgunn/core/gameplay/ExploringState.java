package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.github.joshuacgunn.core.gameplay.TownState.getShopsInTown;
import static com.github.joshuacgunn.core.mechanics.GameEvents.*;

public class ExploringState implements GameState {
    private final GameLoop parentLoop;
    Scanner scanner = new Scanner(System.in);
    private int currentAction;
    private boolean isExploring = true;
    private boolean inTown = false;
    private boolean inDungeon = false;
    private final Player player;

    /**
     * Constructs a new ExploringState instance.
     *
     * @param parentLoop The parent GameLoop managing this game state.
     * @param isNew Indicates whether this ExploringState is part of a new game.
     */
    public ExploringState(GameLoop parentLoop, boolean isNew) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();

        if (isNew) {
            printScreen(this);
            if (player.getPreviousGameState() != null) {
                System.out.println("You begin exploring the world...");
            }
        }
    }


    @Override
    public void handleGameState() {
        while (isExploring) {
            update();
        }
        if (inTown) {
            player.setPreviousGameState(this);
            TownState townState = new TownState(parentLoop, true);
            GameEvents.switchGameStates(player, townState);
        } else if (inDungeon) {
            player.setPreviousGameState(this);
            DungeonState dungeonState = new DungeonState(parentLoop, true);
            GameEvents.switchGameStates(player, dungeonState);
        } else {
            MainMenuState mainMenuState = new MainMenuState();
            GameEvents.switchGameStates(player, mainMenuState);
        }
    }

    @Override
    public void update() {
        if (!isExploring) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Back to the main menu");
        System.out.println("1. Go to a previous town");
        System.out.println("2. Go to a previous dungeon");
        System.out.println("3. Find a new place");
        handleInput();
        switch (currentAction) {
            case 0:
                isExploring = false;
                break;
            case 1:
                if (Location.getLocationsByType(Town.class).isEmpty()) {
                    System.out.println("There are no towns in the world.");
                    update();
                    break;
                }
                System.out.println("Which town?");
                System.out.println("0: Go back");

                int i = 1;
                ArrayList<Town> towns = new ArrayList<>(Location.getLocationsByType(Town.class));

                for (Town town : towns) {
                    System.out.println(i + ": " + town.getLocationName() + " (" + getShopsInTown(town) + ")");
                    i += 1;
                }
                int townIndex = scanner.nextInt();
                scanner.nextLine();

                if (townIndex == 0) {
                    break;
                }

                isExploring = false;
                inTown = true;
                player.setCurrentLocation(towns.get(townIndex-1));
                break;
            case 2:

                if (Location.getLocationsByType(Dungeon.class).isEmpty()) {
                    System.out.println("There are no dungeons in the world.");
                    break;
                }

                System.out.println("Which dungeon?");
                System.out.println("0: Go back");

                int j = 1;
                ArrayList<Dungeon> dungeons = new ArrayList<>(Location.getLocationsByType(Dungeon.class));

                for (Dungeon dungeon : dungeons) {
                    System.out.println(j + ": " + dungeon.getLocationName() + " (" + dungeon.getFloors().size() + " floors, " + dungeon.getDifficultyRating() + " difficulty)");
                }

                int dungeonIndex = scanner.nextInt();
                scanner.nextLine();

                if (dungeonIndex == 0) {
                    break;
                }

                isExploring = false;
                inDungeon = true;
                player.setCurrentLocation(dungeons.get(dungeonIndex-1));
                break;
            case 3:
                printLoadingDots("Searching for a new place to go", new Random().nextInt(3, 5));
                Location newLocation = Location.generateLocation();
                System.out.println("You found a new " + newLocation.getClass().getSimpleName().toLowerCase() + " to go to: " + newLocation.getLocationName());
                System.out.println("Would you like to go there? (y/n)");
                String input = scanner.nextLine();
                if (newLocation instanceof Town && input.equalsIgnoreCase("y") ) {
                    isExploring = false;
                    inTown = true;
                    player.setCurrentLocation(newLocation);
                } else if (newLocation instanceof Dungeon && input.equalsIgnoreCase("y") ) {
                    isExploring = false;
                    inDungeon = true;
                    player.setCurrentLocation(newLocation);
                } else {
                    System.out.println("You decided not to go there.");
                    Location.locationMap.remove(newLocation.getLocationUUID());
                    update();
                    break;
                }
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
        return "ExploringState";
    }

    @Override
    public GameLoop getParentLoop() {
        return this.parentLoop;
    }
}
