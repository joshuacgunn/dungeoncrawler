package io.github.joshuacgunn.entity;

import io.github.joshuacgunn.container.Inventory;
import io.github.joshuacgunn.gameplay.CombatState;
import io.github.joshuacgunn.gameplay.DungeonState;
import io.github.joshuacgunn.item.Armor;
import io.github.joshuacgunn.item.Item;
import io.github.joshuacgunn.item.Weapon;
import io.github.joshuacgunn.location.DungeonFloor;
import io.github.joshuacgunn.location.Location;
import io.github.joshuacgunn.tickmanager.TickManager;
import io.github.joshuacgunn.tickmanager.Tickable;

import java.util.*;
import java.util.List;

import static io.github.joshuacgunn.item.Potion.consumePotion;

/**
 * Master entity class for which each sub-entity (player, npc, monster, etc.) will inherit
 * @author github.com/joshuacgunn
 * @version 1.0
 */

public abstract class Entity implements Tickable {
    /**
     * The name of the entity.
     */
    protected String entityName;

    /**
     * The unique identifier (UUID) of the entity.
     */
    protected UUID entityUUID;

    private final Inventory inventory;
    protected EntityStats entityStats;

    /**
     * The current health points (HP) of the entity.
     * This will be set in subclasses, but all entities need this field.
     */
    protected float entityHp;

    /**
     * The current weapon equipped by the entity.
     */
    protected Weapon currentWeapon;

    /**
     * Indicates whether the entity is dead.
     */
    protected boolean isAlive = true;

    /**
     * The dungeon the player is currently in
     */
    private Location currentLocation;
    private String currentLocationType;

    /**
     * The total defense value of the entity.
     * This is initialized to 0 and can be increased by equipping armor.
     */
    protected float entityDefense = 0;
    /**
     * A map of all armor pieces equipped by the entity.
     * The key is the armor slot (e.g., "HELMET", "CHESTPLATE"), and the value is the Armor object.
     */
    public Map<Armor.ArmorSlot, Armor> armors = new HashMap<>();

    /**
     * A static map of all entities in the game.
     * The key is the entity's UUID, and the value is the Entity object.
     */
    public static Map<UUID, Entity> entityMap = new HashMap<>();

    private Map<StatusEffect, Integer> activeStatusEffects = new HashMap<>();

