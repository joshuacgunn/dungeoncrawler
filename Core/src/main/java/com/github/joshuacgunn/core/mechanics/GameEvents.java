package com.github.joshuacgunn.core.mechanics;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.gameplay.GameState;
import com.github.joshuacgunn.core.gameplay.TownState;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.location.Shop;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.save.SaveManager;
import com.github.joshuacgunn.core.gameplay.GameLoop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A utility class that manages and handles various game-related events and actions
 * in the Terminal RPG game. This class serves as a central hub for common game operations
 * and state transitions.
 */
public abstract class GameEvents {

    /**
     * Displays the inventory contents of a given entity.
     * Shows items, equipment, and relevant statistics.
     *
     * @param entity The entity whose inventory should be displayed
     */
    public static void showInventory(Entity entity) {
        System.out.println("Your inventory:");
        for (Item item : entity.getInventory().getItems()) {
            if (!(item.getItemUUID().equals(entity.getCurrentWeapon().getItemUUID()))) {
                System.out.println(item.getItemName());
            }
        }
        if (entity.getCurrentWeapon() != null) {
            System.out.println("Your equipped weapon:");
            System.out.println("    Name: " + entity.getCurrentWeapon().getItemName());
            System.out.println("    Damage: " + entity.getCurrentWeapon().getWeaponDamage());
            System.out.println("    Armor Penetration: " + entity.getCurrentWeapon().getArmorPenetration());
            System.out.println("    Quality: " + entity.getCurrentWeapon().getWeaponQuality().name().toLowerCase());
            System.out.println("    Material: " + entity.getCurrentWeapon().getWeaponMaterial().name().toLowerCase());

        }
        if (!entity.getArmors().isEmpty()) {
            System.out.println("Your equipped armor:");
            for (Armor item : entity.getArmors()) {
                System.out.println("    Name: " + item.getItemName());
                System.out.println("    Defense: " + item.getArmorDefense());
                System.out.println("    Slot: " + item.getArmorSlot().name().toLowerCase());
                System.out.println("    Material: " + item.getArmorMaterial().name().toLowerCase());
                System.out.println("    Quality: " + item.getArmorQuality().name().toLowerCase());
            }
        }
    }

    /**
     * Handles the transition between different game states.
     * Manages state cleanup and initialization during transitions.
     *
     * @param player The player whose state is being changed
     * @param newGameState The game state to transition to
     */
    public static void switchGameStates(Player player, GameState newGameState) {
        if (player == null || newGameState == null) {
            throw new IllegalArgumentException("Player and new game state cannot be null");
        }

        GameState currentState = player.getGameState();
        if (currentState == null) {
            player.setGameState(newGameState);
            return;
        }

        // Use the existing parent loop instead of creating a new one
        GameLoop parentLoop = currentState.getParentLoop();
        if (parentLoop != null) {
            parentLoop.setPreviousGameState(currentState);
            parentLoop.setCurrentGameState(newGameState);
        }

        player.setPreviousGameState(currentState);
        player.setGameState(newGameState);

        // Move save to after state transition is complete
        SaveManager.saveState(player);

        newGameState.handleGameState();
    }

    /**
     * Retrieves dialogue for NPCs based on the conversation context.
     * Provides appropriate responses for different dialogue states.
     *
     * @param npc The NPC who is speaking
     * @param dialogue The dialogue ID or context number
     * @return The selected dialogue string for the given context
     */
    public static String npcDialogue(NPC npc, int dialogue) {
        switch (npc.getNpcPersonality()) {
            case ANGRY:
                switch (dialogue) {
                    case 1:
                        return "If you aren't buying anything, THEN GET OUT!";
                    case 2:
                        return "If you don't get out of my face, we're going to have a problem.";
                    case 3:
                        return "Let the door hit you on the way out.";
                }
            case DEPRESSED:
                switch (dialogue) {
                    case 1:
                        return "Buy whatever you want. I don't care anymore.";
                    case 2:
                        return "Can you leave me alone?";
                    case 3:
                        return "Come back I guess, or don't";
                }
            case FUNNY:
                switch (dialogue) {
                    case 1:
                        return "Welcome to my shop! Feel free to browse, but remember: shoplifting is just borrowing stuff that gets you stabbed!";
                    case 2:
                        return "Finally, I was starting to think my invisibility potion was working too well!";
                    case 3:
                        return "Come back soon! Or don't, I'm not your mom... but I do sell wares your mom would approve of!";
                }
            case STUPID:
                switch (dialogue) {
                    case 1:
                        return "Welcome to... uh... my shop? Is this my shop? Yeah, my shop!";
                    case 2:
                        return "Hi! Did you know swords are pointy? I learned that yesterday!";
                    case 3:
                        return "Bye-bye! Don't forget to... um... what was I saying?";
                }
            case SARCASTIC:
                switch (dialogue) {
                    case 1:
                        return "Oh look, another 'hero' graces my humble establishment. I'm absolutely thrilled.";
                    case 2:
                        return "Let me guess, you're here to save the world? How original.";
                    case 3:
                        return "Please, take your time leaving. It's not like I have a business to run or anything.";
                }
            case SERIOUS:
                switch (dialogue) {
                    case 1:
                        return "Welcome, traveler. All items are priced as marked. No haggling.";
                    case 2:
                        return "State your business. I have inventory to manage.";
                    case 3:
                        return "Safe travels. Return when you require further supplies.";
                }
            default:
                return "I'm sorry, but I don't know what to say.";
        }
    }

