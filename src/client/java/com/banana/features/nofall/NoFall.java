package com.banana.features.nofall;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text; // Import for Text

public class NoFall implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!BananaClientConfig.getInstance().isNoFallEnabled()) {
                return;
            }

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                return;
            }

            if (!player.isOnGround() && player.fallDistance > 0) {
                if (isFallingFastEnoughToCauseDamage(player)) {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc != null && mc.getNetworkHandler() != null) {
                        ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
                        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.Full(
                                player.getX(),
                                player.getY(),
                                player.getZ(),
                                player.getYaw(),
                                player.getPitch(),
                                true, // Force onGround to true
                                player.horizontalCollision
                        );
                        networkHandler.sendPacket(packet);
                    }
                }
            }
        });
    }

    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }

    // Helper method for chat messages
    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            client.player.sendMessage(Text.literal("NoFall: " + status), false);
        }
    }
}