package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;
import com.github.joshuacgunn.core.save.SaveManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ExploringState implements GameState {
    private final GameLoop parentLoop;
    Scanner scanner = new Scanner(System.in);
    private int currentAction;
    private boolean isExploring = true;
    private final Player player;

    public ExploringState(GameLoop parentLoop) {
        this.parentLoop = parentLoop;
        this.player = parentLoop.getPlayer();
        if (parentLoop.getPreviousGameState() != null && parentLoop.getPreviousGameState().getGameStateName().equals("ExploringState")) {
            System.out.println("You are now exploring the world.");
        } else {
            GameEvents.loadGameGreet(player);
        }
    }


    @Override
    public void handleGameState() {
        while (isExploring) {
            update();
        }
        GameEvents.leaveGame(player, parentLoop);
    }

    @Override
    public void update() {
        if (!isExploring) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Exit game");
        System.out.println("1. Go to a previous town");
        System.out.println("2. Go to a previous dungeon");
        System.out.println("3. Find a new place");
        handleInput();
        switch (currentAction) {
            case 0:
                isExploring = false;
                break;
            case 1:
                System.out.println("Which town?");
                System.out.println("0: Go back");
                int i = 1;
                ArrayList<Town> towns = new ArrayList<>();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    towns.add(town);
                }

                for (Town town : towns) {
                    System.out.println(i + ": " + town.getLocationName() + " (" + town.getShopsInTown().size() + " shops)");
                    i += 1;
                }

                if (towns.isEmpty()) {
                    System.out.println("There are no towns in the world.");
                    break;
                }

                int townIndex = scanner.nextInt();
                scanner.nextLine();

                if (townIndex == 0) {
                    break;
                }
                player.setCurrentLocation(towns.get(townIndex-1));
                TownState townState = new TownState(parentLoop);
                GameEvents.switchGameStates(player, townState);
                townState.handleGameState();
                isExploring = false;
                break;
            case 2:
                System.out.println("Which dungeon?");
                System.out.println("0: Go back");

                int j = 1;
                ArrayList<Dungeon> dungeons = new ArrayList<>();
                for (Dungeon dungeon : Location.getLocationsByType(Dungeon.class)) {
                    dungeons.add(dungeon);
                }

                for (Dungeon dungeon : dungeons) {
                    System.out.println(j + ": " + dungeon.getLocationName() + " (" + dungeon.getFloors().size() + " floors, " + dungeon.getDifficultyRating() + " difficulty rating)");
                }

                if (dungeons.isEmpty()) {
                    System.out.println("There are no dungeons in the world.");
                    update();
                    break;
                }

                int dungeonIndex = scanner.nextInt();
                scanner.nextLine();
                if (dungeonIndex == 0) {
                    update();
                    break;
                }
                player.setCurrentLocation(dungeons.get(dungeonIndex-1));
                DungeonState dungeonState = new DungeonState(parentLoop);
                GameEvents.switchGameStates(player, dungeonState);
                dungeonState.handleGameState();
                isExploring = false;
                break;
            case 3:
                System.out.println("Searching for a new place to go...");
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(4, 8));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Location newLocation = Location.generateLocation();
                System.out.println("You found a new place to go to: " + newLocation.getLocationName());
                System.out.println("Would you like to go there? (y/n)");
                String input = scanner.nextLine();
                if (newLocation instanceof Town && input.equalsIgnoreCase("y") ) {
                    player.setCurrentLocation(newLocation);
                    GameEvents.switchGameStates(player, new TownState(parentLoop));
                    isExploring = false;
                } else if (newLocation instanceof Dungeon && input.equalsIgnoreCase("y") ) {
                    player.setCurrentLocation(newLocation);
                    GameEvents.switchGameStates(player, new DungeonState(parentLoop));
                    isExploring = false;
                } else {
                    System.out.println("You decided not to go there.");
                    update();
                    break;
                }
                break;
        }
    }

    @Override
    public void handleInput() {
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
