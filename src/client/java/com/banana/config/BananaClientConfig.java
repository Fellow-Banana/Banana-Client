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
    public boolean toggleSprintActive = false;

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
                return GSON.fromJson(reader, BananaClientConfig.class);
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

    public boolean isAutoFishEnabled() {
        return autoFishEnabled;
    }

    public void setAutoFishEnabled(boolean enabled) {
        this.autoFishEnabled = enabled;
        save();
    }

    public boolean isNightVisionEnabled() {
        return nightVisionEnabled;
    }

    public void setNightVisionEnabled(boolean enabled) {
        this.nightVisionEnabled = enabled;
        save();
    }

    public boolean isToggleSprintActive() {
        return toggleSprintActive;
    }

    public void setToggleSprintActive(boolean active) {
        this.toggleSprintActive = active;
        save();
    }
}