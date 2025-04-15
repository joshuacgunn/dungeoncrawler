import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.Town;
import com.github.joshuacgunn.core.save.SaveManager;

import java.io.File;
import java.util.*;

import static com.github.joshuacgunn.core.item.Armor.generateArmor;
import static com.github.joshuacgunn.core.item.Weapon.generateWeapon;

public class Test {
    public static void main(String[] args) {
        try {
            Player player = createPlayer();
            SaveManager.saveState(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Player createPlayer() {
        if (!(new File("saves/player_save.json").exists())) {
            Player player = new Player("Josh", UUID.randomUUID());
            Dungeon dungeon = new Dungeon("Test", UUID.randomUUID(), true);
            player.setCurrentLocation(dungeon.getLocationUUID());
            Weapon weapon = generateWeapon(3, 3, player.getInventory());
            player.setCurrentWeapon(weapon);
            Town town = new Town("TestTown", UUID.randomUUID(), 2, true);
            generateArmor(3, 3, player.getInventory());
            return player;
        } else {
            SaveManager.loadState();
            return SaveManager.loadPlayer();
        }
    }
}