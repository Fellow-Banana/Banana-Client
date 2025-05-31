package com.banana.mixin.client;

import com.banana.config.BananaClientConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class FlyPlayerMovementMixin {

    @Unique
    private static int flyTickCounter = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void bananaClient$onClientPlayerTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;

        if (BananaClientConfig.getInstance().isFlyEnabled()) {
            PlayerAbilities abilities = player.getAbilities();
            if (!abilities.allowFlying) {
                abilities.allowFlying = true;
                abilities.flying = true;
                player.sendAbilitiesUpdate();
            }

            player.fallDistance = 0.0f;

            flyTickCounter++;

            if (flyTickCounter >= 40 && !player.isOnGround()) {
                flyTickCounter = 0;
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        player.getX(),
                        player.getY() - 0.04, // Move slightly down
                        player.getZ(),
                        true, // Faking onGround
                        player.horizontalCollision
                ));
            }
        } else {
            // When Fly is NOT enabled, reset abilities if they were modified by the mod
            PlayerAbilities abilities = player.getAbilities();
            if (abilities.allowFlying && !player.isCreative() && !player.isSpectator()) {
                abilities.allowFlying = false;
                abilities.flying = false;
                player.sendAbilitiesUpdate();
            }
            flyTickCounter = 0;
        }
    }
}