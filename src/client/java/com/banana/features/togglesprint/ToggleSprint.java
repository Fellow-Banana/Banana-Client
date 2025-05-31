package com.banana.features.togglesprint;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text; // Import for Text

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToggleSprint implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_ToggleSprint");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Toggle Sprint feature entrypoint initialized. Registering client tick event.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                if (BananaClientConfig.getInstance().isToggleSprintEnabled()) {
                    boolean isMovingForward = client.player.input.hasForwardMovement();
                    boolean isSneaking = client.player.isSneaking();
                    boolean isSprintingCurrently = client.player.isSprinting();
                    boolean isHoldingVanillaSprintKey = client.options.sprintKey.isPressed();

                    if (isMovingForward && !isSneaking) {
                        if (!isSprintingCurrently) {
                            client.player.setSprinting(true);
                        }
                    } else {
                        if (isSprintingCurrently && !isHoldingVanillaSprintKey) {
                            client.player.setSprinting(false);
                        }
                    }
                } else {
                    if (client.player.isSprinting() && !client.options.sprintKey.isPressed()) {
                        client.player.setSprinting(false);
                    }
                }
            }
        });
    }

    // Helper method for chat messages
    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("Toggle Sprint: " + status), false);
        }
    }
}