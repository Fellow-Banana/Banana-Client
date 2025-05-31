package com.banana.features.tp;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TP implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_TP");

    @Override
    public void onInitializeClient() {
        LOGGER.info("TP feature entrypoint initialized. Registering client tick event (if needed).");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Placeholder for any tick-based logic related to TP
            if (BananaClientConfig.getInstance().isTpEnabled()) {
                // Your TP logic that runs every tick goes here.
                // For a command-based TP, this section might remain empty
                // or handle specific post-teleport actions.
            }
        });
    }

    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("TP: " + status), false);
        }
    }

    public static void teleportPlayer(MinecraftClient client, double x, double y, double z) {
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            LOGGER.warn("Cannot teleport: Client, player, or network handler is null.");
            return;
        }

        ClientPlayerEntity player = client.player;
        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

        // 1. Send packet to server informing new position and that player is NOT on ground
        // This is crucial for the server to know you're in the air.
        networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                x, y, z,
                false, // Explicitly false for falling
                player.horizontalCollision
        ));

        // 2. Update client-side position immediately
        player.setPos(x, y, z);

        // 3. Force client-side onGround status to false
        // This directly tells the client's internal state that it's no longer grounded.
        player.setOnGround(false);

        // 4. Reset fall distance
        // This prevents fall damage from the teleport itself.
        player.fallDistance = 0.0f;

        // 5. Apply a tiny downward velocity to "kick-start" the fall physics
        // This makes the client immediately recognize it should be falling.
        // We preserve existing X/Z velocity to not interfere with player momentum.
        player.setVelocity(player.getVelocity().x, -0.1, player.getVelocity().z);


        LOGGER.info("Attempted to teleport player to X:{}, Y:{}, Z:{}", x, y, z);
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Teleported to X:§b" + String.format("%.1f", x) + "§r Y:§b" + String.format("%.1f", y) + "§r Z:§b" + String.format("%.1f", z)), false);
        }
    }
}