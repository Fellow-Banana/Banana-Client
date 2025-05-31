package com.banana.mixin.client;

import com.banana.config.BananaClientConfig;
import com.banana.features.autofishing.AutoFishingMod;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow private boolean caughtFish;

    @Unique private boolean lastCaughtFish = false;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (BananaClientConfig.getInstance().isAutoFishEnabled()) {
            if (caughtFish && !lastCaughtFish) {
                AutoFishingMod.useRod();
                AutoFishingMod.triggerRecastCountdown();
            }
        }
        lastCaughtFish = caughtFish;
    }
}