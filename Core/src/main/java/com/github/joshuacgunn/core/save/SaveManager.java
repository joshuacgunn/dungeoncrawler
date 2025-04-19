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

    public static Player loadState() {
        // The order of this is critical for functionality. It will not work if changed.
        loadItems();
        loadEntities();
        loadDungeons();
        loadTowns();
        return loadPlayer();
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

    public static void savePlayer(Player player) {
        createDirectories();
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "player_save.json")) {

            PlayerDTO playerDTO = (PlayerDTO) EntityMapper.INSTANCE.entityToEntityDTO(player);
            writer.write(GSON.toJson(playerDTO, PlayerDTO.class));
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
                Player player = (Player) EntityMapper.INSTANCE.entityDtoToEntity(dto);
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
            Item.itemMap.values().removeIf(e -> e instanceof Armor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer writer = new FileWriter(SAVE_DIRECTORY + "weapons_snapshot.json")) {
            writer.write(GSON.toJson(weaponDTOS));
            Item.itemMap.values().removeIf(e -> e instanceof Weapon);
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