    /**
     * Handles the game exit process.
     * Performs necessary cleanup and save operations before exit.
     *
     * @param player The player who is leaving the game
     * @param parentLoop The main game loop instance
     */
    public static void leaveGame(Player player, GameLoop parentLoop) {
        System.out.println("You have left the game");
        SaveManager.saveState(player);
        parentLoop.stopGame();
    }

    /**
     * Displays welcome message when loading a saved game.
     * Shows relevant player statistics and game state information.
     *
     * @param player The player whose game is being loaded
     */
    public static void loadGameGreet(Player player) {
        System.out.println("Welcome back, " + player.getEntityName() + "!");
        if (player.getCurrentLocation() != null) {
            if (player.getCurrentLocation() instanceof Town town) {
                System.out.println("You are currently in the town of " + town.getLocationName() + ".");
            } else if (player.getCurrentLocation() instanceof com.github.joshuacgunn.core.location.Dungeon dungeon) {
                System.out.println("You are currently in the dungeon " + dungeon.getLocationName() + ", on floor " + dungeon.getCurrentFloor().getFloorNumber() + ".");
            } else if (player.getCurrentLocation() instanceof com.github.joshuacgunn.core.location.Shop shop) {
                System.out.println("You are currently in the shop " + shop.getLocationName() + ".");
            }
        } else {
            System.out.println("You are currently exploring the world. Have fun!");
        }
    }

    /**
     * Initializes the game environment.
     * Sets up necessary game systems and initial state.
     */
    public static void initializeGame() {
        clearConsole();
        Player player;
        boolean isNewGame;
        if (new File("backups/saves/").exists() || new File("saves/").exists()) {
            isNewGame = false;
        } else {
            isNewGame = true;
        }

        if (isNewGame) {
            player = Player.createPlayer();
            Town startingTown = new Town(UUID.randomUUID(), true);
            player.setCurrentLocation(startingTown);
        } else {
            player = SaveManager.loadState();
        }

        GameLoop gameLoop = new GameLoop(player, isNewGame);
        if (isNewGame) {
            player.setGameState(new TownState(gameLoop, false));
        }
        gameLoop.startGameLoop();
    }

    /**
     * Clears the console screen.
     * Ensures clean display of new game states or information.
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }

    public static void printLogo() {
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
    }

    public static List<NPC> generateUniqueNPCs(Shop shop) {
        ArrayList<NPC> npcList = new ArrayList<>();
        String name = shop.getParentTown().getLocationName();
        if (name.equalsIgnoreCase("Blaviken") && shop.getShopType() == Shop.ShopType.TAVERN) {
            NPC geralt = new NPC("Geralt of Rivia", UUID.randomUUID());
            geralt.setNpcPersonality(NPC.Personality.SERIOUS);
            npcList.add(geralt);
            NPC ciri = new NPC("Ciri", UUID.randomUUID());
            ciri.setNpcPersonality(NPC.Personality.SERIOUS);
            npcList.add(ciri);
            NPC vesemir = new NPC("Vesemir", UUID.randomUUID());
            vesemir.setNpcPersonality(NPC.Personality.SERIOUS);
            npcList.add(vesemir);
        } else if (name.equalsIgnoreCase("Pallet Town") && shop.getShopType() == Shop.ShopType.TAVERN) {
            NPC ash = new NPC("Ash Ketchum", UUID.randomUUID());
            ash.setNpcPersonality(NPC.Personality.FUNNY);
            npcList.add(ash);
            NPC paul = new NPC("Paul", UUID.randomUUID());
            paul.setNpcPersonality(NPC.Personality.SERIOUS);
            npcList.add(paul);
            NPC gary = new NPC("Gary Oak", UUID.randomUUID());
            gary.setNpcPersonality(NPC.Personality.SARCASTIC);
            npcList.add(gary);
        } else if (name.equalsIgnoreCase("Silver Skalitz") && shop.getShopType() == Shop.ShopType.BLACKSMITH) {
            NPC henry = new NPC("Henry", UUID.randomUUID());
            henry.setNpcPersonality(NPC.Personality.FUNNY);
            npcList.add(henry);
            NPC theresa = new NPC("Theresa", UUID.randomUUID());
            theresa.setNpcPersonality(NPC.Personality.SARCASTIC);
            npcList.add(theresa);
        }
        return npcList;
    }
}