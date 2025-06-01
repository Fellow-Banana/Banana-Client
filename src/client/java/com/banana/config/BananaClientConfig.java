package com.banana.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BananaClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bananaclient.json");
    private static final File CONFIG_FILE = CONFIG_PATH.toFile();

    public boolean autoFishEnabled = false;
    public boolean nightVisionEnabled = false;
    public boolean toggleSprintEnabled = false;
    public boolean autoclickerEnabled = false;
    public int autoclickerDelay = 5;
    public int autoclickerMouseButton = 0;
    public boolean autoclickerHoldToClick = true;
    public boolean noFallEnabled = false;
    public boolean flyEnabled = false;
    public double flySpeedMultiplier = 1.0;
    public boolean tpEnabled = false;
    public boolean noHungerEnabled = false; // This is correctly defined

    private static BananaClientConfig instance;

    public static BananaClientConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static BananaClientConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                BananaClientConfig loadedConfig = GSON.fromJson(reader, BananaClientConfig.class);
                if (loadedConfig != null) {
                    // Ensure new fields get default values if missing from old config file
                    if (loadedConfig.autoclickerDelay == 0) loadedConfig.autoclickerDelay = 5;
                    if (loadedConfig.flySpeedMultiplier == 0.0) loadedConfig.flySpeedMultiplier = 1.0;
                    // For boolean fields, GSON defaults them to false if not present, which is usually desired.
                    return loadedConfig;
                }
            } catch (IOException e) {
                System.err.println("Failed to load Banana Client config: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return new BananaClientConfig();
    }

    public void save() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save Banana Client config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public boolean isAutoFishEnabled() { return autoFishEnabled; }
    public void setAutoFishEnabled(boolean enabled) { this.autoFishEnabled = enabled; save(); }
    public boolean isNightVisionEnabled() { return nightVisionEnabled; }
    public void setNightVisionEnabled(boolean enabled) { this.nightVisionEnabled = enabled; save(); }
    public boolean isToggleSprintEnabled() { return toggleSprintEnabled; }
    public void setToggleSprintEnabled(boolean enabled) { this.toggleSprintEnabled = enabled; save(); }
    public boolean isAutoclickerEnabled() { return autoclickerEnabled; }
    public void setAutoclickerEnabled(boolean enabled) { this.autoclickerEnabled = enabled; save(); }
    public int getAutoclickerDelay() { return autoclickerDelay; }
    public void setAutoclickerDelay(int delay) { this.autoclickerDelay = Math.max(1, delay); save(); }
    public int getAutoclickerMouseButton() { return autoclickerMouseButton; }
    public void setAutoclickerMouseButton(int button) {
        if (button == 0 || button == 1) { // 0 for left click, 1 for right click
            this.autoclickerMouseButton = button;
            save();
        }
    }

    public boolean isAutoclickerHoldToClick() {
        return autoclickerHoldToClick;
    }

    public void setAutoclickerHoldToClick(boolean holdToClick) {
        this.autoclickerHoldToClick = holdToClick;
        save();
    }

    public boolean isNoFallEnabled() {
        return noFallEnabled;
    }

    public void setNoFallEnabled(boolean enabled) {
        this.noFallEnabled = enabled;
        save();
    }

    public boolean isFlyEnabled() {
        return flyEnabled;
    }

    public void setFlyEnabled(boolean enabled) {
        this.flyEnabled = enabled;
        save();
    }

    public double getFlySpeedMultiplier() {
        return flySpeedMultiplier;
    }

    public void setFlySpeedMultiplier(double multiplier) {
        this.flySpeedMultiplier = Math.max(0.1, Math.min(10.0, multiplier)); // Clamp between 0.1 and 10.0
        save();
    }

    public boolean isTpEnabled() {
        return tpEnabled;
    }

    public void setTpEnabled(boolean enabled) {
        this.tpEnabled = enabled;
        save();
    }

    public boolean isNoHungerEnabled() { return noHungerEnabled; } // New: Getter for NoHunger.java
    public void setNoHungerEnabled(boolean enabled) { this.noHungerEnabled = enabled; save(); } // New: Setter for NoHunger.java
}