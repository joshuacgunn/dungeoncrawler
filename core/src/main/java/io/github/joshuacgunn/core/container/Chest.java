package io.github.joshuacgunn.core.container;

import io.github.joshuacgunn.core.item.Armor;
import io.github.joshuacgunn.core.item.Item;
import io.github.joshuacgunn.core.item.Weapon;
import io.github.joshuacgunn.core.location.DungeonFloor;
import org.reflections.Reflections;

import java.util.*;

/**
 * Represents a chest container that can hold items in a dungeon floor.
 * Chests have different rarity levels which affect the quantity and quality
 * of items they contain. Chests can be locked, requiring a specific key to open.
 * Each chest belongs to a specific dungeon floor and generates its contents
 * based on the floor's difficulty and its own rarity.
 */
public class Chest extends Container{
    /** Whether this chest is locked and requires a key to open */
    private boolean locked;

    /** The UUID of the key that can unlock this chest, if locked */
    private UUID keyUUID;

    /** The rarity level of this chest, affecting loot quantity and quality */
    private ChestRarity chestRarity;

    /** The dungeon floor this chest belongs to */
    private DungeonFloor parentFloor;

    /**
     * Defines the rarity levels for chests, each with an associated loot multiplier.
     * Higher rarity chests have higher loot multipliers, resulting in more or better items.
     */
    public enum ChestRarity {
        /** Common rarity chest with 0.5x loot multiplier */
        COMMON(0.5f),

        /** Uncommon rarity chest with 1.0x loot multiplier */
        UNCOMMON(1.0f),

        /** Rare rarity chest with 1.5x loot multiplier */
        RARE(1.5f),

        /** Epic rarity chest with 2.0x loot multiplier */
        EPIC(2.0f),

        /** Legendary rarity chest with 2.5x loot multiplier */
        LEGENDARY(2.5f);

        /** The multiplier that affects the quantity and quality of loot in this chest */
        public final float lootMultiplier;

        /**
         * Constructor for ChestRarity enum values.
         *
         * @param lootMultiplier The multiplier to apply to loot generation for this rarity
         */
        ChestRarity(float lootMultiplier) {
            this.lootMultiplier = lootMultiplier;
        }
    }

    /**
     * Creates a new chest with the specified rarity, lock status, and parent floor.
     * Upon creation, the chest automatically generates its contents based on
     * the floor difficulty and its own rarity level.
     *
     * @param chestRarity The rarity level of the chest
     * @param locked Whether the chest is locked and requires a key
     * @param parentFloor The dungeon floor this chest belongs to
     */
    public Chest(ChestRarity chestRarity, UUID uuid, boolean locked, DungeonFloor parentFloor) {
        super(uuid,  "Chest");
        this.containerName = chestRarity + " Chest";
        this.chestRarity = chestRarity;
        this.locked = locked;
        this.parentFloor = parentFloor;
        this.items = generateItems();
    }

    /**
     * Generates a list of items for this chest based on the parent floor's difficulty,
     * chest rarity, and randomization factors.
     *
     * <p>The algorithm determines the number of items to generate using:
     * <ol>
     *   <li>Base count derived from the parent floor's difficulty rating</li>
     *   <li>Chest rarity multiplier (better chests contain more items)</li>
     *   <li>Random variation factor (±20% randomization)</li>
     *   <li>Floor number bonus (5% more items per floor depth)</li>
     * </ol>
     *
     * <p>The resulting count is capped between 0 and 8 items. If no items
     * are generated or the floor difficulty is below 5, the chest will be
     * marked as not present on the floor.
     *
     * @param <T> Type parameter extending Item for the generated items
     * @return A list of generated items for this chest
     */
    public <T extends Item> List<T> generateItems() {
        Random random = new Random();

        // Base item count from floor difficulty
        float baseCount = parentFloor.getDifficultyRating();

        // Apply chest rarity multiplier to increase items for better chests
        float modifiedCount = baseCount * chestRarity.lootMultiplier;

        // Add slight randomization (+/- 20%)
        float randomFactor = 0.8f + (random.nextFloat() * 0.4f); // 0.8 to 1.2
        modifiedCount *= randomFactor;

        // Floor number bonus (5% more items per floor)
        float floorBonus = 1.0f + (0.05f * parentFloor.getFloorNumber());
        modifiedCount *= floorBonus;

        // Ensure at least 1 item for non-empty chests, cap at reasonable maximum
        int itemCount = Math.max(0, Math.min(8, Math.round(modifiedCount)));

        // Generate the items
        List<T> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            T item = generateItem();
            if (item != null) {
                items.add(item);
            }
        }

        // Remove chest if certain requirements are not met (floor # below 3, floor too easy)
        if (items.isEmpty() || parentFloor.getDifficultyRating() < 1.3 || (random.nextFloat() * parentFloor.getDifficultyRating() / 3) < 0.75f) {
            parentFloor.setHasChest(false);
        }

        return items;
    }

    /**
     * Generates a random item for this chest based on reflection and probability.
     *
     * <p>This method uses reflection to discover all subclasses of Item in the
     * com.joshuacgunn package. It then generates an item based on calculated
     * probabilities, where each item type has an equal chance of being selected.
     *
     * <p>Currently, this implementation specifically supports:
     * <ul>
     *   <li>Armor - generated if random value falls in first probability segment</li>
     *   <li>Weapon - generated if random value falls in second probability segment</li>
     * </ul>
     *
     * <p>The quality and attributes of the generated item will be determined by
     * the respective item generation methods, with level range 0-4 passed to
     * the item generators.
     *
     * @param <T> The type parameter extending Item for the returned item
     * @return A randomly generated item of type T, or null if generation fails
     *         or random chance doesn't select any item type
     * @throws ClassCastException (caught internally) if there's a type mismatch during item generation
     */
    @SuppressWarnings("unchecked")
    public <T extends Item> T generateItem() {
        try {
            Random rand = new Random();
            Reflections reflection = new Reflections("com.github.joshuacgunn");
            Set<Class<? extends Item>> itemClasses = reflection.getSubTypesOf(Item.class);
            int extendedClasses = 0;
            for (Class<?> itemClass : itemClasses) {
                extendedClasses += 1;
            }

            Item.ItemRarity rarityToUse;
            float rarityChance = rand.nextFloat();
            if (parentFloor.getDifficultyRating() < 3) {
                rarityToUse = Item.ItemRarity.COMMON;
            } else if (parentFloor.getDifficultyRating() < 6) {
                rarityToUse = Item.ItemRarity.values()[rand.nextInt(0, 2)];
                if (rarityChance > .8f) {
                    rarityToUse = Item.ItemRarity.UNCOMMON;
                }
            } else if (parentFloor.getDifficultyRating() < 9) {
                rarityToUse = Item.ItemRarity.RARE;
            } else {
                rarityToUse = Item.ItemRarity.EPIC;
            }

            float generatePercent = (100.0f / extendedClasses) / 100.0f;

            float chanceToGenerate = rand.nextFloat();
            if (chanceToGenerate < generatePercent) {
                return (T) Armor.generateArmor(rarityToUse, this, false);
            } else if (chanceToGenerate < generatePercent * 2) {
                return (T) Weapon.generateWeapon(rarityToUse, this);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the rarity level of this chest.
     *
     * @return The chest's rarity level
     */
    public ChestRarity getChestRarity() {
        return this.chestRarity;
    }
}
