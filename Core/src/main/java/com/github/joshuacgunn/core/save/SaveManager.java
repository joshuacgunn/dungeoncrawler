package com.github.joshuacgunn.core.save;

import com.github.joshuacgunn.core.dto.*;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Item;
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

public abstract class SaveManager {
    private static final String SAVE_DIRECTORY = "saves/";
    private static final String BACKUP_DIRECTORY = "backups/";
    private static final SimpleDateFormat date = new SimpleDateFormat("dd_HH.mm.ss");

    public static void saveState(Player player) {
        // The order of this is critical for functionality. It will not work if changed.
        saveItems();
        saveEntities();
        saveDungeons();
        saveTowns();
        savePlayer(player);
        backupSave();
    }

    public static void loadState() {
        // The order of this is critical for functionality. It will not work if changed.
        loadItems();
        loadEntities();
        loadDungeons();
        loadTowns();
        loadPlayer();
    }

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


    public static void savePlayer(Player player) {
        createDirectories();
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "player_save.json")) {

            PlayerDTO playerDTO = (PlayerDTO) EntityMapper.INSTANCE.entityToEntityDTO(player);
            writer.write(GSON.toJson(playerDTO, PlayerDTO.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveEntities() {
        createDirectories();
        List<EntityDTO> entityDTOs = new ArrayList<>();

        // Get all entities except player (which is saved separately)
        for (Entity entity : Entity.getEntities()) {
            if (!(entity instanceof Player)) {
                entityDTOs.add(EntityMapper.INSTANCE.entityToEntityDTO(entity));
            }
        }

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "entities_snapshot.json", false)) {
            writer.write(GSON.toJson(entityDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Player loadPlayer() {
        // Check for backup in case player_save.json is deleted somehow
        File directory = new File(BACKUP_DIRECTORY + "player/");
        File[] files = directory.listFiles();
        String chosenFile = null;
        long lastModifiedTime = Long.MIN_VALUE;

        if (files != null && !((new File(SAVE_DIRECTORY + "player_save.json")).exists())) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file.toString();
                    lastModifiedTime = file.lastModified();
                }
            }
            try (Reader reader = new FileReader(chosenFile)) {
                // Read specifically as PlayerDTO rather than EntityDTO
                PlayerDTO dto = GSON.fromJson(reader, PlayerDTO.class);
                return (Player) EntityMapper.INSTANCE.entityDtoToEntity(dto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (Reader reader = new FileReader(SAVE_DIRECTORY + "player_save.json")) {
                // Read specifically as PlayerDTO rather than EntityDTO
                PlayerDTO dto = GSON.fromJson(reader, PlayerDTO.class);
                return (Player) EntityMapper.INSTANCE.entityDtoToEntity(dto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void loadEntities() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "entities_snapshot.json")) {
            // Use TypeToken for proper generic type handling
            Type listType = new TypeToken<List<EntityDTO>>(){}.getType();
            List<EntityDTO> entityDTOs = GSON.fromJson(reader, listType);

            // Clear existing entities
            Entity.entityMap.clear();

            // Create entities from DTOs
            for (EntityDTO dto : entityDTOs) {
                EntityMapper.INSTANCE.entityDtoToEntity(dto);
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

    public static void loadItems() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "items_snapshot.json")) {
            ItemDTO[] itemDTOs = GSON.fromJson(reader, ItemDTO[].class);

            Item.itemMap.clear();

            // Create items from DTOs
            for (ItemDTO dto : itemDTOs) {
                ItemMapper.INSTANCE.itemDtoToItem(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveItems() {
        createDirectories();
        List<ItemDTO> itemDTOs = new ArrayList<>();

        for (Item item : Item.getItems()) {
            itemDTOs.add(ItemMapper.INSTANCE.itemToItemDTO(item));
        }

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "items_snapshot.json")) {
            writer.write(GSON.toJson(itemDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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