package com.github.joshuacgunn.core.save;


import com.github.joshuacgunn.core.dto.DungeonDTO;
import com.github.joshuacgunn.core.dto.PlayerDTO;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.mapper.DungeonMapper;
import com.github.joshuacgunn.core.mapper.PlayerMapper;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;

import static com.github.joshuacgunn.core.gson.GsonProvider.GSON;

/**
 * Manages the saving and loading functionality for game data.
 * This utility class provides methods to persist and retrieve player data and dungeon information.
 * It uses a DTO (Data Transfer Object) pattern with mapper classes to convert between
 * entity objects and their serializable representations.
 * <p>
 * The class uses GSON for JSON serialization/deserialization and maintains both primary saves
 * and backups to prevent data loss. It handles directory creation and management automatically.
 */
public abstract class SaveManager {
    /** Directory path where primary save files are stored */
    private static final String SAVE_DIRECTORY = "saves/";

    /** Directory path where backup save files are stored */
    private static final String BACKUP_DIRECTORY = "backups/";

    /** Gson instance configured for pretty printing used for all JSON operations */
    /** Date formatter used for timestamping backup files */
    private static SimpleDateFormat date = new SimpleDateFormat("dd_HH.mm.ss");

    public static void saveState(Player player) {
        savePlayer(player);
        saveDungeons();
    }

    /**
     * Initializes the required directories for save files if they don't already exist.
     * Creates the main save directory, backup directory, and player backup directory.
     * Any exceptions during directory creation are printed to the standard error stream.
     */
    public static void createDirectories() {
        try {
            File save_player = new File(SAVE_DIRECTORY);
            File backup = new File(BACKUP_DIRECTORY);
            File backup_player = new File(BACKUP_DIRECTORY + "player");
            if (!save_player.exists()) {
                save_player.mkdirs();
            }
            if (!backup.exists()) {
                backup.mkdirs();
            }
            if (!backup_player.exists()) {
                backup_player.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves player data to the file system in JSON format.
     * This method converts the Player entity to a DTO, serializes it using Gson,
     * and writes it to the save directory. It also creates a backup of the player data.
     *
     * @param player The player object to save
     * @throws RuntimeException If an I/O error occurs during the save process
     */
    public static void savePlayer(Player player) {
        createDirectories();
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "player_save.json")) {
            PlayerDTO playerDTO = PlayerMapper.INSTANCE.playerToPlayerDTO(player);
            writer.write(GSON.toJson(playerDTO, PlayerDTO.class));
            backupPlayer(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads player data from the file system.
     * First attempts to load from the primary save file. If that file doesn't exist,
     * it falls back to the most recently modified backup file. The loaded JSON is
     * deserialized into a PlayerDTO and then mapped back to a Player entity.
     *
     * @return A reconstructed Player object, or null if no save data could be loaded
     * @throws RuntimeException If an I/O error occurs when loading from the primary save
     */
    public static Player loadPlayer() {
        // Check for backup in case player_save.json is deleted somehow
        File directory = new File(BACKUP_DIRECTORY + "player/");
        File[] files = directory.listFiles();
        String chosenFile = null;
        long lastModifiedTime = Long.MIN_VALUE;
        long firstModifiedTime = Long.MAX_VALUE;
        if (files != null && !((new File(SAVE_DIRECTORY + "player_save.json")).exists())) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file.toString();
                    lastModifiedTime = file.lastModified();
                }
            }
            try (Reader reader = new FileReader(chosenFile)) {
                PlayerDTO dto = GSON.fromJson(reader, PlayerDTO.class);
                return PlayerMapper.INSTANCE.playerDtoToPlayer(dto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Load player normally
        } else {
            try (Reader reader = new FileReader(SAVE_DIRECTORY + "player_save.json")) {
                PlayerDTO dto = GSON.fromJson(reader, PlayerDTO.class);
                return PlayerMapper.INSTANCE.playerDtoToPlayer(dto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * Creates a timestamped backup of the player's data.
     * Manages the backup directory by removing the oldest backup file when
     * the number of backups exceeds 10. Each backup is named with a timestamp
     * to facilitate chronological organization and retrieval.
     *
     * @param player The player object to back up
     * @throws RuntimeException If an I/O error occurs during the backup process
     */
    public static void backupPlayer(Player player) {
        // Deletes the earliest existing backup if there are more than 10 backups
        File file = new File(BACKUP_DIRECTORY + "player/");
        if ((file.exists())) {
            File[] files = file.listFiles();
            String chosenFile = null;
            if (files.length > 10) {
                long firstModifiedFile = Long.MAX_VALUE;
                for (File file1 : files) {
                    if (file1.lastModified() < firstModifiedFile) {
                        firstModifiedFile = file1.lastModified();
                        chosenFile = file1.toString();
                    }
                }
                File delete = new File(chosenFile);
                delete.delete();
            }
        }
        try (Writer writer = new FileWriter(BACKUP_DIRECTORY +  "player/player_saves_backup_" + (date.format(new Date())) + ".json")) {
            PlayerDTO playerDTO = PlayerMapper.INSTANCE.playerToPlayerDTO(player);
            writer.write(GSON.toJson(playerDTO, PlayerDTO.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves all dungeon data to the file system in JSON format.
     * Filters all locations to find Dungeon instances, converts them to DTOs,
     * and serializes the collection to a single JSON file.
     *
     * @throws RuntimeException If an I/O error occurs during the save process
     */
    public static void saveDungeons() {
        createDirectories();
        List<DungeonDTO> dungeonDTOs = Location.locationMap.values().stream()
                .filter(location -> location instanceof Dungeon)
                .map(location -> (Dungeon) location)
                .map(DungeonMapper.INSTANCE::dungeonToDungeonDto)
                .toList();

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "dungeon_saves.json")) {
            writer.write(GSON.toJson(dungeonDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Loads all dungeon data from the file system.
     * Deserializes the dungeon save file into an array of DungeonDTOs,
     * removes any existing Dungeon instances from the location map,
     * and recreates the dungeons from the loaded data.
     *
     * @throws RuntimeException If an I/O error occurs during the load process
     */
    public static void loadDungeons() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "dungeon_saves.json")) {
            DungeonDTO[] dungeonDTOs = GSON.fromJson(reader, DungeonDTO[].class);

            Location.locationMap.values().removeIf(location -> location instanceof Dungeon);

            for (DungeonDTO dto : dungeonDTOs) {
                DungeonMapper.INSTANCE.dungeonDtoToDungeon(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}