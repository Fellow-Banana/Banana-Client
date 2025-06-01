package com.banana.features.nohunger;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.banana.BananaClient; // Import your main client class for the logger

public class NoHunger implements ClientModInitializer {

    public void onInitializeClient() {
        // No special initialization needed here as the core logic is in the mixin.
        // This class primarily exists to manage the toggle message and keep consistency
        // with other features.
        BananaClient.LOGGER.info("NoHunger feature initialized.");
    }

    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("No Hunger: " + status), false);
        }
    }
}