package com.banana.features.autoclicker;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoClicker implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient_AutoClicker");
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        LOGGER.info("AutoClicker initialized.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.player == null || client.world == null) return;
            if (!BananaClientConfig.getInstance().isAutoclickerEnabled()) {
                tickCounter = 0;
                return;
            }

            boolean shouldClick = !BananaClientConfig.getInstance().isAutoclickerHoldToClick();

            if (!shouldClick) {
                long window = client.getWindow().getHandle();
                int button = BananaClientConfig.getInstance().getAutoclickerMouseButton();
                if (GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS) {
                    shouldClick = true;
                }
            }

            if (shouldClick && client.currentScreen == null) {
                tickCounter++;
                if (tickCounter >= BananaClientConfig.getInstance().getAutoclickerDelay()) {
                    tickCounter = 0;

                    int button = BananaClientConfig.getInstance().getAutoclickerMouseButton();
                    ClientPlayerInteractionManager interactionManager = client.interactionManager;
                    HitResult hitResult = client.crosshairTarget;

                    if (interactionManager == null) return;

                    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        if (hitResult != null) {
                            switch (hitResult.getType()) {
                                case BLOCK -> {
                                    BlockHitResult blockHit = (BlockHitResult) hitResult;
                                    interactionManager.attackBlock(blockHit.getBlockPos(), blockHit.getSide());
                                }
                                case ENTITY -> {
                                    EntityHitResult entityHit = (EntityHitResult) hitResult;
                                    interactionManager.attackEntity(client.player, entityHit.getEntity());
                                }
                                case MISS -> {
                                    // Just swing
                                }
                            }
                            client.player.swingHand(Hand.MAIN_HAND);
                        }
                    } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                        if (hitResult != null) {
                            switch (hitResult.getType()) {
                                case BLOCK -> {
                                    BlockHitResult blockHit = (BlockHitResult) hitResult;
                                    interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
                                }
                                case ENTITY -> {
                                    EntityHitResult entityHit = (EntityHitResult) hitResult;
                                    interactionManager.interactEntity(client.player, entityHit.getEntity(), Hand.MAIN_HAND);
                                }
                                case MISS -> {
                                    interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                                }
                            }
                            client.player.swingHand(Hand.MAIN_HAND);
                        }
                    }
                }
            } else {
                tickCounter = 0;
            }
        });
    }

    // Existing helper method for chat messages
    public static void sendToggleMessage(MinecraftClient client, boolean enabled) {
        if (client != null && client.player != null) {
            String status = enabled ? "§aON" : "§cOFF";
            String button = BananaClientConfig.getInstance().getAutoclickerMouseButton() == 0 ? "LEFT" : "RIGHT";
            int delay = BananaClientConfig.getInstance().getAutoclickerDelay();
            String hold = BananaClientConfig.getInstance().isAutoclickerHoldToClick() ? " (Hold)" : "";
            client.player.sendMessage(Text.literal("Autoclicker: " + status + " (Button: §b" + button + "§r, Delay: §e" + delay + "§r ticks" + hold + ")"), false);
        }
    }
}