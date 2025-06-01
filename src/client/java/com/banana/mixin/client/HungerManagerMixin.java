package com.banana.mixin.client;

import com.banana.config.BananaClientConfig;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {

    // Inject at the beginning of the 'update' method.
    // The 'update' method is called every tick and handles hunger/saturation decay.
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void banana_preventHungerDecay(CallbackInfo ci) {
        // Check if the NoHunger feature is enabled in your configuration
        if (BananaClientConfig.getInstance().isNoHungerEnabled()) {
            // Cast 'this' to HungerManager to access its public methods
            HungerManager self = (HungerManager) (Object) this;

            // Set food level to maximum (20)
            self.setFoodLevel(20);
            // Set saturation level to maximum (20.0f)
            self.setSaturationLevel(20.0f);

            // If it's enabled, cancel the original 'update' method.
            // This prevents the game's default hunger and saturation decay logic from running.
            ci.cancel();
        }
    }
}
