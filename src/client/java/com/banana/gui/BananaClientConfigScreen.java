package com.banana.gui;

import com.banana.config.BananaClientConfig;
import com.banana.features.autoclicker.AutoClicker;
import com.banana.features.autofishing.AutoFishingMod;
import com.banana.features.fly.Fly;
import com.banana.features.nightvision.NightVision;
import com.banana.features.nofall.NoFall;
import com.banana.features.togglesprint.ToggleSprint;
import com.banana.features.tp.TP; // NEW: Import TP

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;

public class BananaClientConfigScreen extends Screen {

    private final Screen parent;
    private final BananaClientConfig config;

    private ButtonWidget flySpeedLabel;
    private ButtonWidget autoclickerDelayLabel; // NEW: Reference for autoclicker delay label

    public BananaClientConfigScreen(Screen parent) {
        super(Text.literal("Banana Client Settings"));
        this.parent = parent;
        this.config = BananaClientConfig.getInstance();
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = this.width / 2;
        int currentY = this.height / 4 + 20;

        // AutoFishing Button
        ButtonWidget autoFishButton = ButtonWidget.builder(
                getAutoFishButtonText(),
                button -> {
                    boolean newState = !config.isAutoFishEnabled();
                    config.setAutoFishEnabled(newState);
                    button.setMessage(getAutoFishButtonText());
                    AutoFishingMod.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(autoFishButton);

        currentY += buttonHeight + 5;

        // Night Vision Button
        ButtonWidget nightVisionButton = ButtonWidget.builder(
                getNightVisionButtonText(),
                button -> {
                    boolean newState = !config.isNightVisionEnabled();
                    config.setNightVisionEnabled(newState);
                    button.setMessage(getNightVisionButtonText());
                    NightVision.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(nightVisionButton);

        currentY += buttonHeight + 5;

        // Toggle Sprint Button
        ButtonWidget toggleSprintButton = ButtonWidget.builder(
                getToggleSprintButtonText(),
                button -> {
                    boolean newState = !config.isToggleSprintEnabled();
                    config.setToggleSprintEnabled(newState);
                    button.setMessage(getToggleSprintButtonText());
                    ToggleSprint.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(toggleSprintButton);

        currentY += buttonHeight + 5;

        // NoFall Button
        ButtonWidget noFallButton = ButtonWidget.builder(
                getNoFallButtonText(),
                button -> {
                    boolean newState = !config.isNoFallEnabled();
                    config.setNoFallEnabled(newState);
                    button.setMessage(getNoFallButtonText());
                    NoFall.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(noFallButton);

        currentY += buttonHeight + 5;

        // Fly Toggle Button
        ButtonWidget flyButton = ButtonWidget.builder(
                getFlyButtonText(),
                button -> {
                    boolean newState = !config.isFlyEnabled();
                    config.setFlyEnabled(newState);
                    button.setMessage(getFlyButtonText());
                    Fly.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(flyButton);

        currentY += buttonHeight + 5;

        // Fly Speed Label
        int thirdWidth = (buttonWidth - 10) / 3;
        flySpeedLabel = ButtonWidget.builder(
                getFlySpeedText(),
                button -> {}
        ).position(centerX - buttonWidth / 2, currentY).size(thirdWidth, buttonHeight).build();
        flySpeedLabel.active = false;
        this.addDrawableChild(flySpeedLabel);

        // Fly Speed Decrease Button
        ButtonWidget flySpeedDecreaseButton = ButtonWidget.builder(
                Text.literal("-"),
                button -> {
                    config.setFlySpeedMultiplier(config.getFlySpeedMultiplier() - 0.1);
                    flySpeedLabel.setMessage(getFlySpeedText());
                }
        ).position(centerX - buttonWidth / 2 + thirdWidth + 5, currentY).size(thirdWidth / 2, buttonHeight).build();
        this.addDrawableChild(flySpeedDecreaseButton);

        // Fly Speed Increase Button
        ButtonWidget flySpeedIncreaseButton = ButtonWidget.builder(
                Text.literal("+"),
                button -> {
                    config.setFlySpeedMultiplier(config.getFlySpeedMultiplier() + 0.1);
                    flySpeedLabel.setMessage(getFlySpeedText());
                }
        ).position(centerX - buttonWidth / 2 + thirdWidth + 5 + thirdWidth / 2 + 5, currentY).size(thirdWidth / 2, buttonHeight).build();
        this.addDrawableChild(flySpeedIncreaseButton);

        currentY += buttonHeight + 5; // NEW: Space for TP button

        // NEW: TP Button
        ButtonWidget tpButton = ButtonWidget.builder(
                getTpButtonText(),
                button -> {
                    boolean newState = !config.isTpEnabled();
                    config.setTpEnabled(newState);
                    button.setMessage(getTpButtonText());
                    TP.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(tpButton);


        currentY += buttonHeight + 15;

        // Autoclicker Toggle Button
        ButtonWidget autoclickerToggleButton = ButtonWidget.builder(
                getAutoclickerToggleButtonText(),
                button -> {
                    boolean newState = !config.isAutoclickerEnabled();
                    config.setAutoclickerEnabled(newState);
                    button.setMessage(getAutoclickerToggleButtonText());
                    AutoClicker.sendToggleMessage(MinecraftClient.getInstance(), newState);
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(autoclickerToggleButton);

        currentY += buttonHeight + 5;

        // Autoclicker Delay Label
        autoclickerDelayLabel = ButtonWidget.builder( // Assign to the field
                getAutoclickerDelayText(),
                button -> {}
        ).position(centerX - buttonWidth / 2, currentY).size(thirdWidth, buttonHeight).build();
        autoclickerDelayLabel.active = false;
        this.addDrawableChild(autoclickerDelayLabel);

        // Autoclicker Delay Decrease Button
        ButtonWidget autoclickerDelayDecreaseButton = ButtonWidget.builder(
                Text.literal("-"),
                button -> {
                    config.setAutoclickerDelay(config.getAutoclickerDelay() - 1);
                    autoclickerDelayLabel.setMessage(getAutoclickerDelayText()); // Update label
                }
        ).position(centerX - buttonWidth / 2 + thirdWidth + 5, currentY).size(thirdWidth / 2, buttonHeight).build();
        this.addDrawableChild(autoclickerDelayDecreaseButton);

        // Autoclicker Delay Increase Button
        ButtonWidget autoclickerDelayIncreaseButton = ButtonWidget.builder(
                Text.literal("+"),
                button -> {
                    config.setAutoclickerDelay(config.getAutoclickerDelay() + 1);
                    autoclickerDelayLabel.setMessage(getAutoclickerDelayText()); // Update label
                }
        ).position(centerX - buttonWidth / 2 + thirdWidth + 5 + thirdWidth / 2 + 5, currentY).size(thirdWidth / 2, buttonHeight).build();
        this.addDrawableChild(autoclickerDelayIncreaseButton);

        currentY += buttonHeight + 5;

        // Autoclicker Mouse Button Button
        ButtonWidget autoclickerMouseButtonButton = ButtonWidget.builder(
                getAutoclickerMouseButtonText(),
                button -> {
                    int newButton = (config.getAutoclickerMouseButton() == 0) ? 1 : 0;
                    config.setAutoclickerMouseButton(newButton);
                    button.setMessage(getAutoclickerMouseButtonText());
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(autoclickerMouseButtonButton);

        currentY += buttonHeight + 5;

        // Autoclicker Hold to Click Button
        ButtonWidget autoclickerHoldToClickButton = ButtonWidget.builder(
                getAutoclickerHoldToClickText(),
                button -> {
                    boolean newState = !config.isAutoclickerHoldToClick();
                    config.setAutoclickerHoldToClick(newState);
                    button.setMessage(getAutoclickerHoldToClickText());
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(autoclickerHoldToClickButton);

        // Done Button
        ButtonWidget doneButton = ButtonWidget.builder(
                Text.literal("Done"),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(parent);
                }
        ).position(centerX - buttonWidth / 2, this.height - 30).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(doneButton);
    }

    // Helper methods for button text
    private Text getAutoFishButtonText() {
        return Text.literal("AutoFishing: " + (config.isAutoFishEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getNightVisionButtonText() {
        return Text.literal("Night Vision: " + (config.isNightVisionEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getToggleSprintButtonText() {
        return Text.literal("Toggle Sprint: " + (config.isToggleSprintEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getNoFallButtonText() {
        return Text.literal("NoFall: " + (config.isNoFallEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getFlyButtonText() {
        return Text.literal("Fly: " + (config.isFlyEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getFlySpeedText() {
        return Text.literal("Speed: §b" + String.format("%.1f", config.getFlySpeedMultiplier()) + "x");
    }

    // NEW: TP Button Text
    private Text getTpButtonText() {
        return Text.literal("TP: " + (config.isTpEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getAutoclickerToggleButtonText() {
        return Text.literal("Autoclicker: " + (config.isAutoclickerEnabled() ? "§aON" : "§cOFF"));
    }

    private Text getAutoclickerMouseButtonText() {
        String buttonText = (config.getAutoclickerMouseButton() == 0) ? "Left" : "Right";
        return Text.literal("Autoclicker Button: §b" + buttonText);
    }

    private Text getAutoclickerDelayText() {
        return Text.literal("Delay: §e" + config.getAutoclickerDelay() + " Ticks");
    }

    private Text getAutoclickerHoldToClickText() {
        return Text.literal("Hold to Click: " + (config.isAutoclickerHoldToClick() ? "§aON" : "§cOFF"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
        config.save();
    }
}