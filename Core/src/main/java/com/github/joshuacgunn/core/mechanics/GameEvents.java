package com.github.joshuacgunn.core.mechanics;

import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.gameplay.ExploringState;
import com.github.joshuacgunn.core.gameplay.GameState;
import com.github.joshuacgunn.core.gameplay.TownState;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.location.World;
import com.github.joshuacgunn.core.save.SaveManager;
import com.github.joshuacgunn.core.gameplay.GameLoop;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public abstract class GameEvents {


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

    public static void leaveGame(Player player, GameLoop parentLoop) {
        System.out.println("You have left the game");
        SaveManager.saveState(player);
        parentLoop.stopGame();
    }

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
        }
        else {
            System.out.println("You are currently exploring the world. Have fun!");
        }
    }

    public static void initializeGame() {
        clearConsole();
        Player player;
        if (new File("backups/saves/").exists()) {
            SaveManager.loadState();
        }

        boolean isNewGame = !(new File("saves/player_save.json").exists());

        if (isNewGame) {
            player = Player.createPlayer();
            Town startingTown = new Town(UUID.randomUUID(), true);
            player.setCurrentLocation(startingTown);
        } else {
            player = SaveManager.loadState();
        }

        GameLoop gameLoop = new GameLoop(player, isNewGame);
        player.setGameState(new TownState(gameLoop, false));
        gameLoop.startGameLoop();
    }

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
}