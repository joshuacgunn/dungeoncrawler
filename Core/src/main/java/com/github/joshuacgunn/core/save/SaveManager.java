package com.github.joshuacgunn.core.save;

import com.github.joshuacgunn.core.dto.*;
import com.github.joshuacgunn.core.entity.Enemy;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.NPC;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.mapper.DungeonMapper;
import com.github.joshuacgunn.core.mapper.EntityMapper;
import com.github.joshuacgunn.core.mapper.ItemMapper;
import com.github.joshuacgunn.core.mapper.TownMapper;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import static com.github.joshuacgunn.core.gson.GsonProvider.GSON;

/**
 * A utility class that manages game state persistence and loading operations.
 * Handles saving and loading of all game elements including player data, entities,
 * locations, and items. Also manages backup functionality for save files.
 */
public abstract class SaveManager {
    /** Directory path for main save files */
    public static final String SAVE_DIRECTORY = "saves/";

    /** Directory path for backup save files */
    public static final String BACKUP_DIRECTORY = "backups/";

    /** Date format for backup file naming */
    private static final SimpleDateFormat date = new SimpleDateFormat("dd_HH.mm.ss");

    /**
     * Saves the complete game state using the current player context.
     * Creates a comprehensive save including all related game elements.
     *
     * @param player The player whose game state is being saved
     */
    public static void saveState(Player player) {
        // The order of this is critical for functionality. It will not work if changed.
        saveItems();
        saveEntities();
        saveDungeons();
        saveTowns();
        savePlayer(player);
        backupSave();
    }

    /**
     * Loads the complete game state from saved files.
     * Reconstructs the entire game state including all entities and locations.
     *
     * @return The loaded Player object with restored game state
     */
    public static Player loadState() {
        if (!(new File(SAVE_DIRECTORY).exists()) && new File(BACKUP_DIRECTORY + "saves/").exists()) {
            loadBackup();
        }

        // The order of this is critical for functionality. It will not work if changed.
        loadItems();

        loadEntities();
        loadDungeons();
        loadTowns();

        manageBackupDirectory();
        return loadPlayer();
    }

