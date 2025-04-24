package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.github.joshuacgunn.core.mechanics.GameEvents.*;
import static com.github.joshuacgunn.core.save.SaveManager.BACKUP_DIRECTORY;
import static com.github.joshuacgunn.core.save.SaveManager.SAVE_DIRECTORY;

public class MainMenuState implements GameState {
    Scanner scanner = new Scanner(System.in);
    private boolean inMainMenu = true;
    private int currentAction;

    /**
     * Constructs a new MainMenuState and initializes the main menu for the game.
     *
     * This method clears the console, displays the main menu banner, and
     * calls the handleGameState method to process the main menu loop.
     * It is responsible for setting up the starting point of the game's main menu.
     */
    public MainMenuState() {
        printScreen(this);
        handleGameState();
    }

    @Override
    public void handleGameState() {
        while (inMainMenu) {
            update();
        }
        System.exit(0);
    }

    @Override
    public void update() {
        if (!inMainMenu) return;
        System.out.println("What would you like to do?");
        System.out.println("0: Leave the game");
        System.out.println("1. Start a new game");
        System.out.println("2. Load a game");
        handleInput();
        switch (currentAction) {
            case 0:
                inMainMenu = false;
                break;
            case 1:
                if (new File("saves/player_save.json").exists()) {
                    System.out.println("You have already saved a game!");
                    System.out.println("Would you like to overwrite it? (y/n)");
                    System.out.print("Choice: ");
                    String input = scanner.nextLine();
                    switch (input) {
                        case "y":
                            GameEvents.printLoadingDots("Starting new game", new Random().nextInt(1, 4));
                            try {
                                deleteDirectory(new File(SAVE_DIRECTORY));
                                deleteDirectory(new File(BACKUP_DIRECTORY));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            GameEvents.initializeGame();
                            break;
                        case "n":
                            update();
                            break;
                    }
                } else {
                    System.out.print("Starting new game");
                    int totalTime = new Random().nextInt(1, 4);
                    for (int seconds = 0; seconds < totalTime; seconds++) {
                        for (int dots = 0; dots < 4; dots++) {
                            if (dots == 0) {
                                System.out.print("");
                            } else {
                                System.out.print(".");
                            }
                            try {
                                TimeUnit.MILLISECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.print("\b\b\b   \b\b\b");
                    }
                    GameEvents.initializeGame();
                    break;
                }
                break;
            case 2:
                if (new File("saves/player_save.json").exists() || new File("backups/saves/").exists()) {
                    GameEvents.initializeGame();
                    break;
                } else {
                    System.out.println("You don't have a save!");
                    break;
                }
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
        }
    }

    @Override
    public String getGameStateName() {
        return "MainMenuState";
    }

    @Override
    public GameLoop getParentLoop() {
        return null;
    }

    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file); // Recursive call
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete(); // Delete the (now empty) directory
        }
    }
}
