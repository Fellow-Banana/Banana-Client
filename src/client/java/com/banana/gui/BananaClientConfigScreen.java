package com.banana.gui;

import com.banana.config.BananaClientConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class BananaClientConfigScreen extends Screen {

    private final Screen parent;
    private final BananaClientConfig config;

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

        ButtonWidget autoFishButton = ButtonWidget.builder(
                getAutoFishButtonText(),
                button -> {
                    config.setAutoFishEnabled(!config.isAutoFishEnabled());
                    button.setMessage(getAutoFishButtonText());
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(autoFishButton);

        currentY += buttonHeight + 5;

        ButtonWidget nightVisionButton = ButtonWidget.builder(
                getNightVisionButtonText(),
                button -> {
                    config.setNightVisionEnabled(!config.isNightVisionEnabled());
                    button.setMessage(getNightVisionButtonText());
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(nightVisionButton);

        currentY += buttonHeight + 5;

        ButtonWidget toggleSprintButton = ButtonWidget.builder(
                getToggleSprintButtonText(),
                button -> {
                    config.setToggleSprintActive(!config.isToggleSprintActive());
                    button.setMessage(getToggleSprintButtonText());
                }
        ).position(centerX - buttonWidth / 2, currentY).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(toggleSprintButton);

        ButtonWidget doneButton = ButtonWidget.builder(
                Text.literal("Done"),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(parent);
                }
        ).position(centerX - buttonWidth / 2, this.height - 30).size(buttonWidth, buttonHeight).build();
        this.addDrawableChild(doneButton);
    }

    private Text getAutoFishButtonText() {
        return Text.literal("AutoFishing: " + (config.isAutoFishEnabled() ? "ON" : "OFF"));
    }

    private Text getNightVisionButtonText() {
        return Text.literal("Night Vision: " + (config.isNightVisionEnabled() ? "ON" : "OFF"));
    }

    private Text getToggleSprintButtonText() {
        return Text.literal("Toggle Sprint: " + (config.isToggleSprintActive() ? "ON" : "OFF"));
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