    /**
     * Creates the necessary directory structure for save files.
     * Ensures both main save and backup directories exist.
     */
    public static void createDirectories() {
        try {
            File save_dir = new File(SAVE_DIRECTORY);
            File backup = new File(BACKUP_DIRECTORY);
            File backup_saves = new File(BACKUP_DIRECTORY + "saves");

            if (!save_dir.exists()) save_dir.mkdirs();
            if (!backup.exists()) backup.mkdirs();
            if (!backup_saves.exists()) backup_saves.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a backup of the current save state.
     * Uses timestamp-based naming for backup files.
     */
    public static void backupSave() {
        String location = ("backups/saves/save_" + date.format(new Date()));
        File backupDir = new File(location);

        File saveDir = new File("saves/");
        try {
            FileUtils.copyDirectory(saveDir, backupDir);
            manageBackupDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Manages the backup directory by enforcing size limits and cleanup.
     * Removes old backups when necessary.
     */
    private static void manageBackupDirectory() {
        File dir = new File(BACKUP_DIRECTORY + "saves" + "/");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 10) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                files[0].delete();
            }
        }
    }

    /**
     * Loads a backup save state.
     * Restores the game state from the most recent backup.
     */
    public static void loadBackup () {
        File backupDir = new File(BACKUP_DIRECTORY + "saves");
        if (!backupDir.exists() || !backupDir.isDirectory()) {
            throw new RuntimeException("Backup directory not found");
        }

        // Get all backup directories
        File[] backupDirs = backupDir.listFiles();
        if (backupDirs == null || backupDirs.length == 0) {
            throw new RuntimeException("No backups found");
        }

        // Sort by last modified time to get the most recent backup
        Arrays.sort(backupDirs, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        File mostRecentBackup = backupDirs[0];

        // Clear current save directory
        File currentSaveDir = new File(SAVE_DIRECTORY);
        if (currentSaveDir.exists()) {
            try {
                FileUtils.deleteDirectory(currentSaveDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to clear current save directory", e);
            }
        }

        // Copy the most recent backup to the save directory
        try {
            FileUtils.copyDirectory(mostRecentBackup, currentSaveDir);
            System.out.println("Loaded backup from: " + mostRecentBackup.getName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy backup to save directory", e);
        }
    }

    /**
     * Saves all game entities to persistent storage.
     * Includes NPCs, enemies, and other game characters.
     */
    public static void saveEntities() {
        createDirectories();
        List<NpcDTO> npcDTOs = new ArrayList<>();
        List<EnemyDTO> enemyDTOS = new ArrayList<>();

        // Get all entities except player (which is saved separately)
        for (Entity entity : Entity.getEntities()) {
            if (entity instanceof NPC) {
                npcDTOs.add((NpcDTO) EntityMapper.INSTANCE.entityToEntityDTO(entity));
            } else if (entity instanceof Enemy && entity.isAlive()) {
                enemyDTOS.add((EnemyDTO) EntityMapper.INSTANCE.entityToEntityDTO(entity));
            }
        }

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "NPCs_snapshot.json", false)) {
            writer.write(GSON.toJson(npcDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "enemies_snapshot.json", false)) {
            writer.write(GSON.toJson(enemyDTOS));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all game entities from persistent storage.
     * Reconstructs entity relationships and states.
     */
    public static void loadEntities() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "NPCs_snapshot.json")) {
            // Use TypeToken for proper generic type handling
            Type listType = new TypeToken<List<NpcDTO>>(){}.getType();
            List<NpcDTO> npcDTOs = GSON.fromJson(reader, listType);

            // Clear existing entities
            Entity.entityMap.values().removeIf(e -> e instanceof NPC);

            // Create entities from DTOs
            for (NpcDTO npcDTO : npcDTOs) {
                EntityMapper.INSTANCE.entityDtoToEntity(npcDTO);
            }
        } catch (IOException e) {
            // Handle empty file case
            if (e instanceof FileNotFoundException || e.getMessage().contains("empty")) {
                System.err.println("No entities file found or empty file. Starting with empty entities.");
                return;
            }
            throw new RuntimeException(e);
        }
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "enemies_snapshot.json")) {
            Type listType = new TypeToken<List<EnemyDTO>>(){}.getType();
            List<EnemyDTO> enemyDTOS = GSON.fromJson(reader, listType);

            Entity.entityMap.values().removeIf(e -> e instanceof Enemy);

            for (EnemyDTO enemyDTO : enemyDTOS) {
                if (enemyDTO.isAlive()) {
                    EntityMapper.INSTANCE.entityDtoToEntity(enemyDTO);
                }
            }
        } catch (IOException e) {
            // Handle empty file case
            if (e instanceof FileNotFoundException || e.getMessage().contains("empty")) {
                System.err.println("No entities file found or empty file. Starting with empty entities.");
                return;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves player-specific data to persistent storage.
     * Includes inventory, stats, and current state.
     *
     * @param player The player to save
     */
    public static void savePlayer(Player player) {
        createDirectories();
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "player_save.json")) {

            PlayerDTO playerDTO = (PlayerDTO) EntityMapper.INSTANCE.entityToEntityDTO(player);
            writer.write(GSON.toJson(playerDTO, PlayerDTO.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads player data from persistent storage.
     *
     * @return The reconstructed Player object
     */
    public static Player loadPlayer() {
            try (Reader reader = new FileReader(SAVE_DIRECTORY + "player_save.json")) {
                PlayerDTO dto = GSON.fromJson(reader, PlayerDTO.class);
                return (Player) EntityMapper.INSTANCE.entityDtoToEntity(dto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    /**
     * Saves all dungeon locations and their states.
     * Includes dungeon layout, contents, and progress.
     */
    public static void saveDungeons() {
        createDirectories();
        List<DungeonDTO> dungeonDTOs = Location.locationMap.values().stream()
                .filter(location -> location instanceof Dungeon)
                .map(location -> (Dungeon) location)
                .map(DungeonMapper.INSTANCE::dungeonToDungeonDto)
                .toList();


        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "dungeons_snapshot.json")) {
            writer.write(GSON.toJson(dungeonDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all dungeon locations and their states.
     * Reconstructs dungeon environments and contents.
     */
    public static void loadDungeons() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "dungeons_snapshot.json")) {
            DungeonDTO[] dungeonDTOs = GSON.fromJson(reader, DungeonDTO[].class);

            Location.locationMap.values().removeIf(location -> location instanceof Dungeon);

            for (DungeonDTO dto : dungeonDTOs) {
                DungeonMapper.INSTANCE.dungeonDtoToDungeon(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all items from persistent storage.
     * Reconstructs item properties and relationships.
     */
    public static void loadItems() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "armors_snapshot.json")) {
            ArmorDTO[] armorDTOS = GSON.fromJson(reader, ArmorDTO[].class);

            // Create items from DTOs
            for (ArmorDTO dto : armorDTOS) {
                ItemMapper.INSTANCE.itemDtoToItem(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "weapons_snapshot.json")) {
            WeaponDTO[] weaponDTOS = GSON.fromJson(reader, WeaponDTO[].class);

            for (WeaponDTO dto : weaponDTOS) {
                ItemMapper.INSTANCE.itemDtoToItem(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves all items to persistent storage.
     * Includes equipment, inventory items, and their states.
     */
    public static void saveItems() {
        createDirectories();
        List<ArmorDTO> armorDTOs = new ArrayList<>();
        List<WeaponDTO> weaponDTOS = new ArrayList<>();

        for (Armor armor : Item.getItemsByType(Armor.class)) {
            armorDTOs.add((ArmorDTO) ItemMapper.INSTANCE.itemToItemDTO((armor)));
        }
        for (Weapon weapon : Item.getItemsByType(Weapon.class)) {
            weaponDTOS.add((WeaponDTO) ItemMapper.INSTANCE.itemToItemDTO(weapon));
        }

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "armors_snapshot.json")) {
            writer.write(GSON.toJson(armorDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "weapons_snapshot.json")) {
            writer.write(GSON.toJson(weaponDTOS));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves all town locations and their states.
     * Includes shops, NPCs, and other town-specific data.
     */
    public static void saveTowns() {
        createDirectories();
        List<TownDTO> townDTOS = Location.locationMap.values().stream()
                .filter(location -> location instanceof Town)
                .map(location -> (Town) location)
                .map(TownMapper.INSTANCE::townToTownDto)
                .toList();

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "towns_snapshot.json")) {
            writer.write(GSON.toJson(townDTOS));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all town locations and their states.
     * Reconstructs town environments and inhabitants.
     */
    public static void loadTowns() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "towns_snapshot.json")) {
            TownDTO[] townDTOS = GSON.fromJson(reader, TownDTO[].class);

            Location.locationMap.values().removeIf(location -> location instanceof Town);

            for (TownDTO dto : townDTOS) {
                TownMapper.INSTANCE.townDtoToTown(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}