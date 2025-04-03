package com.github.joshuacgunn.core.save;

import com.github.joshuacgunn.core.dto.DungeonDTO;
import com.github.joshuacgunn.core.dto.EntityDTO;
import com.github.joshuacgunn.core.dto.ItemDTO;
import com.github.joshuacgunn.core.dto.PlayerDTO;
import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Item;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Location;
import com.github.joshuacgunn.core.mapper.DungeonMapper;
import com.github.joshuacgunn.core.mapper.EntityMapper;
import com.github.joshuacgunn.core.mapper.ItemMapper;
import org.apache.commons.io.FileUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import static com.github.joshuacgunn.core.gson.GsonProvider.GSON;

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
            // Cast to PlayerDTO to ensure the right type is passed to Gson
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

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "entities_save.json", false)) {
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
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "entities_save.json")) {
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
            manageBackupDirectory("saves", 10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void manageBackupDirectory(String subdir, int maxFiles) {
        File dir = new File(BACKUP_DIRECTORY + subdir + "/");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > maxFiles) {
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

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "dungeon_saves.json")) {
            writer.write(GSON.toJson(dungeonDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static void loadItems() {
        try (Reader reader = new FileReader(SAVE_DIRECTORY + "items_save.json")) {
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

        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "items_save.json")) {
            writer.write(GSON.toJson(itemDTOs));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}