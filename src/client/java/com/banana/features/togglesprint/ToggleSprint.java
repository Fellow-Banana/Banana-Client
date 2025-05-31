package com.banana.features.togglesprint;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToggleSprint implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_ToggleSprint");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Toggle Sprint feature entrypoint initialized. Registering client tick event.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                if (BananaClientConfig.getInstance().isToggleSprintActive()) {
                    LOGGER.info("ToggleSprint: ACTIVE (Config ON)");

                    boolean isMovingForward = client.player.input.hasForwardMovement();
                    boolean isSneaking = client.player.isSneaking();
                    boolean isSprintingCurrently = client.player.isSprinting();
                    boolean isHoldingVanillaSprintKey = client.options.sprintKey.isPressed();

                    LOGGER.info("  hasForwardMovement: " + isMovingForward);
                    LOGGER.info("  isSneaking: " + isSneaking);
                    LOGGER.info("  isSprinting (before action): " + isSprintingCurrently);
                    LOGGER.info("  isHoldingVanillaSprintKey: " + isHoldingVanillaSprintKey);

                    if (isMovingForward && !isSneaking) {
                        if (!isSprintingCurrently) {
                            client.player.setSprinting(true);
                            LOGGER.info("  Action: FORCING SPRINT to TRUE.");
                        } else {
                            LOGGER.info("  Action: Already sprinting, no change needed by us.");
                        }
                    } else {
                        LOGGER.info("  Conditions to force sprint NOT met (not moving forward or is sneaking).");
                        if (isSprintingCurrently && !isHoldingVanillaSprintKey) {
                            client.player.setSprinting(false);
                            LOGGER.info("  Action: Turning OFF forced sprint (not holding vanilla key).");
                        } else {
                            LOGGER.info("  Action: No change to sprinting (either not sprinting or holding vanilla key).");
                        }
                    }
                    LOGGER.info("  isSprinting (after action): " + client.player.isSprinting());

                } else {
                    LOGGER.info("ToggleSprint: INACTIVE (Config OFF)");
                    if (client.player.isSprinting() && !client.options.sprintKey.isPressed()) {
                        client.player.setSprinting(false);
                        LOGGER.info("  Action: Set sprinting to FALSE because Toggle Sprint is OFF.");
                    } else {
                        LOGGER.info("  Action: No change to sprinting (Toggle Sprint OFF).");
                    }
                }
            }
        });
    }
}