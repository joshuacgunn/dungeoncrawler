package io.github.joshuacgunn.core.tickmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickManager {
    private static final int TICK_RATE_MS = 1000; // Tick every second
    private static TickManager instance;
    private final ScheduledExecutorService scheduler;
    private final List<Tickable> tickables;
    private boolean isRunning;
    private int currentTick;

    private TickManager() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.tickables = new ArrayList<>();
        this.isRunning = false;
        this.currentTick = 0;
    }

    public static TickManager getInstance() {
        if (instance == null) {
            instance = new TickManager();
        }
        return instance;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            scheduler.scheduleAtFixedRate(this::tick, 0, TICK_RATE_MS, TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }

    private void tick() {
        currentTick++;
        for (Tickable tickable : new ArrayList<>(tickables)) {
            tickable.onTick(currentTick);
        }
    }

    public void register(Tickable tickable) {
        if (!tickables.contains(tickable)) {
            tickables.add(tickable);
        }
    }

    public void unregister(Tickable tickable) {
        tickables.remove(tickable);
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int tick) {
        this.currentTick = tick;
    }
}
