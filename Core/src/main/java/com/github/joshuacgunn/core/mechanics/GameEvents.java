package com.github.joshuacgunn.core.mechanics;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.gameplay.*;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.*;
import com.github.joshuacgunn.core.save.SaveManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        if (entity instanceof Player) {
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
        } else if (entity instanceof NPC) {
            int i = 0;
            System.out.println("Items for sale:");
            for (Item item : entity.getInventory().getItems()) {
                i += 1;
                System.out.println(i + ". " + item.getItemName());
                System.out.println("    Rarity: " + item.getItemRarity().name().toLowerCase());
                if (item instanceof Armor armor) {
                    System.out.println("    Defense: " + armor.getArmorDefense());
                    System.out.println("    Slot: " + armor.getArmorSlot().name().toLowerCase());
                    System.out.println("    Material: " + armor.getArmorMaterial().name().toLowerCase());
                    System.out.println("    Quality: " + armor.getArmorQuality().name().toLowerCase());
                } else if (item instanceof Weapon weapon) {
                    System.out.println("    Damage: " + weapon.getWeaponDamage());
                    System.out.println("    Armor Penetration: " + weapon.getArmorPenetration());
                    System.out.println("    Quality: " + weapon.getWeaponQuality());
                    System.out.println("    Material: " + weapon.getWeaponMaterial());
                }
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
    }

    /**
     * Retrieves dialogue for NPCs based on the conversation context.
     * Provides appropriate responses for different dialogue states.
     *
     * @param npc The NPC who is speaking
     * @param dialogue The dialogue choice. 1 is for shop greeting, 2 is for regular npc greeting, 3 is for leaving the shop.
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
            case CHARISMATIC:
                switch (dialogue) {
                    case 1:
                        return "Ah! A customer with discerning taste! Come in, come in! The finest goods await your perusal.";
                    case 2:
                        return "What a pleasure to meet you! Your presence brightens this dreary day.";
                    case 3:
                        return "Do return soon, won't you? I'll keep an eye out for special items just for you!";
                }
            case COWARDLY:
                switch (dialogue) {
                    case 1:
                        return "Oh! You startled me... um, f-feel free to look around. Just don't touch anything sharp, please.";
                    case 2:
                        return "Oh! H-hello there... you're not going to cause trouble, are you?";
                    case 3:
                        return "Goodbye! Watch out for bandits on the road... and wolves... and bears... and everything else!";
                }
            case OPTIMISTIC:
                switch (dialogue) {
                    case 1:
                        return "Welcome to my wonderful shop! Today is a GREAT day to find exactly what you're looking for!";
                    case 2:
                        return "Hello there, friend! Isn't it a beautiful day to be alive? I just know something amazing is going to happen!";
                    case 3:
                        return "Safe travels! The road ahead is full of opportunities and adventures waiting for you!";
                }
            case PESSIMISTIC:
                switch (dialogue) {
                    case 1:
                        return "Welcome, I suppose. Don't expect to find anything good today. Stock's been terrible lately.";
                    case 2:
                        return "Oh, hello. Not that it matters. We're all just passing time until the inevitable end anyway.";
                    case 3:
                        return "Leaving already? Can't blame you. Nothing good lasts anyway.";
                }
            case GREEDY:
                switch (dialogue) {
                    case 1:
                        return "Welcome, welcome! I see you have a full coin purse there. How fortunate... for both of us!";
                    case 2:
                        return "Hello there! Say, that's a nice looking weapon you have. I'll give you a fair price for it... very fair... for me.";
                    case 3:
                        return "Come back when you have more gold to spend! My prices might go up, though. Supply and demand, you know!";
                }
            case LOYAL:
                switch (dialogue) {
                    case 1:
                        return "Welcome to my shop, friend. You'll find no fairer prices or better quality in all the realm, I promise you that.";
                    case 2:
                        return "Well met! If you ever need an ally in these parts, remember my face. I stand by my friends.";
                    case 3:
                        return "Farewell for now. Know that you'll always have a place here whenever you return.";
                }
            case MYSTERIOUS:
                switch (dialogue) {
                    case 1:
                        return "Enter... if you seek what lies beyond the veil of ordinary merchandise. I have... unusual wares.";
                    case 2:
                        return "Our paths cross again... perhaps by chance, perhaps by design. The stars have much to reveal.";
                    case 3:
                        return "Our business concludes... for now. But the threads of fate often intertwine in unexpected ways...";
                }
            case ADVENTUROUS:
                switch (dialogue) {
                    case 1:
                        return "Welcome, fellow traveler! These items? All collected during my expeditions to the farthest reaches of the realm!";
                    case 2:
                        return "Hail! Have you ventured to the Crystal Caves yet? No? Oh, you simply MUST go! I could tell you the way...";
                    case 3:
                        return "Safe journeys! May the road rise to meet you and the wind be always at your back! What an adventure awaits!";
                }
            case WISE:
                switch (dialogue) {
                    case 1:
                        return "Welcome to my humble shop. One often finds what they need, rather than what they seek.";
                    case 2:
                        return "Greetings. Remember that even the smallest pebble can change the course of a mighty river.";
                    case 3:
                        return "Farewell. May your steps be guided by wisdom, and your heart by compassion.";
                }
            case LAZY:
                switch (dialogue) {
                    case 1:
                        return "*Yawn* Oh, a customer... Browse if you want. Just don't ask me to reach anything from the top shelf.";
                    case 2:
                        return "Hey... *stretches* Don't suppose you'd grab that thing over there for me? No? Fine, I'll get up... eventually.";
                    case 3:
                        return "Yeah, bye... *yawn* Close the door on your way out... or don't. Whatever.";
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

        TickManager.getInstance().stop();

        parentLoop.stopGame();
    }

    /**
     * Displays welcome message when loading a saved game.
     * Shows relevant player statistics and game state information.
     *
     * @param player The player whose game is being loaded
     */
    public static void loadGameGreet(Player player) {
        printScreen(player.getGameState());
        System.out.println("Welcome back, " + player.getEntityName() + "!");
        if (player.getCurrentLocation() != null) {
            if (player.getCurrentLocation() instanceof Town town) {
                System.out.println("You are currently in the town of " + town.getLocationName() + ".");
            } else if (player.getCurrentLocation() instanceof Dungeon dungeon) {
                System.out.println("You are currently in " + dungeon.getLocationName() + ", on floor " + dungeon.getCurrentFloor().getFloorNumber() + ".");
            } else if (player.getCurrentLocation() instanceof Shop shop) {
                System.out.println("You are currently at " + shop.getLocationName() + ", in the town of " + shop.getParentTown().getLocationName() + ".");
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
            player = createPlayer();
            Town startingTown = new Town(UUID.randomUUID(), true);
            player.setCurrentLocation(startingTown);
        } else {
            player = SaveManager.loadState();
        }

        TickManager.getInstance().start();

        GameLoop gameLoop = new GameLoop(player, isNewGame);
        if (isNewGame) {
            player.setGameState(new TownState(gameLoop, false));
        }
        SaveManager.saveState(player);
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

    public static void printScreen(GameState gameState) {
        clearConsole();
        if (!Entity.getEntitiesByType(Player.class).isEmpty() && !(gameState instanceof MainMenuState)) {
            Location location = Entity.getEntitiesByType(Player.class).getFirst().getCurrentLocation();
            System.out.println(AsciiArt.getArtForLocation(location, gameState));
        } else {
            System.out.println(AsciiArt.LOGO_MAIN_MENU);
        }
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

    /**
     * Creates a new player character with user input for name and class selection.
     * This method handles the initial player creation process including:
     * - Getting the player's name
     * - Class selection from available options
     * - Generating a unique identifier
     * - Setting up initial player stats based on chosen class
     *
     * @return A newly created Player instance with the chosen attributes
     */
    public static Player createPlayer() {
        System.out.println(AsciiArt.LOGO_CREATE_CHARACTER);
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.println("Welcome " + name + "! What class would you like to play as? (1-3) ");
        System.out.println("1. Rogue");
        System.out.println("2. Wizard");
        System.out.println("3. Paladin");
        System.out.print("Class: ");
        int playerClass = scanner.nextInt();
        scanner.nextLine();
        UUID uuid = UUID.randomUUID();
        Player.PlayerClass playerClassEnum = Player.PlayerClass.values()[playerClass - 1];
        Player player = new Player(name, uuid, playerClassEnum, true);
        System.out.println("You chose " + playerClassEnum.name().toLowerCase() + "!");
        System.out.println(player.getPlayerStatsString());
        return player;
    }

    public static void printLoadingScreen(GameState gameState) {
        clearConsole();

        // Display information about the location
//        System.out.println("\nEntering " + location.getLocationName() + "...");

        if (gameState instanceof TownState townState) {
        } else if (gameState instanceof DungeonState dungeonState) {
        } else if (gameState instanceof ShopState shopState) {
        }

        // Display "Press any key to continue" prompt
        System.out.println("\nPress any key to to continue...");

        try {
            System.in.read();
            // Clear the input buffer
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (IOException e) {
            // Handle exception silently
        }
    }
}