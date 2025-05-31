package com.banana.features.nightvision;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text; // Import for Text

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NightVision implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_NightVision");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Night Vision feature entrypoint initialized. Registering client tick event.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BananaClientConfig.getInstance().isNightVisionEnabled()) {
                if (client.player != null && client.world != null) {
                    if (!client.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, true, false, false));
                    }
                }
            } else {
                if (client.player != null && client.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                }
            }
        });
    }

    // Helper method for chat messages
    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("Night Vision: " + status), false);
        }
    }
}