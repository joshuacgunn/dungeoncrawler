package com.github.joshuacgunn.core.gameplay;

import com.github.joshuacgunn.core.mechanics.GameEvents;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.github.joshuacgunn.core.mechanics.GameEvents.clearConsole;

public class MainMenuState implements GameState {
    Scanner scanner = new Scanner(System.in);
    private boolean inMainMenu = true;
    private int currentAction;
    private boolean inGame = true;
    private boolean isInMainMenu = true;

    /**
     * Constructs a new MainMenuState and initializes the main menu for the game.
     *
     * This method clears the console, displays the main menu banner, and
     * calls the handleGameState method to process the main menu loop.
     * It is responsible for setting up the starting point of the game's main menu.
     */
    public MainMenuState() {
        clearConsole();
        System.out.println(
                """
                         _____                                                                                   _____\s
                        ( ___ )                                                                                 ( ___ )
                         |   |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|   |\s
                         |   |  ____                                        ____                    _            |   |\s
                         |   | |  _ \\ _   _ _ __   __ _  ___  ___  _ __    / ___|_ __ __ ___      _| | ___ _ __  |   |\s
                         |   | | | | | | | | '_ \\ / _` |/ _ \\/ _ \\| '_ \\  | |   | '__/ _` \\ \\ /\\ / / |/ _ \\ '__| |   |\s
                         |   | | |_| | |_| | | | | (_| |  __/ (_) | | | | | |___| | | (_| |\\ V  V /| |  __/ |    |   |\s
                         |   | |____/ \\__,_|_| |_|\\__, |\\___|\\___/|_| |_|  \\____|_|  \\__,_| \\_/\\_/ |_|\\___|_|    |   |\s
                         |   |                    |___/                                                          |   |\s
                         |___|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|___|\s
                        (_____)                                                                                 (_____)
                        """);
        handleGameState();
    }

    @Override
    public void handleGameState() {
        while (inMainMenu) {
            update();
        }
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
                inGame = false;
                break;
            case 1:
                if (new File("saves/player_save.json").exists()) {
                    System.out.println("You have already saved a game!");
                    System.out.println("Would you like to overwrite it? (y/n)");
                    String input = scanner.nextLine();
                    switch (input) {
                        case "y":
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("Starting new game.");
                            try {
                                Files.delete(Path.of("saves/"));
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
                    GameEvents.initializeGame();
                    break;
                }
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
}
