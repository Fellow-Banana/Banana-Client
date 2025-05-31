package com.banana.features.fly;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.text.Text; // Import for Text
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fly implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_Fly");

    private static final float BASE_CREATIVE_FLY_SPEED = 0.05f;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Fly feature entrypoint initialized. Registering client tick event.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if (player == null) {
                return;
            }

            PlayerAbilities abilities = player.getAbilities();

            if (BananaClientConfig.getInstance().isFlyEnabled()) {
                if (!abilities.allowFlying) {
                    abilities.allowFlying = true;
                    abilities.flying = true;
                    player.sendAbilitiesUpdate();
                }

                float desiredSpeed = BASE_CREATIVE_FLY_SPEED * (float) BananaClientConfig.getInstance().getFlySpeedMultiplier();
                if (abilities.getFlySpeed() != desiredSpeed) {
                    abilities.setFlySpeed(desiredSpeed);
                    player.sendAbilitiesUpdate();
                }

            } else {
                if (abilities.allowFlying && !player.isCreative() && !player.isSpectator()) {
                    abilities.allowFlying = false;
                    abilities.flying = false;
                    abilities.setFlySpeed(BASE_CREATIVE_FLY_SPEED);
                    player.sendAbilitiesUpdate();
                }
            }
        });
    }

    // Helper method for chat messages
    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("Fly: " + status), false);
        }
    }
}