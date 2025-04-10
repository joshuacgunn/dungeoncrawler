package com.joshuacgunn.core.save;

import com.joshuacgunn.core.dto.DungeonDTO;
import com.joshuacgunn.core.dto.EntityDTO;
import com.joshuacgunn.core.dto.ItemDTO;
import com.joshuacgunn.core.dto.PlayerDTO;
import com.joshuacgunn.core.entity.Entity;
import com.joshuacgunn.core.entity.Player;
import com.joshuacgunn.core.item.Item;
import com.joshuacgunn.core.location.Dungeon;
import com.joshuacgunn.core.location.Location;
import com.joshuacgunn.core.mapper.DungeonMapper;
import com.joshuacgunn.core.mapper.EntityMapper;
import com.joshuacgunn.core.mapper.ItemMapper;
import org.apache.commons.io.FileUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import static com.joshuacgunn.core.gson.GsonProvider.GSON;

public abstract class SaveManager {
    private static final String SAVE_DIRECTORY = "saves/";
    private static final String BACKUP_DIRECTORY = "backups/";
    private static final SimpleDateFormat date = new SimpleDateFormat("dd_HH.mm.ss");

    public static void saveState(Player player) {
        saveItems();
        saveEntities();
        saveDungeons();
        savePlayer(player);
        backupSave();
    }

    public static void loadState() {
        // The order of this is critical for functionality. It will not work if changed.
        loadItems();
        loadEntities();
        loadDungeons();
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
            EntityDTO[] entityDTOs = GSON.fromJson(reader, EntityDTO[].class);

            // Remove all non-player entities before loading
            Entity.entityMap.values().removeIf(entity -> !(entity instanceof Player));

            // Create entities from DTOs
            for (EntityDTO dto : entityDTOs) {
                EntityMapper.INSTANCE.entityDtoToEntity(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}