import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.item.Armor;
import com.github.joshuacgunn.core.item.Weapon;
import com.github.joshuacgunn.core.location.Dungeon;
import com.github.joshuacgunn.core.location.DungeonFloor;
import com.github.joshuacgunn.core.save.SaveManager;

import java.io.File;
import java.util.*;

import static com.github.joshuacgunn.core.item.Armor.generateArmor;

public class Test {
    public static void main(String[] args) {
        try {
            Player player = createPlayer();
            for (DungeonFloor df : player.getCurrentDungeon().getFloors()) {
                System.out.println(df.getDifficultyRating());
                System.out.println(df.getEnemiesOnFloor().size());
            }
            SaveManager.saveState(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Player createPlayer() {
        Random rand = new Random();
        int i = rand.nextInt(0, 5);
        if (!(new File("saves/player_save.json").exists())) {
            Player player = new Player("Josh", UUID.randomUUID());
            player.setCurrentDungeon(new Dungeon("Test", UUID.randomUUID(), true));
            Weapon weapon = new Weapon("Weapon", UUID.randomUUID(), 5.0f, 10.0f);
            player.getInventory().addItem(weapon);
            player.setCurrentWeapon(weapon);
            generateArmor(3, Armor.ArmorQuality.values()[3], true, player);
            return player;
        } else {
            SaveManager.loadState();
            return SaveManager.loadPlayer();
        }
    }
}