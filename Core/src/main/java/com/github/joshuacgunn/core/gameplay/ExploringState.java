package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.sql.Time;
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
        if (parentLoop.getPreviousGameState() != this) {
            System.out.println("You are now exploring the world.");
        }
    }


    @Override
    public void handleGameState() {
        while (isExploring) {
            update();
        }
    }

    @Override
    public void update() {
        System.out.println("What would you like to do?");
        System.out.println("1. Go to a previous town");
        System.out.println("2. Go to a previous dungeon");
        System.out.println("3. Find a new place");
        handleInput();
        switch (currentAction) {
            case 1:
                System.out.println("Which town?");
                int i = 1;
                ArrayList<Town> towns = new ArrayList<>();
                for (Town town : Location.getLocationsByType(Town.class)) {
                    towns.add(town);
                }
                for (Town town : towns) {
                    System.out.println(i + ": " + town.getLocationName() + " (" + town.getShopsInTown().size() + " shops)");
                }
                int townIndex = scanner.nextInt();
                player.setCurrentLocation(towns.get(townIndex-1).getLocationUUID());
                TownState townState = new TownState(parentLoop);
                GameEvents.switchGameStates(player, townState);
                townState.handleGameState();
                isExploring = false;
                break;
            case 2:
                System.out.println("Which dungeon?");
                int j = 1;
                ArrayList<Dungeon> dungeons = new ArrayList<>();
                for (Dungeon dungeon : Location.getLocationsByType(Dungeon.class)) {
                    dungeons.add(dungeon);
                }
                for (Dungeon dungeon : dungeons) {
                    System.out.println(j + ": " + dungeon.getLocationName() + " (" + dungeon.getFloors().size() + " floors, " + dungeon.getDifficultyRating() + " difficulty rating)");
                }
                int dungeonIndex = scanner.nextInt();
                player.setCurrentLocation(dungeons.get(dungeonIndex-1).getLocationUUID());
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
                if (newLocation instanceof Town) {
                    player.setCurrentLocation(newLocation.getLocationUUID());
                    GameEvents.switchGameStates(player, new TownState(parentLoop));
                    isExploring = false;
                } else if (newLocation instanceof Dungeon) {
                    player.setCurrentLocation(newLocation.getLocationUUID());
                    GameEvents.switchGameStates(player, new DungeonState(parentLoop));
                    isExploring = false;
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
