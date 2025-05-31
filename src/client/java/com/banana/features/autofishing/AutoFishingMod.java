package com.banana.features.autofishing;

import com.banana.config.BananaClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoFishingMod implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("AutoFishingMod");
    private static MinecraftClient client;
    private static int rodReelCountdown = 0;

    @Override
    public void onInitializeClient() {
        client = MinecraftClient.getInstance();
        LOGGER.info("AutoFishingMod initialized");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BananaClientConfig.getInstance().isAutoFishEnabled()) {
                if (rodReelCountdown > 0) {
                    rodReelCountdown--;
                    if (rodReelCountdown == 0) {
                        useRod();
                    }
                }
            }
        });
    }

    public static void triggerRecastCountdown() {
        if (BananaClientConfig.getInstance().isAutoFishEnabled()) {
            rodReelCountdown = 10;
        }
    }

    public static void useRod() {
        if (client == null || client.player == null || client.world == null) return;

        if (!BananaClientConfig.getInstance().isAutoFishEnabled()) {
            return;
        }

        for (Hand hand : Hand.values()) {
            ItemStack stack = client.player.getStackInHand(hand);
            if (stack.getItem() instanceof FishingRodItem) {
                if (client.interactionManager != null) {
                    ActionResult result = client.interactionManager.interactItem(client.player, hand);
                    if (result.isAccepted()) {
                        client.player.swingHand(hand);
                        client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                    }
                }
                break;
            }
        }
    }
}