    /**
     * @param name Sets entity name
     * @param uuid Unique identifier to be used to identify objects in loading/querying
     */
    public Entity(String name, UUID uuid) {
        this.entityName = name;
        this.entityUUID = uuid;
        entityMap.put(uuid, this);
        inventory = new Inventory(UUID.randomUUID(), this);
        TickManager.getInstance().register(this);
        if (!(this instanceof Player)) {
            entityStats = new EntityStats();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setEntityHp(float amount) {
        this.entityHp = (10 * amount) / 10.0f;
    }

    /**
     * Gets the dungeon the player is currently in.
     *
     * @return The current dungeon, or null if the player is not in a dungeon
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Sets the player's current dungeon.
     *
     * @param currentLocation The dungeon to place the player in
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }


    /**
     * @return A string containing the entity's name
     */
    public String getEntityName() {
        return this.entityName;
    }

    /**
     * @return A unique identifier (UUID) of the entity
     */
    public UUID getEntityUUID() {
        return this.entityUUID;
    }

    /**
     * @return A float value containing the entity's current entityHp
     */
    public float getEntityHp() {
        return this.entityHp;
    }

    /**
     * @return A list of all active entity's
     */
    public static List<Entity> getEntities() {
        return entityMap.values().stream().toList();
    }

    /**
     * @param entityClass The type of entity you want to get a list of (eg, EntityPlayer.class)
     * @return A list of all active entity's of entityClass type
     */
    public static <T extends Entity> List<T> getEntitiesByType(Class<T> entityClass) {
        return entityMap.values().stream().filter(entityClass::isInstance).map(entityClass::cast).toList();
    }

    /**
     * Gets the current weapon equipped by the entity.
     *
     * @return The current weapon equipped by the entity.
     */
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * Sets the current weapon equipped by the entity.
     *
     * @param weapon The weapon to be equipped by the entity.
     */
    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    public void setCurrentLocationType(String currentLocationType) {
        this.currentLocationType = currentLocationType;
    }

    public String getCurrentLocationType() {
        return currentLocationType;
    }

    /**
     * Reduces the entity's health points (HP) by the specified damage amount.
     * If the damage exceeds the current HP, the entity is marked as dead.
     *
     * @param damage The amount of damage to be taken by the entity.
     */
    public void takeDamage(float damage) {
        if (damage > this.entityHp) {
            this.entityHp = 0;
            this.isAlive = false;
            if (this instanceof Enemy) {
                DungeonFloor dungeonFloor = (DungeonFloor) Location.locationMap.get(this.currentLocation.getLocationUUID());
                for (Item item : this.getInventory().getItems()) {
                    Item.itemMap.remove(item.getItemUUID());
                }
                dungeonFloor.getEnemiesOnFloor().remove(this);
                Entity.entityMap.remove(this.getEntityUUID());
            }
        } else {
            this.entityHp -= damage;
        }
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setDeathStatus(boolean status) {
        this.isAlive = status;
    }

    public float getEntityDefense() {
        return this.entityDefense;
    }

    /**
     * Equips the specified armor to the entity.
     * If the armor slot is already occupied, a message is printed.
     * The entity's defense value is increased by the armor's defense value.
     *
     * @param armor The armor to be equipped by the entity.
     */
    public void equipArmor(Armor armor) {
        if (armors.containsKey(armor.getArmorSlot())) {
            System.out.println("You already have an armor equipped in slot " + armor.getArmorSlot());
        } else {
            armors.put(armor.getArmorSlot(), armor);
            entityDefense += armor.getArmorDefense();
            this.getInventory().getItems().remove(armor);
        }
    }

    /**
     * Removes the specified armor from the entity.
     * If the armor is not equipped, a message is printed.
     * The entity's defense value is decreased by the armor's defense value.
     *
     * @param armor The armor to be removed from the entity.
     */
    public void unEquipArmor(Armor armor) {
        if (!armors.containsValue(armor)) {
            System.out.println("You don't have that equipped!");
        } else {
            entityDefense -= armor.getArmorDefense();
            armors.remove(armor.getArmorSlot());
            this.getInventory().addItem(armor);
        }
    }

    /**
     * Gets a list of all armor pieces equipped by the entity.
     *
     * @return A list of all armor pieces equipped by the entity, or null if no armor is equipped.
     */
    public ArrayList<Armor> getArmors() {
        if (armors.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(armors.values());
        }
    }

    public void addItem(Item item) {
        this.inventory.addItem(item);
    }

    @Override
    public void onTick(int currentTick) {
        Iterator<Map.Entry<StatusEffect, Integer>> iterator = activeStatusEffects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<StatusEffect, Integer> entry = iterator.next();
            int remainingDuration = entry.getValue() - 1;

            if (remainingDuration <= 0) {
                removeStatusEffect(entry.getKey());
                iterator.remove();
            } else {
                entry.setValue(remainingDuration);
            }
        }

        // Other time-based logic like health regeneration
        if (currentTick % 10 == 0 && this instanceof Player player) { // Every 10 seconds
            if (!(player.getGameState() instanceof CombatState || player.getGameState() instanceof DungeonState)) { // Don't regenerate health in combat
                if (player.getEntityHp() < 100 && player.getEntityHp() > 0) {
                    player.setEntityHp(player.getEntityHp() + ((float) player.getSpecificStatValue(EntityStats.Stat.VITALITY) / 10));
                }
            }
        }
    }

    public void addStatusEffect(StatusEffect effect, int durationInTicks) {
        activeStatusEffects.put(effect, durationInTicks);
        applyStatusEffect(effect);
    }

    private void applyStatusEffect(StatusEffect effect) {
        // Apply the effect based on its type
        switch (effect) {
            case STRENGTH_POTION:
//                addTemporaryStrength(5);
                break;
            case ALCOHOL:
                if (this instanceof Player player) {
                    consumePotion(this, StatusEffect.ALCOHOL);
                    addTemporaryStat(EntityStats.Stat.STRENGTH, 5);
                    addTemporaryStat(EntityStats.Stat.CHARISMA, 5);
                }
            // Handle other effects
        }
    }

    private void removeStatusEffect(StatusEffect effect) {
        // Remove the effect
        switch (effect) {
            case STRENGTH_POTION:

                break;
            case ALCOHOL:
                if (this instanceof Player player) {
                    removeTemporaryStat(EntityStats.Stat.STRENGTH, 5);
                    removeTemporaryStat(EntityStats.Stat.CHARISMA, 5);
                }
                break;
            // Handle other effects
        }
    }

    public Map<StatusEffect, Integer> getActiveStatusEffects() {
        return activeStatusEffects;
    }

    public void setActiveStatusEffects(Map<StatusEffect, Integer> activeStatusEffects) {
        this.activeStatusEffects = activeStatusEffects;
    }

    public void addTemporaryStat(EntityStats.Stat stat, int amount) {
        if (this instanceof Player player) {
            player.getEntityStats().setStatValue(stat, player.getEntityStats().getStatValue(stat) + amount);
        }
    }

    public void removeTemporaryStat(EntityStats.Stat stat, int amount) {
        if (this instanceof Player player) {
            player.getEntityStats().setStatValue(stat, player.getEntityStats().getStatValue(stat) - amount);
        }
    }

    public EntityStats getEntityStats() {
        return this.entityStats;
    }

    public void setEntityStats(EntityStats entityStats) {
        this.entityStats = entityStats;
    }


    public int getSpecificStatValue(EntityStats.Stat stat) {
        return this.entityStats.getStatValue(stat);
    }

    public enum StatusEffect {
        STRENGTH_POTION,
        SPEED_POTION,
        POISON,
        REGENERATION,
        BLEEDING,
        CHARISMA_POTION,
        ALCOHOL
    }
}
