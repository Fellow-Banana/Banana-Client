package com.banana.mixin.client;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    // Shadow the chatField (the TextFieldWidget responsible for the input)
    @Shadow protected TextFieldWidget chatField;

    // Inject code after the chatField is initialized and its max length is set
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setMaxLength(I)V", shift = At.Shift.AFTER))
    private void banana_removeChatLimit(CallbackInfo ci) {
        // Set the max length to a much higher value, effectively removing the limit
        // (Integer.MAX_VALUE is a common way to denote "no practical limit")
        this.chatField.setMaxLength(Integer.MAX_VALUE);
    }
}