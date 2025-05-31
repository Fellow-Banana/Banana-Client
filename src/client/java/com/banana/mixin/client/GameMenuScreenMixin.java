package com.banana.mixin.client;

import com.banana.gui.BananaClientConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        int x = (this.width - 200) / 2;
        int y = this.height / 4 + 120 + 24;

        ButtonWidget bananaButton = ButtonWidget.builder(
                Text.literal("Banana Client"),
                b -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.setScreen(new BananaClientConfigScreen(this));
                }
        ).position(x, y).size(200, 20).build();

        this.addDrawableChild(bananaButton);
    }
}