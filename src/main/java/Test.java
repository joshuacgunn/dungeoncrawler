import com.github.joshuacgunn.core.entity.Entity;
import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.save.SaveManager;

import java.io.File;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        try {
            Player player = createPlayer();
            createDungeon();
            for (Entity n : Entity.getEntitiesByType(player.getClass())) {
                System.out.println("Entity name: " + n.getEntityName());
                System.out.println("Entity UUID: " + n.getEntityUUID());
                System.out.println("Entity HP: " + n.getEntityHp());
                System.out.println(n.getCurrentWeapon());
            }
            System.out.println(player.getCurrentDungeon().getLocationName());
            System.out.println(player.getCurrentDungeon().getLocationUUID());
            System.out.println(player.getCurrentDungeon().getFloors());
            System.out.println(player.getCurrentWeapon().getWeaponDamage());
            System.out.println(player.getCurrentWeapon().getItemName());
//            for (Dungeon d : Location.getLocationsByType(Dungeon.class)) {
//                System.out.println(d.getLocationName());
//                System.out.println(d.getLocationUUID());
//                for (DungeonFloor df : d.getFloors()) {
//                    System.out.println(df.getFloorUUID());
//                }
//            }
            SaveManager.saveState(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Player createPlayer() {
        if (!(new File("saves/player_save.json").exists())) {
            Player player = new Player("Josh", UUID.randomUUID());
            player.setCurrentDungeon(new Dungeon("Test", UUID.randomUUID()));
            Weapon weapon = new Weapon("Weapon", UUID.randomUUID(), 5.0f, 10.0f);
            player.getInventory().addItem(weapon);
            player.setCurrentWeapon(weapon);
            Armor armor = new Armor(UUID.randomUUID(), 5.0f, "HELMET", "Armor");
            player.getInventory().addItem(armor);
            player.equipArmor(armor);
            return player;
        } else {
            return SaveManager.loadPlayer();
        }
    }

    public static void createDungeon() {
        Map<UUID, Dungeon> dungeonList = new HashMap<>();
        if (!(new File("saves/dungeon_saves.json").exists())) {
            Dungeon dungeon = new Dungeon("Dungeon", UUID.randomUUID());
//            Dungeon dungeon2 = new Dungeon("Dungeon", UUID.randomUUID());
//            Dungeon dungeon4 = new Dungeon("Dungeon", UUID.randomUUID());
//            Dungeon dungeon3 = new Dungeon("Dungeon", UUID.randomUUID());
        } else {
            SaveManager.loadDungeons();
        }
    }
}