package com.banana;

import com.banana.config.BananaClientConfig;
import com.banana.features.nightvision.NightVision;
import com.banana.features.togglesprint.ToggleSprint;
import net.fabricmc.api.ClientModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BananaClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Banana Client initializing client features...");

        BananaClientConfig.getInstance();

        NightVision nightVisionFeature = new NightVision();
        nightVisionFeature.onInitializeClient();

        ToggleSprint toggleSprintFeature = new ToggleSprint();
        toggleSprintFeature.onInitializeClient();

        LOGGER.info("Banana Client client features initialized.");
    }
}