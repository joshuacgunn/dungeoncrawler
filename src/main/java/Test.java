import com.joshuacgunn.core.entity.Player;
import com.joshuacgunn.core.item.Weapon;
import com.joshuacgunn.core.location.Dungeon;
import com.joshuacgunn.core.location.Town;
import com.joshuacgunn.core.save.SaveManager;

import java.io.File;
import java.util.*;

import static com.joshuacgunn.core.item.Armor.generateArmor;
import static com.joshuacgunn.core.item.Weapon.generateWeapon;

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
            player.setCurrentDungeon(new Dungeon("Test", UUID.randomUUID(), true));
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